package y2018

import util.input

fun main() {
    var index = 0

    val instructions = arrayOf("addr", "addi", "mulr", "muli", "banr", "bani", "borr", "bori", "setr", "seti", "gtir", "gtri", "gtrr", "eqir", "eqri", "eqrr")

    val map = hashMapOf<Int, MutableSet<String>>()

    var result1 = 0

    fun execute(instruction: String, a: Int, b: Int, c: Int, registers: IntArray) {
        when (instruction) {
            "addr" -> {
                registers[c] = registers[a] + registers[b]
            }

            "addi" -> {
                registers[c] = registers[a] + b
            }

            "mulr" -> {
                registers[c] = registers[a] * registers[b]
            }

            "muli" -> {
                registers[c] = registers[a] * b
            }

            "banr" -> {
                registers[c] = registers[a] and registers[b]
            }

            "bani" -> {
                registers[c] = registers[a] and b
            }

            "borr" -> {
                registers[c] = registers[a] or registers[b]
            }

            "bori" -> {
                registers[c] = registers[a] or b
            }

            "setr" -> {
                registers[c] = registers[a]
            }

            "seti" -> {
                registers[c] = a
            }

            "gtir" -> {
                registers[c] = if (a > registers[b]) 1 else 0
            }

            "gtri" -> {
                registers[c] = if (registers[a] > b) 1 else 0
            }

            "gtrr" -> {
                registers[c] = if (registers[a] > registers[b]) 1 else 0
            }

            "eqir" -> {
                registers[c] = if (a == registers[b]) 1 else 0
            }

            "eqri" -> {
                registers[c] = if (registers[a] == b) 1 else 0
            }

            "eqrr" -> {
                registers[c] = if (registers[a] == registers[b]) 1 else 0
            }
        }
    }

    while (true) {
        val beforeStr = input[index].trim()

        if (beforeStr.isEmpty()) {
            break
        }

        val commandStr = input[index + 1].trim()
        val afterStr = input[index + 2].trim()

        val beforeRegisters = beforeStr.split(" ").takeLast(4).map { it.trim(',', '[', ']').toInt() }.toIntArray()
        val afterRegisters = afterStr.split(" ").takeLast(4).map { it.trim(',', '[', ']').toInt() }.toIntArray()
        val (instCode, a, b, c) = commandStr.split(" ").map { it.toInt() }

        val possibleInstructions = hashSetOf<String>()

        instructions.forEach {
            val result = IntArray(beforeRegisters.size) { beforeRegisters[it] }

            execute(it, a, b, c, result)

            if (result.contentEquals(afterRegisters)) {
                possibleInstructions += it
            }
        }

        if (possibleInstructions.size >= 3) {
            result1++
        }

        if (map.containsKey(instCode)) {
            map[instCode] = map[instCode].orEmpty().intersect(possibleInstructions).toMutableSet()
        } else {
            map[instCode] = possibleInstructions
        }

        index += 4
    }

    println(result1)

    val instructionMap = hashMapOf<Int, String>()
    while (map.any { it.value.isNotEmpty() }) {
        val (code, inst) = map.entries.first { it.value.size == 1 }
        val t = inst.first()
        instructionMap[code] = t

        map.forEach { it.value.remove(t) }
    }

    val result2 = IntArray(4)
    input.drop(index + 1).filter { it.isNotEmpty() }.map { it.split(" ").map { it.toInt() } }.forEach { (code, a, b, c) ->
        execute(instructionMap[code]!!, a, b, c, result2)
    }

    println(result2[0])
}

