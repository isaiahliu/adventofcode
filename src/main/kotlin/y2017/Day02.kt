package y2017

import util.input

fun main() {
    val part1Result = input.sumOf {
        it.split("\\s+".toRegex()).map { it.toInt() }.let {
            it.max() - it.min()
        }
    }

    println(part1Result)

    val part2Result = input.sumOf {
        it.split("\\s+".toRegex()).map { it.toInt() }.sortedDescending().let {
            it.mapIndexed { index, a ->
                it.drop(index + 1).filter { a % it == 0 }.sumOf { a / it }
            }.sum()
        }
    }

    println(part2Result)
}