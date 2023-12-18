package y2022

import util.expectInt
import util.input

fun main() {
    expectInt {
        val regex = "(\\d+)-(\\d+),(\\d+)-(\\d+)".toRegex()
        input.forEach {
            val match = regex.matchEntire(it) ?: return@forEach

            val start1 = match.groupValues[1].toInt()
            val end1 = match.groupValues[2].toInt()
            val start2 = match.groupValues[3].toInt()
            val end2 = match.groupValues[4].toInt()

            if (start1 <= start2 && end1 >= end2 || start1 >= start2 && end1 <= end2) {
                part1Result++
            }

            if (!(end1 < start2 || end2 < start1)) {
                part2Result++
            }
        }
    }
}
