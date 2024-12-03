package y2024

import util.expect
import util.input

fun main() {
    expect(0, 0) {
        var t = 1

        """(mul)\((\d{1,3}),(\d{1,3})\)|(do)\(\)|(don't)\(\)""".toRegex().findAll(input.joinToString()).forEach {
            when {
                it.groupValues[1] == "mul" -> {
                    (it.groupValues[2].toInt().toInt() * it.groupValues[3].toInt().toInt()).also {
                        part1Result += it
                        part2Result += it * t
                    }
                }

                it.groupValues[4] == "do" -> t = 1
                it.groupValues[5] == "don't" -> t = 0
            }

        }
    }
}