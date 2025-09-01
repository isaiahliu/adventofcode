package y2016

import util.expect
import util.input

fun main() {
    expect(0, 0) {
        val program = input.map { it.split(" ") }.toTypedArray()

        fun process(registers: IntArray) {
            var instructionIndex = 0

            fun String.evaluate(): Int {
                return toIntOrNull() ?: registers[this[0] - 'a']
            }

            while (instructionIndex < program.size) {
                val instructions = program[instructionIndex]

                var increment = 1
                when (instructions[0]) {
                    "cpy" -> {
                        registers[instructions[2][0] - 'a'] = instructions[1].evaluate()
                    }

                    "inc" -> {
                        registers[instructions[1][0] - 'a']++
                    }

                    "dec" -> {
                        registers[instructions[1][0] - 'a']--
                    }

                    "jnz" -> {
                        if (instructions[1].evaluate() != 0) {
                            increment = instructions[2].evaluate()
                        }
                    }
                }

                instructionIndex += increment
            }
        }

        part1Result = intArrayOf(0, 0, 0, 0).also { process(it) }[0]
        part2Result = intArrayOf(0, 0, 1, 0).also { process(it) }[0]
    }
}