package y2016

import util.expect
import util.input

fun main() {
    expect(0, "") {
        val width = 50
        val height = 6

        val screen = Array(height) { CharArray(width) { ' ' } }


        input.map { it.split(" ") }.forEach { nodes ->
            when (nodes[0]) {
                "rect" -> {
                    val (x, y) = nodes[1].split("x")
                    repeat(x.toInt()) { column ->
                        repeat(y.toInt()) { row ->
                            screen[row][column] = '#'
                        }
                    }
                }

                else -> {
                    val index = nodes[2].split("=")[1].toInt()
                    val times = nodes[4].toInt()

                    repeat(times) {
                        when (nodes[1]) {
                            "row" -> {
                                val t = screen[index][width - 1]

                                repeat(width - 1) {
                                    screen[index][width - it - 1] = screen[index][width - it - 2]
                                }

                                screen[index][0] = t
                            }

                            "column" -> {
                                val t = screen[height - 1][index]

                                repeat(height - 1) {
                                    screen[height - it - 1][index] = screen[height - it - 2][index]
                                }

                                screen[0][index] = t
                            }
                        }
                    }
                }
            }
        }

        part1Result = screen.sumOf { it.count { it == '#' } }
        part2Result = screen.joinToString("\n") { it.concatToString() }
    }
}
