package y2017

import util.input

fun main() {
    val instructions = input.map { it.split(" ") }

    fun Int.minFactor(): Int? {
        return (2..this / 2).firstOrNull { this % it == 0 }
    }

    fun process(registers: IntArray, debugMode: Boolean): Int {
        var instructionIndex = 0

        fun String.evaluate(): Int {
            return this.toIntOrNull() ?: registers[this[0] - 'a']
        }

        var mulTimes = 0
        while (instructionIndex < instructions.size) {
            val instruction = instructions[instructionIndex]

            val cacheKey = "${instructionIndex}:${instruction.joinToString(" ")}"

            when (instruction[0]) {
                "set" -> {
                    registers[instruction[1][0] - 'a'] = instruction[2].evaluate()
                    instructionIndex++
                }

                "sub" -> {
                    registers[instruction[1][0] - 'a'] -= instruction[2].evaluate()
                    instructionIndex++
                }

                "mul" -> {
                    registers[instruction[1][0] - 'a'] *= instruction[2].evaluate()
                    instructionIndex++

                    mulTimes++
                }

                "jnz" -> {
                    if (instruction[1].evaluate() != 0) {
                        instructionIndex += instruction[2].evaluate()
                    } else {
                        instructionIndex++
                    }
                }
            }

            if (debugMode) {
                when (instructionIndex - 1) {
                    10 -> {
                        val b = registers['b' - 'a']
                        val f = registers['f' - 'a']

                        val minFactor = b.minFactor()

                        if (f > 0 && minFactor != null) {
                            registers['d' - 'a'] = minFactor
                            registers['e' - 'a'] = b / minFactor
                        } else {
                            registers['d' - 'a'] = b - 1
                            registers['e' - 'a'] = b - 1
                        }
                    }
                }
            }

//            println("${registers.joinToString { it.toString() }}       --  ${cacheKey}")
        }

        return if (debugMode) {
            registers['h' - 'a']
        } else {
            mulTimes
        }
    }

    println(process(IntArray(8), false))
    println(process(IntArray(8).also { it[0] = 1 }, true))
}