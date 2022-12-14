package y2015

import util.input

fun main() {
    val input = input.first()

    val steps = input.toCharArray().map { if (it == '(') 1 else -1 }

    println(steps.sum())

    var sum = 0
    for ((index, i) in steps.withIndex()) {
        sum += i

        if (sum < 0) {
            println(index + 1)
            break
        }
    }
}
