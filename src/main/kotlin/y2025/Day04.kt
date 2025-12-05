package y2025

import util.expect
import util.input

fun main() {
    expect(0, 0) {
        val nodes = hashMapOf<Pair<Int, Int>, Int>()
        input.forEachIndexed { r, row ->
            row.forEachIndexed { c, ch ->
                if (ch == '@') {
                    nodes[r to c] = 0
                }
            }
        }

        nodes.keys.forEach { (r, c) ->
            arrayOf(r - 1 to c - 1, r - 1 to c, r - 1 to c + 1, r to c - 1, r to c + 1, r + 1 to c - 1, r + 1 to c, r + 1 to c + 1).forEach {
                nodes[it]?.also { count ->
                    nodes[it] = count + 1
                }

            }
        }

        part1Result = nodes.values.count { it < 4 }

        while (true) {
            val removed = nodes.filter { it.value < 4 }

            if (removed.isEmpty()) {
                break
            }

            part2Result += removed.size

            nodes -= removed.keys

            removed.keys.forEach { (r, c) ->
                arrayOf(r - 1 to c - 1, r - 1 to c, r - 1 to c + 1, r to c - 1, r to c + 1, r + 1 to c - 1, r + 1 to c, r + 1 to c + 1).forEach {
                    nodes[it]?.also { count ->
                        nodes[it] = count - 1
                    }
                }
            }
        }
    }
}


