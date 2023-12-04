package y2023

import util.input

fun main() {
    val scores = input.map {
        it.split(": ")[1].split('|').map { it.split(' ').mapNotNull { it.toIntOrNull() } }.let {
            it[0].intersect(it[1].toSet()).size
        }
    }
    val cards = IntArray(input.size) { 1 }

    val part1Result = scores.filter { it > 0 }.sumOf { 1 shl (it - 1) }

    scores.forEachIndexed { index, score ->
        (1..score).forEach {
            cards[index + it] += cards[index]
        }
    }

    val part2Result = cards.sum()

    println(part1Result)
    println(part2Result)
}
