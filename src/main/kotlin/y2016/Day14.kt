package y2016

import input
import md5

fun main() {
    val salt = input.first()
//    val salt = "abc"

    fun String.findDupChars(times: Int): Char? {
        for (index in 0..length - times) {
            val current = this[index]

            if ((1 until times).all {
                        current == this[index + it]
                    }) {
                return current
            }
        }

        return null
    }

    fun process(extra: Int): Int {
        val md5Cache = hashMapOf<Int, String>()
        fun Int.md5(): String {
            return md5Cache.computeIfAbsent(this) {
                (0..extra).fold("${salt}${this}") { a, _ ->
                    a.md5
                }
            }
        }

        val pentCache = hashMapOf<Int, Char?>()
        fun Int.pentChar(): Char? {
            return pentCache.computeIfAbsent(this) {
                it.md5().findDupChars(5)
            }
        }

        fun Int.tripleChar(): Char? {
            return md5().findDupChars(3)
        }

        var currentNum = -1

        var foundKeys = 0
        while (foundKeys < 64) {
            currentNum++

            val tripleChar = currentNum.tripleChar()
            if (tripleChar != null) {
                for (i in 0 until 1000) {
                    if ((currentNum + i + 1).pentChar() == tripleChar) {
                        foundKeys++
                        break
                    }
                }
            }
        }

        return currentNum
    }


    println(process(0))
    println(process(2016))
}