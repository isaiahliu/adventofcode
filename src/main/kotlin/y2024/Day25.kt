package y2024

import util.expect
import util.input

fun main() {
    expect(0, "Merry Christmas!") {
        class Trie {
            val children by lazy { Array(8) { Trie() } }

            var count: Int = 0

            fun add(index: Int, lock: IntArray) {
                lock.getOrNull(index)?.also {
                    (0 until 8 - it).forEach {
                        children[it].add(index + 1, lock)
                    }
                } ?: count++
            }
        }

        val root = Trie()
        val keys = arrayListOf<Trie>()

        (input.indices step 8).forEach {
            val item = IntArray(5)

            (it until it + 7).map { input[it] }.forEach {
                it.forEachIndexed { index, ch ->
                    if (ch == '#') {
                        item[index]++
                    }
                }
            }

            if (input[it][0] == '#') {
                root.add(0, item)
            } else {
                keys += item.fold(root) { p, index -> p.children[index] }
            }
        }

        part1Result = keys.sumOf { it.count }
    }
}
