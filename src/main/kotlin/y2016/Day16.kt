package y2016

import util.expect
import util.input

fun main() {
    expect(StringBuilder(), StringBuilder()) {
        class CheckSum(val size: Int, private val output: StringBuilder) {
            var count = 0
            var checkSum = 0

            val child by lazy { CheckSum(size / 2, output) }
            fun add(value: Int) {
                if (size % 2 == 1) {
                    output.append(value)
                } else {
                    count++
                    checkSum += value

                    if (count == 2) {
                        child.add((checkSum and 1) xor 1)
                        checkSum = 0
                        count = 0
                    }
                }
            }
        }

        val base = input.first()
        var baseIndex = -1
        var baseIndexOffset = 1
        var split = 1

        val checkSums = arrayListOf(CheckSum(272, part1Result), CheckSum(35651584, part2Result))

        repeat(35651584) {
            baseIndex += baseIndexOffset

            val num = base.getOrNull(baseIndex)?.let {
                it - '0' xor ((baseIndexOffset + 1) / 2) xor 1
            } ?: run {
                baseIndexOffset = -baseIndexOffset
                var num = split++
                var s = 0
                while (num > 1) {
                    num.takeHighestOneBit().takeIf { it < num }?.also { higher ->
                        num = higher * 2 - num

                        s = s xor 1
                    } ?: break
                }
                s
            }

            checkSums.forEach { checkSum ->
                if (it < checkSum.size) {
                    checkSum.add(num)
                }
            }
        }
    }
}

