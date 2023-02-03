package y2016

import util.input

fun main() {
    var instructionIndex = 0

    val registers = longArrayOf(12, 0, 0, 0)

    fun String.evaluate(): Long {
        return this.toLongOrNull() ?: registers[this[0] - 'a']
    }

    val instructions = input.map { it.split(" ").toTypedArray() }
    while (instructionIndex < input.size) {
        val instruction = instructions[instructionIndex]

        when (instruction[0]) {
            "cpy" -> {
                registers[instruction[2][0] - 'a'] = instruction[1].evaluate()
                instructionIndex++
            }

            "inc" -> {
                registers[instruction[1][0] - 'a']++
                instructionIndex++
            }

            "dec" -> {
                registers[instruction[1][0] - 'a']--
                instructionIndex++
            }

            "jnz" -> {
                if (instruction[1].evaluate() != 0L) {
                    instructionIndex += instruction[2].evaluate().toInt()
                } else {
                    instructionIndex++
                }
            }

            "tgl" -> {
                instructions.getOrNull(instructionIndex + instruction[1].evaluate().toInt())
                    ?.also { toggleInstruction ->
                        when (toggleInstruction.size) {
                            2 -> {
                                toggleInstruction[0] = when (toggleInstruction[0]) {
                                    "inc" -> "dec"
                                    else -> "inc"
                                }
                            }

                            3 -> {
                                toggleInstruction[0] = when (toggleInstruction[0]) {
                                    "jnz" -> {
                                        if (toggleInstruction[2].toIntOrNull() == null) {
                                            "cpy"
                                        } else {
                                            toggleInstruction[0]
                                        }
                                    }

                                    else -> {
                                        "jnz"
                                    }
                                }
                            }
                        }
                    }

                instructionIndex++
            }

            else -> {
                println("nothing")
                instructionIndex++
            }
        }
    }

    println(registers[0])
}