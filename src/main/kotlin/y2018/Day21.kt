package y2018

import util.input

fun main() {
    var instPointer: Int
    val instructions = input.map { it.split(" ") }.map { it[0] to it.drop(1).map { it.toInt() } }.also {
        it.first().also { (_, args) ->
            instPointer = args[0]
        }
    }.drop(1)

    fun process(fastest: Boolean): Int {
        val registers = IntArray(6)

        val cache = arrayListOf<Int>()
        while (registers[instPointer] < instructions.size) {
            val instructionIndex = registers[instPointer]
            val (inst, args) = instructions[instructionIndex]

            when (instructionIndex) {
                28 -> {
                    if (fastest) {
                        registers[0] = registers[1]
                    } else if (registers[1] in cache) {
                        return cache.last()
                    } else {
                        cache += registers[1]
                    }
                }
            }

            when (inst) {
                "seti" -> {
                    registers[args[2]] = args[0]
                }

                "setr" -> {
                    registers[args[2]] = registers[args[0]]
                }

                "addi" -> {
                    registers[args[2]] = registers[args[0]] + args[1]
                }

                "addr" -> {
                    registers[args[2]] = registers[args[0]] + registers[args[1]]
                }


                "muli" -> {
                    registers[args[2]] = registers[args[0]] * args[1]
                }

                "mulr" -> {
                    registers[args[2]] = registers[args[0]] * registers[args[1]]
                }

                "bani" -> {
                    registers[args[2]] = registers[args[0]] and args[1]
                }

                "banr" -> {
                    registers[args[2]] = registers[args[0]] and registers[args[1]]
                }

                "bori" -> {
                    registers[args[2]] = registers[args[0]] or args[1]
                }

                "borr" -> {
                    registers[args[2]] = registers[args[0]] or registers[args[1]]
                }

                "eqrr" -> {
                    registers[args[2]] = if (registers[args[0]] == registers[args[1]]) 1 else 0
                }

                "eqri" -> {
                    registers[args[2]] = if (registers[args[0]] == args[1]) 1 else 0
                }

                "eqir" -> {
                    registers[args[2]] = if (args[0] == registers[args[1]]) 1 else 0
                }

                "gtrr" -> {
                    registers[args[2]] = if (registers[args[0]] > registers[args[1]]) 1 else 0
                }

                "gtir" -> {
                    registers[args[2]] = if (args[0] > registers[args[1]]) 1 else 0
                }

                "gtri" -> {
                    registers[args[2]] = if (registers[args[0]] > args[1]) 1 else 0
                }

                else -> {
                    println("error ${inst}")
                }
            }

            when (instructionIndex) {
                20 -> {
                    var t = registers[2] / 256

                    while (t * 256 <= registers[2]) {
                        t++
                    }

                    registers[4] = t - 1
                    registers[3] = 1
                }
            }

//            println("${instructionIndex}: ${inst} ${args.joinToString(" ")} -- ${registers.joinToString { it.toString() }}")

            registers[instPointer]++
        }
        return registers[0]
    }
    println(process(true))
    println(process(false))
}