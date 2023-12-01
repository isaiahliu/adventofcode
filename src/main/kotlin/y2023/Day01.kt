package y2023

import util.input

fun main() {
    val part1Result = input.sumOf {
        (it.first { it in '0'..'9' } - '0') * 10 + (it.last { it in '0'..'9' } - '0')
    }
    println(part1Result)

    fun String.startsWithAt(target: String, offset: Int): Boolean {
        target.forEachIndexed { index, c ->
            if (this.getOrNull(offset + index) != c) {
                return false
            }
        }

        return true
    }

    val nums = arrayOf(
        "one",
        "two",
        "three",
        "four",
        "five",
        "six",
        "seven",
        "eight",
        "nine",
    )
    var part2Result = 0
    input.forEach {
        var first = 0
        var last = 0

        it.forEachIndexed { index, c ->
            var num: Int? = null
            when (c) {
                in '0'..'9' -> {
                    num = c - '0'
                }

                else -> {
                    for ((n, s) in nums.withIndex()) {
                        if (it.startsWithAt(s, index)) {
                            num = n + 1
                            break
                        }
                    }
                }
            }

            num?.also {
                first = first.takeIf { it > 0 } ?: it
                last = it
            }
        }

        part2Result += first * 10 + last
    }

    println(part2Result)
}
