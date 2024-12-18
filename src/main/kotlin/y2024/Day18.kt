package y2024

import util.expect
import util.input
import java.util.*

fun main() {
    val UPPER_BOUND = 70

    expect(0, "") {
        class Group {
            var innerParent: Group? = null
                private set

            var parent: Group
                set(value) {
                    innerParent = value
                }
                get() {
                    return innerParent?.parent?.also {
                        innerParent = it
                    } ?: this
                }

            fun join(target: Group) {
                val leftParent = parent
                val rightParent = target.parent

                if (leftParent != rightParent) {
                    leftParent.parent = rightParent
                }
            }
        }

        val top = Group()
        val bottom = Group()
        val right = Group()
        val left = Group()

        val corrupts = hashMapOf<Pair<Int, Int>, Group>()

        for (pos in input.map { it.split(",").let { it[0].toInt() to it[1].toInt() } }) {
            corrupts[pos] = Group().also {
                arrayOf(-1 to -1, -1 to 0, -1 to 1, 0 to -1, 0 to 1, 1 to -1, 1 to 0, 1 to 1).forEach { (dx, dy) ->
                    corrupts[pos.first + dx to pos.second + dy]?.join(it)
                }

                when (pos.first) {
                    0 -> {
                        it.join(left)
                    }

                    UPPER_BOUND -> {
                        it.join(right)
                    }
                }
                when (pos.second) {
                    0 -> {
                        it.join(top)
                    }

                    UPPER_BOUND -> {
                        it.join(bottom)
                    }
                }
            }

            if (top.parent == left.parent || bottom.parent == right.parent) {
                part2Result = "${pos.first},${pos.second}"
                break
            }

            if (corrupts.size == 1024) {
                val queue = PriorityQueue<Pair<Pair<Int, Int>, Int>>(compareBy { it.second })
                queue.add(0 to 0 to 0)
                val visited = hashSetOf<Pair<Int, Int>>()

                while (queue.isNotEmpty()) {
                    val (p, steps) = queue.poll()
                    val (x, y) = p
                    when {
                        x == UPPER_BOUND && y == UPPER_BOUND -> {
                            part1Result = steps
                            break
                        }

                        x !in 0..UPPER_BOUND || y !in 0..UPPER_BOUND -> {}
                        p in corrupts -> {}

                        visited.add(p) -> {
                            queue.add(x - 1 to y to steps + 1)
                            queue.add(x + 1 to y to steps + 1)
                            queue.add(x to y - 1 to steps + 1)
                            queue.add(x to y + 1 to steps + 1)
                        }
                    }
                }
            }
        }
    }
}