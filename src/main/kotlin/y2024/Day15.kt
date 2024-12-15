package y2024

import util.expect
import util.input

fun main() {
    expect(0, 0) {
        var readingMovement = false

        val UP = 0
        val RIGHT = 1
        val DOWN = 2
        val LEFT = 3

        class Warehouse {
            val walls = hashSetOf<Pair<Int, Int>>()

            val boxes = hashMapOf<Pair<Int, Int>, Int?>()

            var robot = 0 to 0

            fun move(direction: Int) {
                if (dfs(robot, direction, false, hashSetOf())) {
                    dfs(robot, direction, true, hashSetOf())
                }
            }

            private fun dfs(node: Pair<Int, Int>, direction: Int, move: Boolean, visited: MutableSet<Pair<Int, Int>>): Boolean {
                if (node in walls) {
                    return false
                }

                if (node !in boxes && node != robot) {
                    return true
                }

                if (!visited.add(node)) {
                    return true
                }

                val target = when (direction) {
                    UP -> node.first - 1 to node.second
                    DOWN -> node.first + 1 to node.second
                    LEFT -> node.first to node.second - 1
                    else -> node.first to node.second + 1
                }

                if (direction % 2 == 0) {
                    boxes[target]?.also { o ->
                        if (!dfs(target.first to target.second + o, direction, move, visited)) {
                            return false
                        }
                    }
                }

                if (!dfs(target, direction, move, visited)) {
                    return false
                }

                if (move) {
                    if (node == robot) {
                        robot = target
                    } else {
                        boxes[target] = boxes[node]
                        boxes -= node
                    }
                }

                return true
            }

            val score: Int
                get() {
                    return boxes.filter { it.value?.takeIf { it < 0 } == null }.keys.sumOf { (r, c) ->
                        r * 100 + c
                    }
                }

        }

        val warehouse1 = Warehouse()
        val warehouse2 = Warehouse()
        input.forEachIndexed { r, line ->
            when {
                line.isEmpty() -> {
                    readingMovement = true
                }

                !readingMovement -> {
                    line.forEachIndexed { c, ch ->
                        when (ch) {
                            '#' -> {
                                warehouse1.walls += r to c
                                warehouse2.walls += r to c * 2
                                warehouse2.walls += r to c * 2 + 1
                            }

                            'O' -> {
                                warehouse1.boxes[r to c] = null

                                warehouse2.boxes[r to c * 2] = 1
                                warehouse2.boxes[r to c * 2 + 1] = -1

                            }

                            '@' -> {
                                warehouse1.robot = r to c
                                warehouse2.robot = r to c * 2
                            }
                        }
                    }
                }

                else -> {
                    line.forEach {
                        when (it) {
                            '^' -> {
                                warehouse1.move(UP)
                                warehouse2.move(UP)
                            }

                            '>' -> {
                                warehouse1.move(RIGHT)
                                warehouse2.move(RIGHT)
                            }

                            'v' -> {
                                warehouse1.move(DOWN)
                                warehouse2.move(DOWN)
                            }

                            '<' -> {
                                warehouse1.move(LEFT)
                                warehouse2.move(LEFT)
                            }
                        }
                    }
                }
            }
        }

        part1Result = warehouse1.score
        part2Result = warehouse2.score
    }
}