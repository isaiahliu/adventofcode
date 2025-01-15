package y2015

import util.expect
import util.input

fun main() {
    expect(0, 0) {
        val vowels = "aeiou".toSet()

        val disallowMap = listOf("ab", "cd", "pq", "xy").associate {
            it[1] to it[0]
        }

        input.filter { it.isNotBlank() }.forEach {
            var vowelCount = 0
            var double = false
            var disallowExists = false

            var previousChar = Char(0)
            var previousChar2 = Char(0)

            var abaExists = false
            val doubleSet = hashMapOf<String, MutableList<Int>>()

            it.forEachIndexed { index, ch ->
                if (ch in vowels) {
                    vowelCount++
                }

                if (previousChar == ch) {
                    double = true
                }

                if (disallowMap[ch] == previousChar) {
                    disallowExists = true
                }

                if (ch == previousChar2) {
                    abaExists = true
                }

                "${previousChar}${ch}".also {
                    doubleSet.computeIfAbsent(it) { arrayListOf() } += index
                }

                previousChar2 = previousChar
                previousChar = ch
            }

            if (vowelCount >= 3 && double && !disallowExists) {
                part1Result++
            }

            if (abaExists && doubleSet.values.any { it.last() - it.first() > 1 }) {
                part2Result++
            }
        }
    }
}
