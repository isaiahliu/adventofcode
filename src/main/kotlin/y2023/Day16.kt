package y2023

import util.input
import java.util.*

fun main() {
    val UP = 0
    val RIGHT = 1
    val DOWN = 2
    val LEFT = 3

    fun Pair<Int, Int>.next(direction: Int): Pair<Int, Int> {
        var (r, c) = this
        when (direction) {
            UP -> r--
            RIGHT -> c++
            DOWN -> r++
            LEFT -> c--
        }

        return r to c
    }

    fun process(init: Pair<Pair<Int, Int>, Int>): Int {
        val visited = hashSetOf<Pair<Pair<Int, Int>, Int>>()

        val current = LinkedList<Pair<Pair<Int, Int>, Int>>()
        current.add(init)

        while (current.isNotEmpty()) {
            val (p, direction) = current.poll()

            val (r, c) = p
            val type = input.getOrNull(r)?.getOrNull(c) ?: continue
            if (visited.add(p to direction)) {
                val nextDirections = hashSetOf<Int>()
                when {
                    type == '/' -> {
                        nextDirections += direction xor 1
                    }

                    type == '\\' -> {
                        nextDirections += direction xor 0b11
                    }

                    type == '|' && direction % 2 == 1 || type == '-' && direction % 2 == 0 -> {
                        nextDirections += (direction + 1) % 4
                        nextDirections += (direction - 1).mod(4)
                    }

                    else -> {
                        nextDirections += direction
                    }
                }

                nextDirections.forEach {
                    current.add(p.next(it) to it)
                }
            }
        }

        val visitedNodes = visited.map { it.first }.toSet()

        return visitedNodes.size
    }

    val part1Result = process(0 to 0 to RIGHT)

    val part2Result = listOf(
        input.indices.map { it to 0 to RIGHT },
        input[0].indices.map { 0 to it to DOWN },
        input.indices.map { it to input[0].lastIndex to LEFT },
        input[0].indices.map { input.lastIndex to it to UP },
    ).flatten().maxOf {
        process(it)
    }

    println(part1Result)
    println(part2Result)
}
