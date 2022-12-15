package y2017

import util.input

fun main() {
    val instructions = input.map { it.split(" ") }

    val program = Program(instructions)

    while (!program.terminated) {
        program.execute()
    }

    println(program.queue.last())

    val programA = Program(instructions, true)
    val programB = Program(instructions, true).apply {
        registers['p' - 'a'] = 1
    }

    programA.targetProgram = programB
    programB.targetProgram = programA

    while (!((programA.waitingTurn > 5 && programB.waitingTurn > 5) || programA.terminated && programB.terminated)) {
        programA.execute()
        programB.execute()
    }

    println(programA.queue.size)
}

private class Program(private val instructions: List<List<String>>, private val version2: Boolean = false) {
    val registers = LongArray(26)

    var terminated = false

    val queue = arrayListOf<Long>()

    var waitingTurn = 0

    var consumeIndex = 0

    var index = 0

    var targetProgram: Program = this

    val recoverHistory = hashSetOf<String>()

    fun execute() {
        val instruction = instructions.getOrNull(index) ?: run {
            terminated = true
            return
        }

        when (instruction[0]) {
            "snd" -> {
                targetProgram.queue.add(instruction[1].evaluate())
                index++
            }

            "set" -> {
                registers[instruction[1]] = instruction[2].evaluate()
                index++
            }

            "add" -> {
                registers[instruction[1]] += instruction[2].evaluate()
                index++
            }

            "mul" -> {
                registers[instruction[1]] *= instruction[2].evaluate()
                index++
            }

            "mod" -> {
                registers[instruction[1]] %= instruction[2].evaluate()
                index++
            }

            "rcv" -> {
                if (version2) {
                    if (consumeIndex < queue.size) {
                        registers[instruction[1]] = queue[consumeIndex++]
                        waitingTurn = 0
                        index++
                    } else {
                        waitingTurn++
                    }
                } else {
                    if (instruction[1].evaluate() != 0L) {
                        if (!recoverHistory.add("${targetProgram.queue.last()}_${index}_${registers.joinToString()}")) {
                            terminated = true
                        }
                    }
                    index++
                }
            }

            "jgz" -> {
                if (instruction[1].evaluate() > 0) {
                    index += instruction[2].evaluate().toInt()
                } else {
                    index++
                }
            }

            else -> {
                println("error")
            }

        }
    }

    fun String.evaluate(): Long {
        return toLongOrNull() ?: registers[this[0] - 'a']
    }

    operator fun LongArray.set(name: String, value: Long) {
        this[name[0] - 'a'] = value
    }

    operator fun LongArray.get(name: String): Long {
        return this[name[0] - 'a']
    }
}

