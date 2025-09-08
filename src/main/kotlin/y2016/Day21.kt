package y2016

import util.expect
import util.input
import kotlin.math.sign

fun main() {
    expect("", "") {
        abstract class AbstractInstruction {
            abstract fun scramble(input: String): String
            open fun unscramble(input: String): String {
                return scramble(input)
            }
        }

        class SwapPosition(val index1: Int, val index2: Int) : AbstractInstruction() {
            override fun scramble(input: String): String {
                return buildString {
                    input.indices.forEach {
                        when (it) {
                            index1 -> append(input[index2])
                            index2 -> append(input[index1])
                            else -> append(input[it])
                        }
                    }
                }
            }
        }

        class SwapCharacter(val char1: Char, val char2: Char) : AbstractInstruction() {
            override fun scramble(input: String): String {
                return buildString {
                    input.forEach {
                        when (it) {
                            char1 -> append(char2)
                            char2 -> append(char1)
                            else -> append(it)
                        }
                    }
                }
            }
        }

        class Rotate(val offset: Int) : AbstractInstruction() {
            private fun offset(input: String, count: Int): String {
                return buildString {
                    input.indices.forEach {
                        append(input[(it - count).mod(input.length)])
                    }
                }
            }

            override fun scramble(input: String): String {
                return offset(input, offset)
            }

            override fun unscramble(input: String): String {
                return offset(input, -offset)
            }
        }

        class Reverse(val index1: Int, val index2: Int) : AbstractInstruction() {
            override fun scramble(input: String): String {
                return buildString {
                    input.indices.forEach {
                        if (it in index1..index2) {
                            append(input[index2 + index1 - it])
                        } else {
                            append(input[it])
                        }
                    }
                }
            }
        }

        class RotateOnCharacter(val char: Char) : AbstractInstruction() {
            private fun offset(input: String, count: Int): String {
                return buildString {
                    input.indices.forEach {
                        append(input[(it - count).mod(input.length)])
                    }
                }
            }

            override fun scramble(input: String): String {
                var offset = input.indexOf(char)
                offset += (offset / 4).sign
                offset++

                return offset(input, offset)
            }

            override fun unscramble(input: String): String {
                val currentIndex = input.indexOf(char)
                var prevIndex = currentIndex
                while (prevIndex % 2 == 0 && prevIndex <= input.length) {
                    prevIndex += input.length
                }
                prevIndex--
                prevIndex /= 2

                return offset(input, prevIndex - currentIndex)
            }
        }

        class Move(val index1: Int, val index2: Int) : AbstractInstruction() {
            private fun move(input: String, index1: Int, index2: Int): String {
                return buildString {
                    if (index1 < index2) {
                        repeat(index1) {
                            append(input[it])
                        }
                        repeat(index2 - index1) {
                            append(input[index1 + it + 1])
                        }
                        append(input[index1])
                        repeat(input.length - index2 - 1) {
                            append(input[index2 + it + 1])
                        }
                    } else {
                        repeat(index2) {
                            append(input[it])
                        }
                        append(input[index1])
                        repeat(index1 - index2) {
                            append(input[index2 + it])
                        }
                        repeat(input.length - index1 - 1) {
                            append(input[index1 + it + 1])
                        }
                    }
                }
            }

            override fun scramble(input: String): String {
                return move(input, index1, index2)
            }

            override fun unscramble(input: String): String {
                return move(input, index2, index1)
            }
        }

        val instructions = arrayListOf<AbstractInstruction>()

        input.map { it.split(" ") }.forEach {
            when {
                it[0] == "swap" && it[1] == "position" -> {
                    instructions += SwapPosition(it[2].toInt(), it[5].toInt())
                }

                it[0] == "swap" && it[1] == "letter" -> {
                    instructions += SwapCharacter(it[2][0], it[5][0])
                }

                it[0] == "rotate" && it[1] == "left" -> {
                    instructions += Rotate(it[2].toInt() * -1)
                }

                it[0] == "rotate" && it[1] == "right" -> {
                    instructions += Rotate(it[2].toInt())
                }

                it[0] == "rotate" && it[1] == "based" -> {
                    instructions += RotateOnCharacter(it[6][0])
                }

                it[0] == "reverse" -> {
                    instructions += Reverse(it[2].toInt(), it[4].toInt())
                }

                it[0] == "move" -> {
                    instructions += Move(it[2].toInt(), it[5].toInt())
                }
            }
        }
        part1Result = instructions.fold("abcdefgh") { input, instruction ->
            instruction.scramble(input)
        }

        part2Result = instructions.reversed().fold("fbgdceah") { input, instruction ->
            instruction.unscramble(input)
        }
    }
}