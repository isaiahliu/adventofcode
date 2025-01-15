package y2015

import util.expect
import util.input

fun main() {
    expect(0, Int.MAX_VALUE) {
        var found = false
        input.first().forEachIndexed { index, c ->
            part1Result += if (c == '(') 1 else -1

            if (!found && part1Result < 0) {
                part2Result = index + 1
                found = true
            }
        }
    }
}
