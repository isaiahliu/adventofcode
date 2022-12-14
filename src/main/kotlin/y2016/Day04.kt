package y2016

import util.input

fun main() {
    val regex = "(.*)-(\\d+)\\[(.*)\\]".toRegex()

    var part1Result = 0
    var part2Result = 0

    val alphabets = ('a'..'z').map { it }.toCharArray()

    input.forEach {
        val match = regex.matchEntire(it) ?: return

        val (name, roomIdStr, checksum) = match.groupValues.drop(1)

        val charsCount = name.filter {
            it in 'a'..'z'
        }.groupingBy { it }.eachCount().toMutableMap()

        var min = Int.MAX_VALUE
        checksum.forEach {
            val count = charsCount.remove(it) ?: 0

            min = min.coerceAtMost(count)
        }

        val roomId = roomIdStr.toInt()
        if (charsCount.none { it.value > min }) {
            part1Result += roomId
        }

        if (String(name.map {
                    if (it in 'a'..'z') alphabets[((it - 'a') + roomId) % 26] else ' '
                }.toCharArray()) == "northpole object storage") {
            part2Result = roomId
        }
    }

    println(part1Result)
    println(part2Result)
}