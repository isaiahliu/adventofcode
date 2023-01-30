package y2021

import util.input

fun main() {
    val fishes = LongArray(9)
    input.first().split(",").forEach {
        fishes[it.toInt()]++
    }

    var currentIndex = 0
    var result1 = 0L

    repeat(256) {
        fishes[(currentIndex + 7) % 9] += fishes[currentIndex]

        currentIndex++
        currentIndex %= 9

        if (it == 80 - 1) {
            result1 = fishes.sum()
        }
    }

    println(result1)
    println(fishes.sum())
}