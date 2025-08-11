package y2015

import util.expect
import util.input

fun main() {
    expect(0, Int.MAX_VALUE) {
        input.first().forEachIndexed { index, c ->
            part1Result += if (c == '(') 1 else -1

            if (part1Result < 0) {
                part2Result = minOf(part2Result, index + 1)
            }
        }
    }
}
