package y2015

import util.expect
import util.input

fun main() {
    expect(0, 0) {
        val instructions = input.map { it.split(" ").toTypedArray() }.toTypedArray()

        fun execute(registers: IntArray) {
            var instIndex = 0
            while (instIndex < instructions.size) {
                val nodes = instructions[instIndex]

                var instMovement = 1
                when (nodes[0]) {
                    "hlf" -> {
                        registers[nodes[1][0] - 'a'] /= 2
                    }

                    "tpl" -> {
                        registers[nodes[1][0] - 'a'] *= 3
                    }

                    "inc" -> {
                        registers[nodes[1][0] - 'a']++
                    }

                    "jmp" -> {
                        instMovement = nodes[1].toInt()
                    }

                    "jie" -> {
                        if (registers[nodes[1][0] - 'a'] % 2 == 0) {
                            instMovement = nodes[2].toInt()
                        }
                    }

                    "jio" -> {
                        if (registers[nodes[1][0] - 'a'] == 1) {
                            instMovement = nodes[2].toInt()
                        }
                    }
                }

                instIndex += instMovement
            }
        }

        part1Result = intArrayOf(0, 0).also { execute(it) }[1]
        part2Result = intArrayOf(1, 0).also { execute(it) }[1]
    }
}
