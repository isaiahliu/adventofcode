package y2024

import util.expect
import util.input
import java.util.*

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

        data class Changes(val c1: Int, val c2: Int, val c3: Int, val c4: Int)

        val total = hashMapOf<Changes, Int>()

        input.forEach {
            val visited = hashSetOf<Changes>()

            var sec = it.toLong()

            var prevPrice = (sec % 10).toInt()
            val prices = LinkedList<Int>()
            repeat(2000) {
                sec = sec.next()

                val price = (sec % 10).toInt()

                prices.add(price - prevPrice)
                if (prices.size > 4) {
                    prices.poll()
                }

                if (prices.size == 4) {
                    val changes = Changes(prices[0], prices[1], prices[2], prices[3])

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
