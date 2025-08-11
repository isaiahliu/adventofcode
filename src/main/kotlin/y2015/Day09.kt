package y2015

import util.expect
import util.input

fun main() {
    expect(Int.MAX_VALUE, 0) {
        val regex = "(.*) to (.*) = (.*)".toRegex()

        val adjacent = hashMapOf<String, MutableMap<String, Int>>()
        input.forEach {
            val match = regex.matchEntire(it) ?: return@forEach

            val city1 = match.groupValues[1]
            val city2 = match.groupValues[2]
            val distance = match.groupValues[3].toInt()

            adjacent.computeIfAbsent(city1) { hashMapOf() }[city2] = distance
            adjacent.computeIfAbsent(city2) { hashMapOf() }[city1] = distance
        }

        fun dfs(city: String, visited: Set<String>, distance: Int) {
            when {
                visited.size == adjacent.size -> {
                    part1Result = minOf(part1Result, distance)
                    part2Result = maxOf(part2Result, distance)
                }

                else -> {
                    adjacent[city]?.forEach { (to, d) ->
                        if (to !in visited) {
                            dfs(to, visited + to, distance + d)
                        }
                    }
                }
            }
        }

        adjacent.keys.forEach {
            dfs(it, setOf(it), 0)
        }
    }
}
