package y2022

import input

fun main() {
    var x = 1
    var cycle = 0

    var instructionIndex = 0

    val instructions = input.map { it.split(" ") }.map {
        when (it[0]) {
            "addx" -> AddX(it[1].toInt())
            else -> Noop()
        }
    }

    val builder = StringBuilder()

    var signalStrength = 0
    while (instructionIndex < instructions.size) {
        builder.append(if ((cycle % 40) in (x - 1)..(x + 1)) {
            "#"
        } else {
            "."
        })

        cycle++

        if ((cycle + 20) % 40 == 0) {
            signalStrength += x * cycle
        }

        if (cycle % 40 == 0) {
            builder.appendLine()
        }

        val instruction = instructions[instructionIndex]

        x = instruction.process(x)

        if (instruction.done) {
            instructionIndex++
        }
    }

    println(signalStrength)
    println(builder.toString())
}

private abstract class AbstractInstruction(protected var remainingCycle: Int) {
    val done: Boolean
        get() {
            return remainingCycle == 0
        }

    abstract fun process(register: Int): Int
}

private class Noop : AbstractInstruction(1) {
    override fun process(register: Int): Int {
        remainingCycle--

        return register
    }
}

private class AddX(private val param: Int) : AbstractInstruction(2) {
    override fun process(register: Int): Int {
        remainingCycle--
        return register + if (remainingCycle == 0) {
            param
        } else {
            0
        }
    }
}