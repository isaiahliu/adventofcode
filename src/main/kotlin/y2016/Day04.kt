package y2016

import util.expect
import util.input

fun main() {
    expect(0, 0) {
        val regex = "(.*)-(\\d+)\\[(.*)]".toRegex()

        input.forEach {
            val (name, roomIdStr, checksum) = regex.matchEntire(it)?.groupValues?.drop(1) ?: return@forEach

            val counts = name.groupingBy { it }.eachCount().toMutableMap()
            counts -= '-'

            var min = Int.MAX_VALUE
            checksum.forEach {
                min = minOf(min, counts.remove(it) ?: 0)
            }

            val roomId = roomIdStr.toInt()
            if (min >= counts.values.max()) {
                part1Result += roomId
            }

            val decryptedName = name.map {
                when (it) {
                    '-' -> ' '
                    else -> 'a' + (it - 'a' + roomId) % 26
                }
            }.toCharArray().concatToString()

            if (decryptedName == "northpole object storage") {
                part2Result = roomId
            }
        }
    }
}