package y2016

import util.input

fun main() {
    val instructions = input.map { it.split(" ") }.toTypedArray()

    fun process(registers: IntArray): Int {
        var index = 0

        fun String.evaluate(): Int {
            return toIntOrNull() ?: registers[this[0] - 'a']
        }
        while (index < instructions.size) {
            val instruction = instructions[index]

            var increment = 1
            when (instruction[0]) {
                "cpy" -> {
                    registers[instruction[2][0] - 'a'] = instruction[1].evaluate()
                }

                "inc" -> {
                    registers[instruction[1][0] - 'a']++
                }

                "dec" -> {
                    registers[instruction[1][0] - 'a']--
                }

                "jnz" -> {
                    if (instruction[1].evaluate() != 0) {
                        increment = instruction[2].evaluate()
                    }
                }
            }

            index += increment
        }

        return registers[0]
    }

    println(process(intArrayOf(0, 0, 0, 0)))
    println(process(intArrayOf(0, 0, 1, 0)))
}