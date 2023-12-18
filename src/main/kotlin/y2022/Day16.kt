package y2022

import util.expectInt
import util.input

fun main() {
    expectInt {
        val pressures = hashMapOf<String, Int>()
        val forwards = hashMapOf<String, Set<String>>()

        input.map { it.split(" ", "=") }.forEach {
            val valve = it[1]
            val rate = it[5].trimEnd(';').toInt()
            val children = it.drop(10).map { it.trimEnd(',') }.toSet()

            if (rate > 0) {
                pressures[valve] = rate
            }

            forwards[valve] = children
        }

        val routeMap = hashMapOf<String, MutableMap<String, Int>>()

        fun findDistance(from: String, to: String): Int {
            return routeMap.computeIfAbsent(from) { hashMapOf() }.computeIfAbsent(to) {
                val reached = hashSetOf<String>()
                val reachable = forwards[from].orEmpty().toMutableSet()

                var distance = 1

                while (true) {
                    if (reachable.isEmpty()) {
                        distance = Int.MAX_VALUE
                        break
                    }

                    if (to in reachable) {
                        break
                    }

                    val t = reachable.toSet()
                    reachable.clear()

                    t.forEach {
                        forwards[it]?.forEach {
                            if (reached.add(it)) {
                                reachable += it
                            }
                        }
                    }

                    distance++
                }

                return@computeIfAbsent distance
            }
        }

        fun calculatePressure(minutes: Int, path: Array<String>): Int {
            var remainingMinutes = minutes

            var currentNode: String? = null

            var pressure = 0
            path.forEach { node ->
                remainingMinutes -= findDistance(currentNode ?: "AA", node) + 1

                pressure += remainingMinutes * (pressures[node] ?: 0)

                currentNode = node
            }
            return pressure
        }

        fun process(minutes: Int, walker: Int): Int {
            fun walk(availableNodes: Set<String>, remainingMinutes: Int, currentPath: Array<String>): Array<String> {
                val possibilities = hashSetOf<Array<String>>()

                availableNodes.filter { it !in currentPath }.forEach { to ->
                    val newRemainingTime = remainingMinutes - findDistance(currentPath.lastOrNull() ?: "AA", to) - 1

                    if (newRemainingTime < 0) {
                        return@forEach
                    }

                    val newPaths = Array(currentPath.size + 1) { currentPath.getOrNull(it) ?: to }

                    possibilities += if (newRemainingTime < 2) {
                        newPaths
                    } else {
                        walk(availableNodes, newRemainingTime, newPaths)
                    }
                }

                return possibilities.maxByOrNull { calculatePressure(minutes, it) } ?: currentPath
            }

            return when (walker) {
                2 -> {
                    val cache = walk(pressures.keys, minutes, emptyArray())
                    val bestPath = listOf(*cache)
                    val bestScore = calculatePressure(minutes, cache)

                    val keys = pressures.keys.toList()

                    (0 until 1.shl(keys.size)).filter {
                        it.countOneBits() <= keys.size / 2
                    }.maxOf {
                        val left = hashSetOf<String>()
                        val right = hashSetOf<String>()

                        repeat(keys.size) { keyIndex ->
                            if (it.and(1.shl(keyIndex)) > 0) {
                                left
                            } else {
                                right
                            } += keys[keyIndex]
                        }

                        val leftScore = if (left.containsAll(bestPath)) {
                            bestScore
                        } else {
                            calculatePressure(minutes, walk(left, minutes, emptyArray()))
                        }

                        val rightScore = if (right.containsAll(bestPath)) {
                            bestScore
                        } else {
                            calculatePressure(minutes, walk(right, minutes, emptyArray()))
                        }

                        leftScore + rightScore
                    }
                }

                else -> {
                    calculatePressure(minutes, walk(pressures.keys, minutes, emptyArray()))
                }
            }
        }

        part1Result = process(30, 1)
        part2Result = process(26, 2)
    }
}