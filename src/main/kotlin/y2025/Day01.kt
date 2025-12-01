package y2025

import util.expect
import util.input

fun main() {
    expect(0, 0) {
        var position = 1000050
        input.forEach {
            val direction = it[0]
            val distance = it.drop(1).toInt()

            when (direction) {
                'L' -> {
                    val pre = (position - 1) / 100
                    position -= distance
                    val post = (position - 1) / 100
                    part2Result += pre - post
                }

                'R' -> {
                    val pre = position / 100
                    position += distance
                    val post = position / 100
                    part2Result += post - pre
                }
            }

            if (position % 100 == 0) {
                part1Result++
            }
        }
    }
}


