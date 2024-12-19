package y2024

import util.expect
import util.input
import kotlin.math.sign

fun main() {

    expect(0, 0L) {
        fun String.endsWithPrefixIndices(word: String): Set<Int> {
            return buildSet {
                val s = "$word#$this"
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
                        add(i - word.length - 1)
                    }
                }
            }
        }

        val words = hashSetOf<String>()

        var readTarget = false
        input.forEach { target ->
            when {
                target.isEmpty() -> {
                    readTarget = true
                }

                readTarget -> {
                    val endsWithWordCount = Array(target.length) {
                        hashMapOf<Int, Int>()
                    }

                    words.forEach { word ->
                        target.endsWithPrefixIndices(word).forEach {
                            endsWithWordCount[it][word.length] = (endsWithWordCount[it][word.length] ?: 0) + 1
                        }
                    }
                    val dp = LongArray(target.length) {
                        endsWithWordCount[it][it + 1]?.toLong() ?: 0L
                    }

                    for (i in 1 until dp.size) {
                        endsWithWordCount[i].forEach { (length, count) ->
                            dp.getOrNull(i - length)?.also {
                                dp[i] += it * count
                            }
                        }
                    }

                    dp.last().also {
                        part1Result += it.sign
                        part2Result += it
                    }
                }

                else -> {
                    words += target.split(", ")
                }
            }
        }

    }
}