package y2016

import util.expect
import util.input

fun main() {
    expect(0L, 0L) {
        class SegNode(val start: Long, val end: Long) {
            val children by lazy {
                arrayOf(SegNode(start, (start + end) / 2), SegNode((start + end) / 2 + 1, end))
            }

            var someBanned = false
            var allBanned = false

            fun mark(from: Long, to: Long) {
                when {
                    from > end || to < start -> {}
                    from <= start && to >= end -> allBanned = true
                    else -> {
                        someBanned = true
                        children.forEach {
                            it.mark(from, to)
                        }
                    }
                }
            }

            fun min(): Long? {
                return when {
                    allBanned -> null
                    !someBanned -> start
                    else -> children[0].min() ?: children[1].min()
                }
            }

            fun count(): Long {
                return when {
                    allBanned -> 0
                    !someBanned -> end - start + 1
                    else -> children.sumOf { it.count() }
                }
            }
        }

        val root = SegNode(0L, (1L shl 32) - 1)

        input.map { it.split("-").map { it.toLong() } }.forEach { (from, to) ->
            root.mark(from, to)
        }

        part1Result = root.min() ?: 0
        part2Result = root.count()
    }
}
