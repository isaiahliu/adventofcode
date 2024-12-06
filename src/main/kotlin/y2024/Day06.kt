package y2024

import util.expect
import util.input
import java.util.*

fun main() {
    expect(0, 0) {
        val validObstacles = hashSetOf<Pair<Int, Int>>()
        val visited = hashSetOf<Pair<Int, Int>>()
        val routes = Array(4) { hashSetOf<Pair<Int, Int>>() }

        data class State(val r: Int, val c: Int, val direction: Int, val obstacle: Pair<Int, Int>?) {
            private var _visited: Boolean = false

            val visited: Boolean
                get() = if (_visited) {
                    true
                } else {
                    _visited = true
                    false
                }

            val pos = r to c

            fun forward(): State {
                var newR = r
                var newC = c
                when (direction) {
                    0 -> newR--
                    1 -> newC++
                    2 -> newR++
                    3 -> newC--
                }

                return copy(r = newR, c = newC)
            }

            fun turnRight(): State {
                return copy(direction = (direction + 1) % 4)
            }

            val node: Char? = input.getOrNull(r)?.getOrNull(c)
        }

        val stack = LinkedList<State>()

        run {
            val r = input.indices.first { '^' in input[it] }
            val c = input[r].indices.first { input[r][it] == '^' }
            stack.push(State(r, c, 0, null))
        }

        while (stack.isNotEmpty()) {
            val state = stack.peek()

            if (state.obstacle == null) {
                visited += state.pos
            }

            val forward = state.forward()

            when {
                state.visited -> {
                    routes[state.direction] -= state.pos
                    stack.pop()
                }

                !routes[state.direction].add(state.pos) -> {
                    state.obstacle?.also { validObstacles += it }
                }

                forward.node == null -> {
                    part1Result = visited.size
                }

                forward.node == '#' || forward.pos == state.obstacle -> {
                    stack.push(state.turnRight())
                }

                else -> {
                    if (state.obstacle == null && forward.node == '.' && routes.none { forward.pos in it }) {
                        stack.push(state.turnRight().copy(obstacle = forward.pos))
                    }
                    stack.push(forward)
                }
            }
        }

        part2Result = validObstacles.size
    }
}
