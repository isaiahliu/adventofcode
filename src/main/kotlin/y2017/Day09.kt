package y2017

import util.expect
import util.input

fun main() {
    expect(0, 0) {
        var inGarbage = false
        var ignoreNext = false

        var score = 0
        input.first().forEach {
            when {
                !inGarbage -> {
                    when (it) {
                        '<' -> {
                            inGarbage = true
                        }

                        '{' -> {
                            score++
                        }

                        '}' -> {
                            part1Result += score
                            score--
                        }
                    }
                }

                ignoreNext -> ignoreNext = false

                else -> {
                    when (it) {
                        '!' -> {
                            ignoreNext = true
                        }

                        '>' -> {
                            inGarbage = false
                        }

                        else -> {
                            part2Result++
                        }
                    }

                }
            }
        }
    }
}