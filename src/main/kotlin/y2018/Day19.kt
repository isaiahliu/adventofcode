package y2018

import util.input

fun main() {

    var instPointer: Int
    val instructions = input.map { it.split(" ") }.map { it[0] to it.drop(1).map { it.toInt() } }.also {
        it.first().also { (_, args) ->
            instPointer = args[0]
        }
    }.drop(1)

    fun process(initValue: Int, debugMode: Boolean): Int {
        val registers = IntArray(6)
        registers[0] = initValue


        while (registers[instPointer] < instructions.size) {
            val instructionIndex = registers[instPointer]
            val (inst, args) = instructions[instructionIndex]

            if (debugMode) {
                when (instructionIndex) {
                    3 -> {
                        registers[0] += (1..registers[3]).filter { registers[3] % it == 0 }.sum()
                        registers[2] = registers[3] + 1
                        registers[5] = registers[3] + 1
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

                "mulr" -> {
                    registers[args[2]] = registers[args[0]] * registers[args[1]]
                }

                "muli" -> {
                    registers[args[2]] = registers[args[0]] * args[1]
                }

                "eqrr" -> {
                    registers[args[2]] = if (registers[args[0]] == registers[args[1]]) 1 else 0
                }

                "gtrr" -> {
                    registers[args[2]] = if (registers[args[0]] > registers[args[1]]) 1 else 0
                }

                else -> {
                    println("error ${inst}")
                }
            }

//            println("${instructionIndex}: ${inst} ${args.joinToString(" ")} -- ${registers.joinToString { it.toString() }}")

            registers[instPointer]++
        }
        return registers[0]
    }
    println(process(0, false))
    println(process(1, true))
}
