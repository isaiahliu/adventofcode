package y2025

import util.expect
import util.input

fun main() {
    expect(0, 0L) {
        var readId = false

        class SegNode(val from: Long, val to: Long) {
            val children by lazy {
                arrayOf(SegNode(from, from + (to - from) / 2), SegNode(from + (to - from) / 2 + 1, to))
            }

            var count = 0L

            fun mark(left: Long, right: Long) {
                when {
                    left > to || right < from -> Unit
                    left <= from && right >= to -> count = to - from + 1
                    count == to - from + 1 -> Unit
                    else -> count = children.sumOf {
                        it.mark(left, right)
                        it.count
                    }
                }
            }

            fun valid(id: Long): Boolean {
                return when {
                    id !in from..to -> false
                    count == 0L -> false
                    count == to - from + 1 -> true
                    else -> children.any { it.valid(id) }
                }
            }
        }

        val root = SegNode(0, Long.MAX_VALUE - 1)

        input.forEach {
            when {
                it.isEmpty() -> readId = true
                readId -> {
                    if (root.valid(it.toLong())) {
                        part1Result++
                    }
                }

                else -> {
                    it.split("-").also { (from, to) ->
                        root.mark(from.toLong(), to.toLong())
                    }
                }
            }
        }

        part2Result = root.count
    }
}


