package y2017

import util.input

fun main() {
    val line = input.first()

    val part1Result = line.filterIndexed { index, value -> value == line[(index + 1) % line.length] }.sumOf { it - '0' }
    println(part1Result)

    val part2Result =
        line.filterIndexed { index, value -> value == line[(index + line.length / 2) % line.length] }.sumOf { it - '0' }
    println(part2Result)
}