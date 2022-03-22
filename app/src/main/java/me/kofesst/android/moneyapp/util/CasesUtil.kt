package me.kofesst.android.moneyapp.util

class CasesUtil {
    companion object {
        private val InvalidCasesWord = CasesWord(
            "INVALID",
            "invalid", "invalid", "invalid"
        )

        private val Words: MutableMap<String, CasesWord> =
            mutableMapOf(Pair(InvalidCasesWord.uid, InvalidCasesWord))

        fun registerWord(uid: String, firstCase: String, secondCase: String, thirdCase: String) {
            registerWord(CasesWord(uid, firstCase, secondCase, thirdCase))
        }

        fun registerWord(word: CasesWord) {
            if (Words.containsKey(word.uid.lowercase())) return
            Words[word.uid.lowercase()] = word
        }

        fun getWord(uid: String): CasesWord {
            return Words.getOrElse(uid.lowercase()) { InvalidCasesWord }
        }

        fun getCase(uid: String, amount: Int, includeAmount: Boolean = true): String {
            return "${if (includeAmount) amount.toString() else ""} %s".format(
                getWord(uid).getCase(amount)
            )
        }
    }
}

data class CasesWord(
    val uid: String,
    val firstCase: String,  // секунда
    val secondCase: String, // секунды
    val thirdCase: String   // секунд
) {
    fun getCase(amount: Int): String {
        if (amount in 11..19) return thirdCase

        return when (amount % 10) {
            1 -> firstCase
            in 2..4 -> secondCase
            else -> thirdCase
        }
    }
}