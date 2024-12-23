package y2024

import util.expect
import util.input

fun main() {
    expect(0, "") {
        val lt = hashMapOf<String, MutableSet<String>>()
        val gt = hashMapOf<String, MutableSet<String>>()

        input.map { it.split("-") }.forEach { (from, to) ->
            lt.computeIfAbsent(maxOf(from, to)) { hashSetOf() } += minOf(from, to)
            gt.computeIfAbsent(minOf(from, to)) { hashSetOf() } += maxOf(from, to)
        }

        var maxSize = 0

        class Node(val node: String, val parent: Node? = null, val size: Int) {
            fun match(adj: Set<String>): Boolean {
                return node.isEmpty() || node in adj && parent?.match(adj) == true
            }

            fun asString(): String {
                return parent?.asString()?.let { "$it," }.orEmpty() + node
            }

            fun startsWithT(): Boolean {
                return node.firstOrNull() == 't' || parent?.startsWithT() == true
            }

            fun dfs() {
                if (size > maxSize) {
                    maxSize = size
                    part2Result = asString().drop(1)
                }

                if (size == 3 && startsWithT()) {
                    part1Result++
                }

                gt[node]?.forEach {
                    if (match(lt[it].orEmpty())) {
                        Node(it, this, size + 1).dfs()
                    }
                }
            }
        }

        gt[""] = gt.keys.toMutableSet()

        Node("", null, 0).dfs()
    }
}
