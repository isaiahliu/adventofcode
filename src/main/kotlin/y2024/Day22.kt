package y2024

import util.expect
import util.input

fun main() {
    expect(0L, 0) {
        fun Long.mixAndPrune(sec: Long): Long {
            return (this xor sec) % 16777216
        }

        fun Long.process(op: (Long) -> Long): Long {
            return op(this).mixAndPrune(this)
        }

        fun Long.next(): Long {
            return this.process { it * 64 }.process { it / 32 }.process { it * 2048 }
        }

        val map = hashMapOf<Int, MutableMap<Int, MutableMap<Int, MutableMap<Int, IntArray>>>>()

        input.forEachIndexed { index, str ->
            var map1: MutableMap<Int, MutableMap<Int, MutableMap<Int, IntArray>>>? = null
            var map2: MutableMap<Int, MutableMap<Int, IntArray>>? = null
            var map3: MutableMap<Int, IntArray>? = null

            var sec = str.toLong()

            var prevPrice = (sec % 10).toInt()

            repeat(2000) {
                sec = sec.next()

                val price = (sec % 10).toInt()

                val diff = price - prevPrice

                map3?.computeIfAbsent(diff) { intArrayOf(-1, 0) }?.also {
                    if (it[0] < index) {
                        it[0] = index
                        it[1] += price

                        part2Result = maxOf(part2Result, it[1])
                    }
                }

                map3 = map2?.computeIfAbsent(diff) { hashMapOf() }
                map2 = map1?.computeIfAbsent(diff) { hashMapOf() }
                map1 = map.computeIfAbsent(diff) { hashMapOf() }

                prevPrice = price
            }

            part1Result += sec
        }
    }
}
