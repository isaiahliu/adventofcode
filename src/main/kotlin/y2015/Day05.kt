package y2015

import util.expect
import util.input

fun main() {
    expect(0, 0) {
        val vowels = "aeiou".toSet()

        val disallowMap = listOf("ab", "cd", "pq", "xy").associate {
            it[1] to it[0]
        }

        input.forEach {
            var vowelCount = 0
            var double = false
            var disallowExists = false

            var previousChar = Char(0)
            var previousChar2 = Char(0)

            var abaExists = false
            val pairIndices = hashMapOf<String, MutableList<Int>>()

            it.forEachIndexed { index, ch ->
                if (ch in vowels) {
                    vowelCount++
                }

                double = double || previousChar == ch
                disallowExists = disallowExists || disallowMap[ch] == previousChar
                abaExists = abaExists || ch == previousChar2
                pairIndices.computeIfAbsent("${previousChar}${ch}") { arrayListOf() } += index

                previousChar2 = previousChar
                previousChar = ch
            }

            if (vowelCount >= 3 && double && !disallowExists) {
                part1Result++
            }

            if (abaExists && pairIndices.values.any { it.last() - it.first() > 1 }) {
                part2Result++
            }
        }
    }
}
