package y2017

import util.input

fun main() {
    val registers = hashMapOf<String, Int>()

    fun String.evaluate(): Int {
        return this.toIntOrNull() ?: registers.computeIfAbsent(this) { 0 }
    }

    var result2 = Int.MIN_VALUE
    input.map { it.split(" ") }.forEach {
        val (reg, incOp, incParam) = it
        val (opParam1, op, opParam2) = it.takeLast(3)

        val predicate = when (op) {
            ">" -> {
                opParam1.evaluate() > opParam2.evaluate()
            }

            "<" -> {
                opParam1.evaluate() < opParam2.evaluate()
            }

            ">=" -> {
                opParam1.evaluate() >= opParam2.evaluate()
            }

            "<=" -> {
                opParam1.evaluate() <= opParam2.evaluate()
            }

            "!=" -> {
                opParam1.evaluate() != opParam2.evaluate()
            }

            else -> {
                opParam1.evaluate() == opParam2.evaluate()
            }
        }

        if (predicate) {
            val inc = if (incOp == "inc") 1 else -1

            registers[reg] = (registers[reg] ?: 0) + inc * incParam.evaluate()

            result2 = result2.coerceAtLeast(registers[reg] ?: 0)
        }
    }

    println(registers.values.max())

    println(result2)
}