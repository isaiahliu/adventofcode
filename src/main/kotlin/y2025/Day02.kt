package y2025

import util.expect
import util.input
import java.util.*

fun main() {
    expect(0L, 0L) {
        fun process(count: Int): Set<Long>? {
            val result = hashSetOf<Long>()
            val map = TreeMap<Long, Int>()
            input.forEach {
                it.split(",").forEach {
                    it.split("-").takeIf { it.size == 2 }?.map { it.toLong() }?.also { (from, to) ->
                        map[from] = (map[from] ?: 0) + 1
                        map[to + 1] = (map[to + 1] ?: 0) - 1
                    }
                }
            }

            var base = 0L
            var baseMove = 1L
            var m = 0L
            var t = 1L
            repeat(count) {
                m += t
                t *= baseMove
            }

            var size = 0
            while (map.isNotEmpty()) {
                if (++base == baseMove) {
                    baseMove *= 10
                    m = 0L
                    t = 1L
                    repeat(count) {
                        m += t
                        t *= baseMove
                    }
                }

                val invalidNum = base * m

                while (map.isNotEmpty() && invalidNum >= map.firstKey()) {
                    size += map.pollFirstEntry().value
                }

                if (size > 0) {
                    result += invalidNum
                }
            }

            return result.takeIf { base > 1 }
        }
        part1Result = process(2)?.sum() ?: 0L

        var times = 2
        part2Result = buildSet {
            while (true) {
                process(times++)?.also {
                    addAll(it)
                } ?: break
            }
        }.sum()
    }
}


