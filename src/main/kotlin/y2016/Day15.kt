package y2016

import util.input

fun main() {
    val regex = "Disc #\\d+ has (\\d+) positions; at time=(\\d+), it is at position (\\d+).".toRegex()

    val discs = input.map {
        val match = regex.matchEntire(it) ?: return

        val (positionStr, time, current) = match.groupValues.drop(1)

        val position = positionStr.toInt()

        position to (current.toInt() - time.toInt()).mod(position)
    }

    fun process(discs: List<Pair<Int, Int>>): Int {
        var startTime = 0

        while (true) {
            if (discs.filterIndexed { discIndex, pair ->
                    val passedSeconds = startTime + discIndex + 1

                    (pair.second + passedSeconds) % pair.first > 0
                }.isEmpty()) {
                return startTime
            }

            startTime++
        }
    }
    println(process(discs))

    println(process(buildList {
        addAll(discs)
        add(11 to 0)
    }))
}

private class Disc(val position: Int)