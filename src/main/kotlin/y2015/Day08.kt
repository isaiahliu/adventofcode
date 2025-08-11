package y2015

import util.expect
import util.input

fun main() {
    expect(0, 0) {
        val hex = setOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f')
        input.forEach {
            part1Result += 2
            part2Result += 2

            var index = 0
            while (index < it.length) {
                when (it[index++]) {
                    '"' -> {
                        part2Result++
                    }

                    '\\' -> {
                        part2Result++

                        when {
                            it.getOrNull(index) == '\\' || it.getOrNull(index) == '"' -> {
                                index++
                                part1Result += 1
                                part2Result++
                            }

                            it.getOrNull(index) == 'x' && it.getOrNull(index + 1) in hex && it.getOrNull(index + 2) in hex -> {
                                index += 3
                                part1Result += 3
                            }
                        }
                    }
                }
            }
        }
    }
}