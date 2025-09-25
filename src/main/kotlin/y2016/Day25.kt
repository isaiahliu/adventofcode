package y2016

import util.expect
import util.input

fun main() {
    expect(-1, "Merry Christmas!") {
        val instructions = input.map { it.split(" ") }

        var tiktokCount = 0
        while (tiktokCount < 100) {
            tiktokCount = 0

            val registers = arrayOf(++part1Result, 0, 0, 0)

            fun String.evaluate(): Int {
                return this.toIntOrNull() ?: registers[this[0] - 'a']
            }

            var instructionIndex = 0
            while (tiktokCount < 100) {
                val instruction = instructions[instructionIndex]
                var instructionOffset = 1

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
                            instructionOffset = instruction[2].evaluate()
                        }
                    }

                    "out" if instruction[1].evaluate() == 0 && tiktokCount % 2 == 0 -> {
                        tiktokCount++
                    }

                    "out" if instruction[1].evaluate() == 1 && tiktokCount % 2 == 1 -> {
                        tiktokCount++
                    }

                    else -> {
                        break
                    }
                }

                instructionIndex += instructionOffset
            }
        }
    }
}