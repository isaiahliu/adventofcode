package y2024

import util.expect
import util.input

fun main() {
    expect("", 0L) {
        val instructions = arrayOf("adv", "bxl", "bst", "jnz", "bxc", "out", "bdv", "cdv")

        val program = arrayListOf<Int>()
        fun LongArray.process(suppressJump: Boolean = false, sendOutput: (Int) -> Unit) {
            var instIndex = 0

            fun literalOperand(): Int {
                return program[instIndex + 1]
            }

            fun comboOperand(): Long {
                return literalOperand().let { it.takeIf { it < 4 }?.toLong() ?: this.getOrNull(it - 4) ?: throw Exception("Error") }
            }

            while (instIndex < program.size) {
                when (val inst = instructions[program[instIndex]]) {
                    "adv", "bdv", "cdv" -> {
                        var pow = comboOperand()

                        this[inst[0] - 'a'] = this[0]

                        while (pow-- > 0 && this[inst[0] - 'a'] > 0) {
                            this[inst[0] - 'a'] /= 2L
                        }

                    }

                    "bxl" -> {
                        this[1] = this[1] xor literalOperand().toLong()
                    }

                    "bst" -> {
                        this[1] = comboOperand() and 0b111
                    }

                    "jnz" -> {
                        if (this[0] > 0 && !suppressJump) {
                            instIndex = literalOperand() - 2
                        }
                    }

                    "bxc" -> {
                        this[1] = this[1] xor this[2]
                    }

                    "out" -> {
                        sendOutput((comboOperand() and 0b111).toInt())
                    }

                    else -> throw Exception("Error")
                }

                instIndex += 2
            }
        }

        val registerRegex = "Register (\\w): (\\d+)".toRegex()
        val programRegex = "Program: (.*)".toRegex()
        val registers = LongArray(3)

        input.forEach {
            registerRegex.matchEntire(it)?.groupValues?.drop(1)?.also { (index, value) ->
                registers[index[0] - 'A'] = value.toLong()
            } ?: programRegex.matchEntire(it)?.groupValues?.getOrNull(1)?.split(",")?.map { it.toInt() }?.also {
                program += it

                part1Result = buildString {
                    registers.process {
                        if (this.isNotEmpty()) {
                            append(",")
                        }

                        append(it)
                    }
                }

                fun dfs(index: Int, base: Long): Long? {
                    if (index < 0) {
                        return base
                    }

                    val target = program[index]

                    for (a in 0L..0b111L) {
                        var output = 0
                        longArrayOf((base shl 3) + a, 0, 0).process(true) {
                            output = it
                        }

                        if (output == target) {
                            val newBase = (base shl 3) + a

                            var matchIndex = index
                            var success = true

                            longArrayOf(newBase, 0, 0).process {
                                if (success && it != program[matchIndex++]) {
                                    success = false
                                }
                            }

                            if (success) {
                                dfs(index - 1, newBase)?.also {
                                    return it
                                }
                            }
                        }
                    }

                    return null
                }

                part2Result = dfs(program.lastIndex, 0L) ?: 0
            }
        }
    }
}