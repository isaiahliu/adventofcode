package y2015

import input
import kotlin.streams.toList

fun main() {
    var part1Sum = 0
    var part2Sum = 0

    val vowels = "aeiou".chars().toList()

    val disallowMap = listOf("ab", "cd", "pq", "xy").associate {
        it[1].code to it[0].code
    }

    input.filter { it.isNotBlank() }.forEach {
        var vowelCount = 0
        var double = false
        var disallowExists = false

        var previousChar = 0
        var previousChar2 = 0

        var abaExists = false
        val doubleSet = arrayListOf<String>()

        it.chars().forEach {
            if (it in vowels) {
                vowelCount++
            }

            if (previousChar == it) {
                double = true
            }

            if (disallowMap[it] == previousChar) {
                disallowExists = true
            }

            if (it == previousChar2) {
                abaExists = true
            }

            doubleSet.add("${previousChar}_${it}")

            previousChar2 = previousChar
            previousChar = it
        }

        if (vowelCount >= 3 && double && !disallowExists) {
            part1Sum++
        }

        if (abaExists && doubleSet.mapIndexed { index, s -> s to index }.groupBy({ it.first }, { it.second }).filter {
                    it.value.max() - it.value.min() > 1
                }.isNotEmpty()) {
            part2Sum++
        }
    }

    println(part1Sum)
    println(part2Sum)
}
