package y2016

import util.expect
import util.input

fun main() {
    expect(0, 0) {
        val toggleMap = hashMapOf(
            "inc" to "dec", "dec" to "inc", "tgl" to "inc", "jnz" to "cpy", "cpy" to "jnz"
        )

        val showLog = false
        fun process(initValue: Int): Int {
            var instructionIndex = 0
            val instructions = input.map { it.split(" ").toTypedArray() }
            val registers = intArrayOf(initValue, 0, 0, 0)

            fun String.evaluate(): Int {
                return this.toIntOrNull() ?: registers[this[0] - 'a']
            }

            while (instructionIndex < instructions.size) {
                val instruction = instructions[instructionIndex]

                var instructionOffset = 1

                when (instruction[0]) {
                    "cpy" -> {
                        registers[instruction[2][0] - 'a'] = instruction[1].evaluate()
                    }

                    "inc" if instruction[1][0] == 'a' -> {
                        registers[0] += registers[2] * registers[3]
                        registers[2] = 1
                        registers[3] = 1
                    }

                    "inc" -> {
                        registers[instruction[1][0] - 'a']++
                    }

                    "dec" -> {
                        registers[instruction[1][0] - 'a']--
                    }

                    "jnz" -> {
                        if (instruction[1].evaluate() != 0) {
                            instructionOffset = instruction[2].evaluate()
                        }
                    }

                    "tgl" -> {
                        val targetInstructionIndex = instructionIndex + instruction[1].evaluate()
                        instructions.getOrNull(targetInstructionIndex)?.also {
                            it[0] = toggleMap[it[0]] ?: throw Exception("Error")
                        }
                    }
                }

                if (showLog) {
                    buildString {
                        append(instructionIndex)
                        append(" ")
                        instruction.forEach {
                            append(it)
                            append(" ")
                        }
                        repeat(20 - length) {
                            append(" ")
                        }

                        registers.forEachIndexed { index, value ->
                            append('a' + index)
                            append(":")
                            append(value.toString().padEnd(10, ' '))
                        }
                    }.also { println(it) }
                }

                instructionIndex += instructionOffset
            }

            return registers[0]
        }
        part1Result = process(7)
        part2Result = process(12)
    }
}