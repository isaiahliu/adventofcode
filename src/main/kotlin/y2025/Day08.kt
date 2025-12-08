package y2025

import util.expect
import util.input
import java.util.*

fun main() {
    expect(0L, 0L) {
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

        val nodes = input.map { it.split(",").map { it.toLong() }.toTypedArray() }.toTypedArray()

        val max = PriorityQueue<Pair<Long, Pair<Int, Int>>>(compareByDescending { it.first })
        val min = PriorityQueue<Pair<Long, Pair<Int, Int>>>(compareBy { it.first })

        for (from in nodes.indices) {
            val (fx, fy, fz) = nodes[from]
            for (to in from + 1 until nodes.size) {
                val (tx, ty, tz) = nodes[to]

                val distance = (fx - tx) * (fx - tx) + (fy - ty) * (fy - ty) + (fz - tz) * (fz - tz) to (from to to)

                min.add(distance)
                max.add(distance)

                if (max.size > 1000) {
                    max.poll()
                }
            }
        }

        var groups = Array(nodes.size) {
            Group().also { it.size = 1 }
        }
        while (max.isNotEmpty()) {
            val (from, to) = max.poll().second
            groups[from].join(groups[to])
        }

        part1Result = groups.map { it.parent }.distinct().map { it.size }.sortedByDescending { it }.take(3).fold(1L) { a, b -> a * b }

        groups = Array(nodes.size) {
            Group().also { it.size = 1 }
        }
        while (min.isNotEmpty()) {
            val (from, to) = min.poll().second
            groups[from].join(groups[to])

            if (groups[from].parent.size == groups.size) {
                part2Result = nodes[from].first() * nodes[to].first()
                break
            }
        }
    }
}


