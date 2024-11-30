package y2024

import util.expect
import util.input

fun main() {
    expect(0, 0) {
        input.forEach {
            it.toIntOrNull()?.also {
                part1Result += it
                part2Result -= it
            }
        }
    }
}