package y2023

import util.expect
import util.input
import java.util.*

fun main() {
    expect(0, "") {
        class Group {
            var size = 0

            var innerParent: Group? = null
                private set

            var parent: Group
                set(value) {
                    innerParent = value
                    value.size += size
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

        val adjacent = hashMapOf<String, MutableSet<String>>()
        val connections = arrayListOf<Pair<String, String>>()

        input.forEach {
            val (left, right) = it.split(": ")

            right.split(" ").forEach {
                connections += minOf(left, it) to maxOf(left, it)

                adjacent.computeIfAbsent(left) { hashSetOf() } += it
                adjacent.computeIfAbsent(it) { hashSetOf() } += left
            }
        }

        /**
         * 枚举第一对点, 假设它是需要移除的通路, 那移除后, from和to之间只应存在两条最小通路
         * 用BFS找出from到to的一条from到to的最短通路, 将其移除, 然后再用BFS找出一条最短通路移除
         * 如果这时from和to断开了, 说明第一对点是正确的, 第二第三对点也在两次BFS的路径中
         * 后面应该有更好的方法找到正确的两对点, 但是我懒得写了, 直接暴力枚举
         * 将枚举的三对点从连接中移除, 基于此使用并查集来统计分组顺便计数
         * 如果并查集有两组, 则返回group1.size * group2.size即可
         */
        for ((from1, to1) in connections) {
            val availableConnections = connections.toMutableSet()
            availableConnections -= from1 to to1

            fun bfs(): Set<Pair<String, String>>? {
                val timestamps = hashMapOf<String, Int>()
                val tasks = LinkedList<String>()
                tasks += from1

                var timestamp = 0

                while (tasks.isNotEmpty() && timestamps[to1] == null) {
                    timestamp++
                    repeat(tasks.size) {
                        val current = tasks.poll()

                        timestamps[current] ?: run {
                            timestamps[current] = timestamp

                            if (current != to1) {
                                adjacent[current]?.forEach { next ->
                                    if (minOf(current, next) to maxOf(current, next) in availableConnections) {
                                        tasks.add(next)
                                    }
                                }
                            }
                        }
                    }
                }

                return timestamps[to1]?.let {
                    val result = hashSetOf<Pair<String, String>>()

                    var current = to1
                    while (--timestamp > 0) {
                        val next = adjacent[current]?.first {
                            timestamps[it] == timestamp && minOf(current, it) to maxOf(
                                current,
                                it
                            ) in availableConnections
                        } ?: throw Exception("Error")

                        result.add(minOf(current, next) to maxOf(current, next))

                        current = next
                    }

                    result
                }
            }

            val seconds = bfs().orEmpty()
            availableConnections.removeAll(seconds)

            val thirds = bfs().orEmpty()
            availableConnections.removeAll(thirds)

            if (bfs() == null) {
                //from1 to to1 is disconnected by removing seconds and thirds paths.
                for ((from2, to2) in seconds) {
                    for ((from3, to3) in thirds) {
                        val groups = hashMapOf<String, Group>()

                        for ((from, to) in connections) {
                            if ((from != from1 || to != to1) && (from != from2 || to != to2) && (from != from3 || to != to3)) {
                                val g1 = groups.computeIfAbsent(from) { Group().also { it.size++ } }
                                val g2 = groups.computeIfAbsent(to) { Group().also { it.size++ } }

                                g1.join(g2)

                                if (g1.parent.size == adjacent.size) {
                                    break
                                }
                            }
                        }

                        val parents = groups.values.map { it.parent }.distinct()

                        if (parents.size == 2) {
                            part1Result = parents[0].size * parents[1].size
                            part2Result = "DONE!"
                            return@expect
                        }
                    }
                }
            }
        }
    }
}
