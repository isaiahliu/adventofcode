package y2016

import util.expect
import util.input

fun main() {
    expect(0, 0) {
        val toggleMap = hashMapOf(
            "inc" to "dec", "dec" to "inc", "tgl" to "inc", "jnz" to "cpy", "cpy" to "jnz"
        )

        fun process(initValue: Int): Int {
            var instructionIndex = 0
            val instructions = input.map { it.split(" ").toTypedArray() }
            val registers = intArrayOf(initValue, 0, 0, 0)

            fun String.evaluate(): Int {
                return this.toIntOrNull() ?: registers[this[0] - 'a']
            }

            while (instructionIndex < instructions.size) {
                val instruction = instructions[instructionIndex]

//            val sb = StringBuilder("${instructionIndex} ${instruction.joinToString(" ")}".padEnd(20, ' '))
                when (instruction[0]) {
                    "cpy" -> {
                        registers[instruction[2][0] - 'a'] = instruction[1].evaluate()
                        instructionIndex++
                    }

                    "inc" if instruction[1][0] == 'a' -> {
                        registers[0] += registers[2] * registers[3]
                        registers[2] = 1
                        registers[3] = 1
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
                            instructionIndex += instruction[2].evaluate().toInt()
                        } else {
                            instructionIndex++
                        }
                    }

                    "tgl" -> {
                        val targetInstructionIndex = instructionIndex + instruction[1].evaluate()
                        instructions.getOrNull(targetInstructionIndex.toInt())?.also {
                            it[0] = toggleMap[it[0]] ?: throw Exception("Error")
                        }

                        instructionIndex++
                    }
                }
            }

//            registers.forEachIndexed { index, value ->
//                sb.append("${'a' + index}:${value.toString().padEnd(8, ' ')}")
//            }
//            println(sb)
            return registers[0]
        }
        part1Result = process(7)
        part2Result = process(12)
    }
}