package y2016

import util.expect
import util.input
import util.md5

fun main() {
    expect(StringBuilder(), "") {
        var currentNum = 0
        val password2 = CharArray(8)
        val filledIndices = hashSetOf<Int>()

        while (part1Result.length < 8 || filledIndices.size < 8) {
            (input.first() + currentNum).md5.takeIf { it.startsWith("00000") }?.also { md5 ->
                if (part1Result.length < 8) {
                    part1Result.append(md5[5])
                }

                (md5[5] - '0').takeIf { it in (0 until 8) }?.takeIf { filledIndices.add(it) }?.also {
                    password2[it] = md5[6]
                }
            }
            currentNum++
        }

        part2Result = password2.concatToString()
    }
}