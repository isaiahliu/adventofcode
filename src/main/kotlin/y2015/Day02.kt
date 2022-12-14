package y2015

import input

fun main() {
    var part1Sum = 0
    var part2Sum = 0
    input.map { it.split("x").map { it.toInt() }.sorted() }.filter { it.size == 3 }.forEach { (a, b, c) ->
        part1Sum += 2 * a * b
        part1Sum += 2 * a * c
        part1Sum += 2 * b * c
        part1Sum += a * b

        part2Sum += a * 2
        part2Sum += b * 2
        part2Sum += a * b * c
    }

    println(part1Sum)
    println(part2Sum)
}
