package y2017

import util.input

fun main() {
    val scanningCycles = hashMapOf<Int, Int>()
    input.map { it.split(" ") }.forEach {
        scanningCycles[it[0].trimEnd(':').toInt()] = (it[1].toInt() - 1) * 2
    }

    var result1 = 0

    repeat(scanningCycles.keys.max() + 1) { step ->
        scanningCycles[step]?.also { areaCycle ->
            if (step % areaCycle == 0) {
                result1 += step * (areaCycle / 2 + 1)
            }
        }
    }

    println(result1)

    var result2 = 0
    while (true) {
        if ((0..scanningCycles.keys.max()).none { step ->
                    scanningCycles[step]?.let { areaCycle ->
                        ((step + result2) % areaCycle == 0)
                    } == true
                }) {
            break
        }

        result2++
    }

    println(result2)
}