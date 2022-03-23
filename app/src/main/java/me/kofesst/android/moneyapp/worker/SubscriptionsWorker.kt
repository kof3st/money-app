package me.kofesst.android.moneyapp.worker

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters
import me.kofesst.android.moneyapp.R
import me.kofesst.android.moneyapp.database.MainDatabase
import me.kofesst.android.moneyapp.model.TransactionEntity
import me.kofesst.android.moneyapp.model.default.SubscriptionTypes
import me.kofesst.android.moneyapp.util.formatWithCurrency
import java.util.*

class SubscriptionsWorker(
    private val context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {
    private val notificationTitle = context.getString(R.string.subscriptions)
    private val notificationText = context.getString(R.string.subscriptions_updated)
    private val notificationCreditText = context.getString(R.string.credit_line)
    private val notificationDebitText = context.getString(R.string.debit_line)

    companion object {
        const val TAG = "subscriptions_worker"
        const val CHANNEL_ID = "MoneyAppNotifies"
    }

    private val notificationManager = context.getSystemService(
        Context.NOTIFICATION_SERVICE
    ) as NotificationManager
    private val notificationId: Int = 2

    override suspend fun doWork(): Result {
        setForegroundAsync(createForegroundInfo(context.getString(R.string.update_subscriptions)))
        updateSubscriptions(
            iterationCallback = {
                setForegroundAsync(
                    createForegroundInfo(
                        it
                    )
                )
            },
            resultCallback = { credit, debit, amount ->
                sendResultNotification(credit, debit, amount)
            }
        )

        return Result.success()
    }

    private fun sendResultNotification(credit: Double, debit: Double, count: Int) {
        if (count == 0) return

        val style = NotificationCompat.InboxStyle()
            .addLine(notificationCreditText.format(credit.formatWithCurrency(sign = true)))
            .addLine(notificationDebitText.format(debit.formatWithCurrency(sign = true)))

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_baseline_attach_money_24)
            .setContentTitle(notificationTitle)
            .setContentText(notificationText.format(count))
            .setStyle(style)
            .build()

        with(NotificationManagerCompat.from(context)) {
            notify(notificationId, notification)
        }
    }

    private suspend fun updateSubscriptions(
        iterationCallback: (String) -> Unit,
        resultCallback: (Double, Double, Int) -> Unit
    ) {
        val database = MainDatabase.get(context)
        val assetsDao = database.getAssetsDao()
        val transactionsDao = database.getTransactionsDao()
        val subscriptions = database.getSubscriptionsDao().getSubscriptions().toList()

        var creditAmount = 0.0
        var debitAmount = 0.0
        var count = 0

        subscriptions.forEach { subscription ->
            iterationCallback(subscription.title)

            val transaction = transactionsDao.getSubscriptionTransaction(
                subscriptionId = subscription.subscriptionId
            )

            val now = Calendar.getInstance()
            if (subscription.day != now.get(Calendar.DAY_OF_MONTH)) return@forEach

            if (transaction != null) {
                val transactionDate = Calendar.getInstance()
                transactionDate.timeInMillis = transaction.date

                if (transactionDate.get(Calendar.MONTH) == now.get(Calendar.MONTH)) return@forEach
            }

            val asset = assetsDao.getAsset(subscription.assetId) ?: return@forEach
            val subscriptionType = SubscriptionTypes.values()[subscription.type]
            val amount = subscription.amount *
                    if (subscriptionType == SubscriptionTypes.DEBIT) -1
                    else 1

            val newTransaction = TransactionEntity(
                categoryId = null,
                categoryName = context.getString(R.string.subscription),
                assetId = subscription.assetId,
                assetName = asset.name,
                title = subscription.title,
                amount = amount,
                subscriptionId = subscription.subscriptionId
            )
            transactionsDao.addTransaction(newTransaction)

            asset.balance += amount
            assetsDao.updateAsset(asset)

            if (amount > 0.0) creditAmount += amount
            else debitAmount += amount

            count++
        }

        resultCallback(creditAmount, debitAmount, count)
    }

    private fun tryCreateNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = context.getString(R.string.subscription)
            val descriptionText = context.getString(R.string.transaction_amount)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
                enableLights(false)
                enableVibration(false)
            }

            // Register the channel with the system
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun createForegroundInfo(progress: String): ForegroundInfo {
        tryCreateNotificationChannel()

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle(context.getString(R.string.update_subscriptions))
            .setContentText(progress)
            .setSmallIcon(R.drawable.ic_baseline_attach_money_24)
            .setDefaults(Notification.DEFAULT_LIGHTS or Notification.DEFAULT_SOUND)
            .setVibrate(longArrayOf(0))
            .build()

        return ForegroundInfo(1, notification)
    }

}