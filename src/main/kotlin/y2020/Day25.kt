package y2020

import util.input

fun main() {
    val pks = input.take(2).map { it.toInt() }

    fun publicKey(subjectKey: Int, loopSize: Int): Int {
        var t = 1L
        repeat(loopSize) {
            t = (t * subjectKey) % 20201227
        }

        return t.toInt()
    }

    val loopSizes = arrayListOf(7)
    val remainingPks = pks.toMutableList()

    var t = 1
    var loopSize = 1
    while (remainingPks.isNotEmpty()) {
        t = (t * 7) % 20201227

        if (t in remainingPks) {
            loopSizes += loopSize
            remainingPks -= t
        }

        loopSize++
    }

    println(loopSizes.reduce { acc, i ->
        publicKey(acc, i)
    })
}