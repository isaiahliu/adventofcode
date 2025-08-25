package y2016

import util.expect
import util.input

fun main() {
    expect(0, 0) {
        input.forEach { line ->
            var sides = 0

            val abba = booleanArrayOf(false, false)
            val aba = arrayOf(hashSetOf<Pair<Char, Char>>(), hashSetOf())

            line.forEachIndexed { index, ch ->
                when (ch) {
                    '[', ']' -> sides = 1 - sides
                    line.getOrNull(index - 1) -> {}
                    line.getOrNull(index - 3) if line.getOrNull(index - 2) == line.getOrNull(index - 1) -> {
                        abba[sides] = true
                    }

                    line.getOrNull(index - 2) -> {
                        aba[sides] += ch to line[index - 1]
                    }
                }
            }

            if (abba[0] && !abba[1]) {
                part1Result++
            }

            if (aba[0].any { (a, b) -> b to a in aba[1] }) {
                part2Result++
            }
        }
    }
}