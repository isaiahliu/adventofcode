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

        data class Changes(val c1: Int?, val c2: Int?, val c3: Int?, val c4: Int?)

        val total = hashMapOf<Changes, Int>()

        input.forEach {
            val visited = hashSetOf<Changes>()

            var sec = it.toLong()

            var changes = Changes(null, null, null, null)

            var prevPrice = (sec % 10).toInt()

            repeat(2000) {
                sec = sec.next()

                val price = (sec % 10).toInt()

                changes = changes.copy(c1 = changes.c2, c2 = changes.c3, c3 = changes.c4, c4 = price - prevPrice)

                if (changes.c1 != null) {
                    if (visited.add(changes)) {
                        total[changes] = (total[changes] ?: 0) + price
                    }
                }

                prevPrice = price
            }

            part1Result += sec
        }

        part2Result = total.values.max()
    }
}
