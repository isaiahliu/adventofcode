package y2024

import util.expect
import util.input

fun main() {
    expect(0, "") {
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

            fun query(index: Int, key: IntArray): Int {
                return key.getOrNull(index)?.let {
                    children[it].query(index + 1, key)
                } ?: count
            }
        }

        val keys = arrayListOf<IntArray>()

        val rootLock = Trie()

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
                keys += item
            } else {
                rootLock.add(0, item)
            }
        }
        part1Result = keys.sumOf { rootLock.query(0, it) }
        part2Result = "Merry Christmas!"
    }
}