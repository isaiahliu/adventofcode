package y2016

import input

fun main() {
    val count = input.first().toInt()

    var elves = Array(count) {
        (it + 1) % count
    }

    var current = 0

    while (elves[current] != current) {
        elves[current] = elves[elves[current]]

        current = elves[current]
    }

    println(current + 1)

    elves = Array(count) {
        (it + 1) % count
    }

    current = count / 2 - 1

    if (count % 2 == 1) {
        elves[current] = elves[elves[current]]
        current += 2
    }

    while (elves[elves[current]] != current) {
        elves[current] = elves[elves[elves[current]]]

        current = elves[current]
    }

    println(current + 1)
}
