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
            val nodes = hashMapOf<Pair<Int, Int>, Int>()

            var robot = 0 to 0

            fun move(direction: Int) {
                if (dfs(robot, direction, false, hashSetOf())) {
                    dfs(robot, direction, true, hashSetOf())
                }
            }

            private fun dfs(node: Pair<Int, Int>, direction: Int, move: Boolean, visited: MutableSet<Pair<Int, Int>>): Boolean {
                val target = when (direction) {
                    UP -> node.first - 1 to node.second
                    DOWN -> node.first + 1 to node.second
                    LEFT -> node.first to node.second - 1
                    else -> node.first to node.second + 1
                }

                return when {
                    nodes[node] == Int.MAX_VALUE -> {
                        false
                    }

                    nodes[node] == null && node != robot -> {
                        true
                    }

                    !visited.add(node) -> {
                        true
                    }

                    direction % 2 == 0 && nodes[target]?.let { dfs(target.first to target.second + it, direction, move, visited) } == false -> {
                        false
                    }

                    !dfs(target, direction, move, visited) -> {
                        false
                    }

                    move && node == robot -> {
                        robot = target
                        true
                    }

                    move -> {
                        nodes.remove(node)?.also {
                            nodes[target] = it
                        }
                        true
                    }

                    else -> {
                        true
                    }
                }
            }

            val score: Int
                get() {
                    return nodes.filter { it.value in 0..1 }.keys.sumOf { (r, c) ->
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
                                warehouse1.nodes[r to c] = Int.MAX_VALUE
                                warehouse2.nodes[r to c * 2] = Int.MAX_VALUE
                                warehouse2.nodes[r to c * 2 + 1] = Int.MAX_VALUE
                            }

                            'O' -> {
                                warehouse1.nodes[r to c] = 0
                                warehouse2.nodes[r to c * 2] = 1
                                warehouse2.nodes[r to c * 2 + 1] = -1

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