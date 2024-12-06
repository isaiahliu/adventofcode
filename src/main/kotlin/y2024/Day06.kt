package y2024

import util.expect
import util.input
import java.util.*

fun main() {
    expect(0, 0) {
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
                return when (direction) {
                    0 -> copy(r = r - 1)
                    1 -> copy(c = c + 1)
                    2 -> copy(r = r + 1)
                    else -> copy(c = c - 1)
                }
            }

            fun turnRight(): State {
                return copy(direction = (direction + 1) % 4)
            }

            val node: Char? = input.getOrNull(r)?.getOrNull(c)
        }

        val validObstacles = hashSetOf<Pair<Int, Int>>()
        val visited = hashSetOf<Pair<Int, Int>>()
        val routes = Array(4) { hashSetOf<Pair<Int, Int>>() }

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
                    stack.pop()
                }

                forward.node == null -> {
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

        part1Result = visited.size
        part2Result = validObstacles.size
    }
}
