package y2023

import util.input
import java.util.*

fun main() {
    val UP = 0
    val LEFT = 1
    val DOWN = 2
    val RIGHT = 3

    data class State(val r: Int, val c: Int, val score: Int, val straightCount: Int, val direction: Int)

    fun process(maxStraightSteps: Int, moveStep: Int): Int {
        val visited = hashMapOf<Pair<Int, Int>, Array<TreeMap<Int, Int>>>()

        val queue = PriorityQueue<State>(compareBy { it.score })
        queue.add(State(0, 0, 0, 0, RIGHT))

        while (queue.isNotEmpty()) {
            val state = queue.poll()

            if (state.r == input.lastIndex && state.c == input[0].lastIndex) {
                return state.score
            }

            val visitedScores = visited.computeIfAbsent(state.r to state.c) { Array(4) { TreeMap() } }[state.direction]

            if (visitedScores.floorKey(state.straightCount) == null) {
                visitedScores[state.straightCount] = state.score

                val nextDirections = hashSetOf<Pair<Int, Int>>()
                nextDirections += (state.direction - 1).mod(4) to moveStep
                nextDirections += (state.direction + 1).mod(4) to moveStep

                if (state.straightCount < maxStraightSteps) {
                    nextDirections += state.direction to 1
                }

                nextDirections.forEach { (nextDirection, step) ->
                    var r = state.r
                    var c = state.c
                    var newScore = state.score
                    repeat(step) {
                        when (nextDirection) {
                            UP -> r--
                            LEFT -> c--
                            DOWN -> r++
                            RIGHT -> c++
                        }

                        input.getOrNull(r)?.getOrNull(c)?.let { it - '0' }?.also {
                            newScore += it
                        } ?: return@forEach
                    }

                    queue.offer(
                        State(
                            r,
                            c,
                            newScore,
                            step + if (state.direction == nextDirection) state.straightCount else 0,
                            nextDirection
                        )
                    )
                }
            }
        }

        return 0
    }

    val part1Result = process(3, 1)
    val part2Result = process(10, 4)

    println(part1Result)
    println(part2Result)
}
