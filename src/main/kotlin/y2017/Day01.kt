package y2017

import util.expect
import util.input

fun main() {
    expect(0, 0) {
        val line = input.first()
        line.forEachIndexed { index, ch ->
            if (ch == line[(index + 1) % line.length]) {
                part1Result += ch - '0'
            }

            if (ch == line[(index + line.length / 2) % line.length]) {
                part2Result += ch - '0'
            }
        }
    }
}