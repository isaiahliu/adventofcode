package y2016

import util.input

fun main() {
    val instructions = input.map { it.split(" ") }

    var initValue = -1
    var found = false
    while (!found) {
        var instructionIndex = 0

        val registers = arrayOf(++initValue, 0, 0, 0)

        fun String.evaluate(): Int {
            return this.toIntOrNull() ?: registers[this[0] - 'a']
        }

        var currentTiktok = true
        var tiktokCount = 0

        while (instructionIndex < instructions.size) {
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
                    if (instruction[1].evaluate() != 0) {
                        instructionIndex += instruction[2].evaluate()
                    } else {
                        instructionIndex++
                    }
                }

                "out" -> {
                    when (instruction[1].evaluate()) {
                        0 -> {
                            if (currentTiktok) {
                                currentTiktok = false
                                instructionIndex++

                                if (++tiktokCount == 100) {
                                    found = true
                                    break
                                } else {
                                    continue
                                }
                            } else {
                                break
                            }
                        }

                        1 -> {
                            if (!currentTiktok) {
                                currentTiktok = true
                                instructionIndex++

                                if (++tiktokCount == 1000) {
                                    found = true
                                    break
                                } else {
                                    continue
                                }
                            } else {
                                break
                            }
                        }

                        else -> {
                            println("error - ${instruction[1].evaluate()}")
                            break
                        }
                    }
                }

                else -> {
                }
            }
        }
    }

    println(initValue)
}