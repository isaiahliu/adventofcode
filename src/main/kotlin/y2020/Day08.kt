package y2020

import util.input

fun main() {
    val instructions = input.map { it.split(" ") }.map { it[0] to it[1].toInt() }

    fun process(changeIndex: Int = -1): Pair<Int, Boolean> {
        var instructionIndex = 0

        var accumulator = 0

        val executed = hashSetOf<Int>()

        var indexOfJmpOrNop = 0
        val jmpOrNop = arrayOf("jmp", "nop")
        while (instructionIndex < instructions.size) {
            if (!executed.add(instructionIndex)) {
                break
            }

            var (instruction, param) = instructions[instructionIndex]

            if (instruction in jmpOrNop) {
                if (indexOfJmpOrNop == changeIndex) {
                    instruction = jmpOrNop[1 - jmpOrNop.indexOf(instruction)]
                }

                indexOfJmpOrNop++
            }

            when (instruction) {
                "acc" -> {
                    accumulator += param

                    instructionIndex++
                }

                "jmp" -> {
                    instructionIndex += param
                }

                "nop" -> {
                    instructionIndex++
                }

                else -> throw RuntimeException(instruction)
            }
        }

        return accumulator to (instructionIndex >= instructions.size)
    }

    println(process(-1).first)

    var changeIndex = 0
    while (true) {
        val (result, success) = process(changeIndex++)

        if (success) {
            println(result)
            break
        }
    }
}

