package y2016

import util.input

fun main() {
    val ranges = input.map { it.split("-") }.map { IpRange(it[0].toLong(), it[1].toLong()) }.sortedWith(compareBy<IpRange> { it.min }.thenBy { it.max })

    var (min, max) = ranges.first()
    var part1Result = min - 1

    var part2Result = min

    for (range in ranges) {
        if (part1Result < 0 && range.min > max + 1) {
            part1Result = max + 1
        }

        when {
            range.min > max -> {
                part2Result += range.min - max - 1

                max = range.max
            }

            else -> {
                max = max.coerceAtLeast(range.max)
            }
        }
    }

    part2Result += 4294967295 - max

    println(part1Result)
    println(part2Result)
}

private data class IpRange(var min: Long, var max: Long)