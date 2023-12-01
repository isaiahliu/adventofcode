package y2023

import util.input

fun main() {
    val part1Result = input.sumOf {
        (it.first { it in '0'..'9' } - '0') * 10 + (it.last { it in '0'..'9' } - '0')
    }

    fun String.startsWithAt(target: String, offset: Int): Boolean {
        target.forEachIndexed { index, c ->
            if (this.getOrNull(offset + index) != c) {
                return false
            }
        }

        return true
    }

    val nums = arrayOf(
        "one" to 1,
        "two" to 2,
        "three" to 3,
        "four" to 4,
        "five" to 5,
        "six" to 6,
        "seven" to 7,
        "eight" to 8,
        "nine" to 9,
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
                    nums.forEach { (s, n) ->
                        if (it.startsWithAt(s, index)) {
                            num = n
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

    println(part1Result)
    println(part2Result)
}
