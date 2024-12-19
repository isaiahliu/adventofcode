package y2024

import util.expect
import util.input
import kotlin.math.sign

fun main() {
    expect(0, 0L) {
        fun String.prefixFunction(word: String): Set<Int> {
            return buildSet {
                val s = "$word#${this@prefixFunction}"
                val pi = IntArray(s.length)
                for (i in 1 until s.length) {
                    var j = pi[i - 1]
                    while (j > 0 && s[i] != s[j]) {
                        j = pi[j - 1]
                    }

                    if (s[i] == s[j]) {
                        j++
                    }

                    pi[i] = j

                    if (j == word.length) {
                        add(i - word.length)
                    }
                }
            }
        }

        val words = hashSetOf<String>()

        var readTarget = false
        input.forEach {
            when {
                it.isEmpty() -> {
                    readTarget = true
                }

                !readTarget -> {
                    words += it.split(", ")
                }

                else -> {
                    val endsWithWordCount = Array(it.length + 1) {
                        hashSetOf<Int>()
                    }

                    words.forEach { word ->
                        it.prefixFunction(word).forEach {
                            endsWithWordCount[it] += word.length
                        }
                    }
                    val dp = LongArray(it.length + 1) { 1L - it.sign }
                    for (i in 1 until dp.size) {
                        endsWithWordCount[i].forEach { length ->
                            dp[i] += dp[i - length]
                        }
                    }

                    dp.last().also {
                        part1Result += it.sign
                        part2Result += it
                    }
                }
            }
        }
    }
}