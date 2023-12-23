package y2022

import util.expect
import util.input
import kotlin.math.absoluteValue

fun main() {
    expect(0, 0L) {
        val regex = "Sensor at x=(-?\\d+), y=(-?\\d+): closest beacon is at x=(-?\\d+), y=(-?\\d+)".toRegex()

        fun process(targetRow: Int): List<IntRange> {
            val ranges = arrayListOf<Pair<Int, Int>>()
            input.mapNotNull { regex.matchEntire(it)?.groupValues?.drop(1)?.map { it.toInt() } }
                .forEach { (sx, sy, bx, by) ->
                    val distance = (sx - bx).absoluteValue + (sy - by).absoluteValue

                    if (targetRow in sy - distance..sy + distance) {
                        val xWidth = distance - (sy - targetRow).absoluteValue
                        ranges += sx - xWidth to sx + xWidth
                    }
                }
            if (ranges.isEmpty()) {
                return emptyList()
            }

            val sorted = ranges.sortedWith(compareBy<Pair<Int, Int>> { it.first }.thenBy { it.second })

            val preparedRanges = arrayListOf<IntRange>()

            var currentLeft = sorted.first().first
            var currentRight = sorted.first().first

            sorted.forEach { (left, right) ->
                if (left > currentRight + 1) {
                    preparedRanges += currentLeft..currentRight
                    currentLeft = left
                    currentRight = left
                }

                if (right > currentRight) {
                    currentRight = right
                }
            }
            preparedRanges += currentLeft..currentRight

            return preparedRanges
        }

        part1Result = process(2000000).sumOf { it.last - it.first }

        for (row in 0..4000000) {
            val ranges = process(row)

            val filteredRanges = ranges.filter { it.last >= 0 && it.first <= 4000000 }

            if (filteredRanges.size > 1) {
                part2Result = (filteredRanges[0].last + 1).toLong() * 4000000L + row.toLong()
                break
            }
        }
    }
}