package me.kofesst.android.moneyapp

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import me.kofesst.android.moneyapp.databinding.ActivityMainBinding
import me.kofesst.android.moneyapp.worker.SubscriptionsWorker
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(
            binding.fragmentContainer.id
        ) as NavHostFragment
        binding.bottomNavBar.setupWithNavController(navHostFragment.navController)

        navHostFragment.navController.apply {
            addOnDestinationChangedListener { _, destination, _ ->
                when (destination.id) {
                    R.id.assetsFragment, R.id.categoriesFragment, R.id.historyFragment,
                    R.id.subscriptionsFragment -> {
                        binding.bottomNavBar.visibility = View.VISIBLE
                    }
                    else -> {
                        binding.bottomNavBar.visibility = View.GONE
                    }
                }
            }
        }

        setupSubscriptionWorker()
    }

    private fun setupSubscriptionWorker() {
        val workRequest = PeriodicWorkRequest.Builder(
            SubscriptionsWorker::class.java,
            15L,
            TimeUnit.MINUTES
        ).build()
        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            SubscriptionsWorker.TAG,
            ExistingPeriodicWorkPolicy.REPLACE,
            workRequest
        )
    }
}