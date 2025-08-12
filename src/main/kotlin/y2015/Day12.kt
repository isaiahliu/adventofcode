package y2015

import util.expect
import util.input
import java.util.*

fun main() {
    expect(0, 0) {
        var num = 0
        var neg = 1

        var prev2 = Char(0)
        var prev1 = Char(0)

        val lefts = LinkedList<Char>()
        val sums = LinkedList<Int?>()
        sums.push(0)

        input.first().forEach {
            if (it in '0'..'9') {
                num *= 10
                num += (it - '0') * neg
                return@forEach
            }

            if (num != 0) {
                part1Result += num

                sums.push(sums.poll()?.let { it + num })
                num = 0
                neg = 1
            }

            when {
                it == '-' -> {
                    neg = -1
                }

                it == '{' -> {
                    lefts.push(it)
                    sums.push(0)
                }

                it == '}' -> {
                    lefts.poll()

                    sums.poll()?.also { sum ->
                        sums.push(sums.poll()?.let { it + sum })

                    }
                }

                it == '[' -> {
                    lefts.push(it)
                }

                it == ']' -> {
                    lefts.poll()
                }

                lefts.peek() == '{' && prev2 == 'r' && prev1 == 'e' && it == 'd' -> {
                    sums.poll()
                    sums.push(null)
                }
            }

            prev2 = prev1
            prev1 = it
        }

        part2Result = sums[0] ?: 0
    }
}