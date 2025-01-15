package y2015

import util.expect
import util.input

fun main() {
    expect(0, 0) {
        input.map { it.split("x").map { it.toInt() }.sorted() }.filter { it.size == 3 }.forEach { (a, b, c) ->
            part1Result += 3 * a * b + 2 * a * c + 2 * b * c
            part2Result += a * 2 + b * 2 + a * b * c
        }
    }
}
