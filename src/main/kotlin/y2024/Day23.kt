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

        class Trie(val node: String, val parent: Trie? = null, val size: Int) {
            val children by lazy { hashMapOf<String, Trie>() }

            var maxSize = Int.MAX_VALUE

            fun match(adj: Set<String>): Boolean {
                return node.isEmpty() || node in adj && parent?.match(adj) == true
            }

            fun asString(): String {
                return parent?.asString()?.let { "$it," }.orEmpty() + node
            }

            fun markMaxSize(size: Int?) {
                size?.also { maxSize = it } ?: run {
                    maxSize = children.values.maxOf { it.maxSize }
                }

                parent?.markMaxSize(null)
            }

            fun startsWithT(): Boolean {
                return node.firstOrNull() == 't' || parent?.startsWithT() == true
            }
        }

        var maxSize = 0
        fun dfs(trie: Trie) {
            if (trie.size > maxSize) {
                maxSize = trie.size
                part2Result = trie.asString().drop(1)
            }

            if (trie.size == 3 && trie.startsWithT()) {
                part1Result++
            }

            when {
                trie.maxSize < maxSize -> {
                    return
                }

                else -> {
                    adjacent[trie.node]?.forEach { child ->
                        if (child > trie.node && trie.match(adjacent[child].orEmpty())) {
                            val childTrie = trie.children.computeIfAbsent(child) { Trie(child, trie, trie.size + 1) }

                            dfs(childTrie)
                        }
                    }

                    if (trie.children.isEmpty()) {
                        trie.markMaxSize(trie.size)
                    }
                }
            }
        }

        val root = Trie("", null, 0)

        adjacent[""] = adjacent.keys.toMutableSet()

        dfs(root)
    }
}
