package y2020

import util.input

fun main() {
    var result1 = 0
    var result2 = 0
    val answers1 = hashSetOf<Char>()
    var answers2: Set<Char>? = null
    (input + "").forEach {
        if (it.isBlank()) {
            result1 += answers1.size
            result2 += answers2?.size ?: 0
            answers1.clear()
            answers2 = null
        } else {
            val ans = it.toCharArray().toSet()

            answers1 += ans

            answers2 = answers2?.intersect(ans) ?: ans
        }
    }

    println(result1)
    println(result2)
}