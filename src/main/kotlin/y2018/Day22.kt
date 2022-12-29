package y2018

import util.input

fun main() {
    val ROCKY = 0
    val WET = 1
    val NARROW = 2

    val depth = input.first().split(" ").last().toInt()
    val (targetX, targetY) = input.drop(1).first().split(" ").last().split(",").map { it.toInt() }
//    depth = 510
//    targetX = 10
//    targetY = 10

    val cache = hashMapOf<Pair<Int, Int>, Int>()

    fun erosionLevel(x: Int, y: Int): Int {
        if (x to y !in cache) {
            cache[x to y] = (when {
                x == 0 && y == 0 -> 0
                x == targetX && y == targetY -> 0
                x == 0 -> y * 48271
                y == 0 -> x * 16807
                else -> erosionLevel(x - 1, y) * erosionLevel(x, y - 1)
            } + depth) % 20183

        }

        return cache[x to y]!!
    }

    fun riskLevel(x: Int, y: Int): Int {
        return erosionLevel(x, y) % 3
    }

    println((0..targetY).sumOf { y ->
        (0..targetX).sumOf { x -> riskLevel(x, y) }
    })

    val NEITHER = 0
    val TORCH = 1
    val GEAR = 2

    data class Task(val x: Int, val y: Int, var switchingTools: Int, var tools: Int)

    fun walk(): Int {
        var result = 0
        val tasks = arrayListOf(Task(0, 0, 0, TORCH))
        val walked = hashMapOf<Int, MutableSet<Pair<Int, Int>>>(NEITHER to hashSetOf(), TORCH to hashSetOf(), GEAR to hashSetOf())

        while (true) {
            val current = tasks.toList()
            tasks.clear()
            for (task in current) {
                if (task.switchingTools > 0) {
                    task.switchingTools--

                    if (task.x == targetX && task.y == targetY && task.switchingTools == 0 && task.tools == TORCH) {
                        return result
                    }

                    tasks += task
                } else {
                    if (!walked[task.tools]!!.add(task.x to task.y)) {
                        continue
                    }

                    if (task.x == targetX && task.y == targetY) {
                        if (task.tools == TORCH) {
                            return result
                        } else {
                            tasks += task.copy(switchingTools = 7, tools = TORCH)
                        }
                        continue
                    }

                    arrayOf(task.x - 1 to task.y, task.x + 1 to task.y, task.x to task.y - 1, task.x to task.y + 1).filter { (x, y) -> x >= 0 && y >= 0 }.forEach { (x, y) ->
                        when (riskLevel(x, y)) {
                            ROCKY -> {
                                if (task.tools == GEAR || task.tools == TORCH) {
                                    tasks += task.copy(x = x, y = y)
                                } else {
                                    tasks += task.copy(x = x, y = y, switchingTools = 7, tools = GEAR)
                                    tasks += task.copy(x = x, y = y, switchingTools = 7, tools = TORCH)
                                }
                            }

                            WET -> {
                                if (task.tools == GEAR || task.tools == NEITHER) {
                                    tasks += task.copy(x = x, y = y)
                                } else {
                                    tasks += task.copy(x = x, y = y, switchingTools = 7, tools = GEAR)
                                    tasks += task.copy(x = x, y = y, switchingTools = 7, tools = NEITHER)
                                }
                            }

                            NARROW -> {
                                if (task.tools == NEITHER || task.tools == TORCH) {
                                    tasks += task.copy(x = x, y = y)
                                } else {
                                    tasks += task.copy(x = x, y = y, switchingTools = 7, tools = NEITHER)
                                    tasks += task.copy(x = x, y = y, switchingTools = 7, tools = TORCH)
                                }
                            }

                        }
                    }
                }
            }

            result++
        }
    }

    println(walk())
}