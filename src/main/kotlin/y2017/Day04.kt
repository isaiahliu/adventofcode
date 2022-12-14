package y2017

import util.input

fun main() {
    val part1Result = input.map { it.split(" ") }.count {
        it.size == it.distinct().size
    }

    println(part1Result)

    val part2Result = input.map { it.split(" ").map { String(it.toCharArray().sorted().toCharArray()) } }.count {
        it.size == it.distinct().size
    }

    println(part2Result)
}