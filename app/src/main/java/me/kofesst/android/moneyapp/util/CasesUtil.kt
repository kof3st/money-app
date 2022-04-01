package me.kofesst.android.moneyapp.util

class CasesUtil {
    companion object {
        private val InvalidCasesWord = CasesWord(
            "INVALID",
            "invalid", "invalid", "invalid"
        )

        private val Words: MutableMap<String, CasesWord> =
            mutableMapOf(Pair(InvalidCasesWord.uid, InvalidCasesWord))

        /**
         * Регистрирует изменяющееся слово.
         */
        fun registerWord(uid: String, firstCase: String, secondCase: String, thirdCase: String) {
            registerWord(CasesWord(uid, firstCase, secondCase, thirdCase))
        }

        /**
         * Регистрирует изменяющееся слово.
         */
        fun registerWord(word: CasesWord) {
            if (Words.containsKey(word.uid.lowercase())) return
            Words[word.uid.lowercase()] = word
        }

        /**
         * Возвращает зарегистрированное
         * изменяющееся слово с [uid].
         * В случае, если слово с таким [CasesWord.uid]
         * не зарегистрированно, возвращает [CasesUtil.InvalidCasesWord].
         */
        fun getWord(uid: String): CasesWord {
            return Words.getOrElse(uid.lowercase()) { InvalidCasesWord }
        }

        /**
         * Возвращает вариант изменяющегося слова
         * с [uid]. Если [includeAmount] = true,
         * то в форматированную строку добавляется [amount].
         */
        fun getCase(uid: String, amount: Int, includeAmount: Boolean = true): String {
            return "${if (includeAmount) amount.toString() else ""} %s".format(
                getWord(uid).getCase(amount)
            )
        }
    }
}

data class CasesWord(
    val uid: String,
    val firstCase: String,
    val secondCase: String,
    val thirdCase: String
) {
    /**
     * Возвращает вариант слова по [amount].
     */
    fun getCase(amount: Int): String {
        if (amount in 11..19) return thirdCase

        return when (amount % 10) {
            1 -> firstCase
            in 2..4 -> secondCase
            else -> thirdCase
        }
    }
}