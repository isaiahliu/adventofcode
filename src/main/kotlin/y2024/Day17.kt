package y2024

import util.expect
import util.input

fun main() {
    expect("", 0L) {
        val instructions = arrayOf("adv", "bxl", "bst", "jnz", "bxc", "out", "bdv", "cdv")
        fun process(program: IntArray, registers: LongArray, jump: Boolean, sendOutput: (Long) -> Unit): LongArray {
            var instIndex = 0

            while (instIndex < program.size) {
                val inst = instructions[program[instIndex++]]
                val literalParam = program[instIndex++]
                val comboParam = literalParam.takeIf { it < 4 }?.toLong() ?: registers.getOrNull(literalParam - 4) ?: 0

                when (inst) {
                    "adv", "bdv", "cdv" -> {
                        var pow = comboParam

                        registers[inst[0] - 'a'] = registers[0]

                        while (pow-- > 0 && registers[inst[0] - 'a'] > 0) {
                            registers[inst[0] - 'a'] /= 2L
                        }

                    }

                    "bxl" -> {
                        registers[1] = registers[1] xor literalParam.toLong()
                    }

                    "bst" -> {
                        registers[1] = comboParam and 0b111
                    }

                    "jnz" -> {
                        if (registers[0] > 0 && jump) {
                            instIndex = literalParam
                        }
                    }

                    "bxc" -> {
                        registers[1] = registers[1] xor registers[2]
                    }

                    "out" -> {
                        sendOutput(comboParam and 0b111L)
                    }

                    else -> throw Exception("Error")
                }
            }

            return registers
        }

        val registerRegex = "Register (\\w): (\\d+)".toRegex()
        val programRegex = "Program: (.*)".toRegex()
        val registers = LongArray(3)

        input.forEach {
            registerRegex.matchEntire(it)?.groupValues?.drop(1)?.also { (index, value) ->
                registers[index[0] - 'A'] = value.toLong()
            } ?: programRegex.matchEntire(it)?.groupValues?.getOrNull(1)?.split(",")?.map { it.toInt() }?.toIntArray()?.also { program ->
                part1Result = buildString {
                    process(program, registers, true) {
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
                        val result = process(program, longArrayOf((base shl 3) + a, 0, 0), false) {}

                        if (result[1] and 0b111L == target.toLong()) {
                            val newBase = (base shl 3) + a

                            var matchIndex = index
                            var success = true
                            process(program, longArrayOf(newBase, 0, 0), true) {
                                if (success && it.toInt() != program[matchIndex++]) {
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