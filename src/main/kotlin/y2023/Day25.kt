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

        for (i in connections.indices) {
            val (from1, to1) = connections[i]

            val availableConnections = connections.toMutableSet()
            availableConnections -= connections[i]
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
