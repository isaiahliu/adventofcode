package y2016

import util.expect
import util.input

fun main() {
    expect(StringBuilder(), StringBuilder()) {
        input.first().indices.map { index ->
            input.groupingBy { it[index] }.eachCount()
        }.forEach {
            part1Result.append(it.maxBy { it.value }.key)
            part2Result.append(it.minBy { it.value }.key)
        }
    }
}