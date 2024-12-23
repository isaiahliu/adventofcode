package y2024

import util.expect
import util.input
import java.util.*

fun main() {
    expect(0, "") {
        val adjacent = hashMapOf<String, SortedSet<String>>()

        input.map { it.split("-") }.forEach { (from, to) ->
            adjacent.computeIfAbsent(from) { sortedSetOf() } += to
            adjacent.computeIfAbsent(to) { sortedSetOf() } += from
        }

        adjacent.forEach { (node1, adj) ->
            val match1 = node1[0] == 't'

            val adjArray = adj.toTypedArray()
            for (i in adjArray.indices) {
                val node2 = adjArray[i]
                val match2 = node2[0] == 't'
                for (j in i + 1 until adjArray.size) {
                    val node3 = adjArray[j]
                    val match3 = node3[0] == 't'

                    if (match1 || match2 || match3) {
                        if (adjacent[node2]?.contains(node3) == true) {
                            if (node1 < node2 && node2 < node3) {
                                part1Result++
                            }
                        }
                    }
                }
            }
        }
        var left = 1
        var right = adjacent.size

        val sortedNodes = adjacent.keys.sorted().toTypedArray()

        fun dfs(visited: List<String>, index: Int, targetSize: Int): Boolean {
            if (visited.size == targetSize) {
                part2Result = visited.joinToString(",")
                return true
            }

            for (i in index until sortedNodes.size) {
                val adj = adjacent[sortedNodes[i]].orEmpty()

                if (visited.all { it in adj } && dfs(visited + sortedNodes[i], i + 1, targetSize)) {
                    return true
                }
            }

            return false
        }

        while (left <= right) {
            val mid = (left + right) / 2

            if (dfs(emptyList(), 0, mid)) {
                left = mid + 1
            } else {
                right = mid - 1
            }
        }
    }
}
