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
        var depth = 0
        val sums = arrayOfNulls<Int>(999)
        sums[0] = 0

        input.first().forEach {
            if (it in '0'..'9') {
                num *= 10
                num += (it - '0') * neg
                return@forEach
            }

            if (num != 0) {
                part1Result += num

                sums[depth] = sums[depth]?.let { it + num }
                num = 0
                neg = 1
            }

            when (it) {
                '-' -> {
                    neg = -1
                }

                '{' -> {
                    depth++
                    lefts.push(it)
                    sums[depth] = 0
                }

                '}' -> {
                    lefts.poll()

                    sums[depth]?.also { add ->
                        sums[depth - 1] = sums[depth - 1]?.let { it + add }
                    }
                    depth--
                }

                '[' -> {
                    lefts.push(it)
                }

                ']' -> {
                    lefts.poll()
                }

                'd' -> {
                    if (lefts.peek() == '{' && prev2 == 'r' && prev1 == 'e') {
                        sums[depth] = null
                    }
                }
            }

            prev2 = prev1
            prev1 = it
        }

        sums[0]?.also {
            part2Result = it
        }
    }
}