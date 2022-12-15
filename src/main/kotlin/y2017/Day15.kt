package y2017

import util.input

fun main() {
    val division = 0x7fffffff
    val factorA = 16807L
    val factorB = 48271L
    val judge = 0xffffL

    val (a, b) = input.map { it.split(" ").last().toLong() }

    fun process(predicateA: (Long) -> Boolean, predicateB: (Long) -> Boolean, times: Int): Int {
        var producedA = a
        var producedB = b
        var judgeCount = 0
        repeat(times) {
            do {
                producedA = producedA * factorA % division
            } while (!predicateA(producedA))

            do {
                producedB = producedB * factorB % division
            } while (!predicateB(producedB))

            if (producedA.and(judge) == producedB.and(judge)) {
                judgeCount++
            }
        }

        return judgeCount
    }

    println(process({ true }, { true }, 40_000_000))
    println(process({ it % 4 == 0L }, { it % 8 == 0L }, 5_000_000))
}