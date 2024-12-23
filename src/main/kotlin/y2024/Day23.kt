package y2024

import util.expect
import util.input

fun main() {
    expect(0, "") {
        val adjacent = hashMapOf<String, MutableSet<String>>()

        input.map { it.split("-") }.forEach { (from, to) ->
            adjacent.computeIfAbsent(from) { hashSetOf() } += to
            adjacent.computeIfAbsent(to) { hashSetOf() } += from
        }
        var maxSize = 0

        class Trie(val node: String, val parent: Trie? = null, val size: Int) {
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

                adjacent[node]?.forEach { child ->
                    if (child > node && match(adjacent[child].orEmpty())) {
                        Trie(child, this, size + 1).dfs()
                    }
                }
            }
        }

        adjacent[""] = adjacent.keys.toMutableSet()

        Trie("", null, 0).dfs()
    }
}
