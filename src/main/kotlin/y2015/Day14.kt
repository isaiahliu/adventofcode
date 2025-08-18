package y2015

import util.expect
import util.input

fun main() {
    expect(0, 0) {
        val regex = "\\w+ can fly (\\d+) km/s for (\\d+) seconds, but then must rest for (\\d+) seconds.".toRegex()

        val reindeers = arrayListOf<IntArray>()

        input.forEach {
            regex.matchEntire(it)?.groupValues?.drop(1)?.map { it.toInt() }?.toIntArray()?.also {
                reindeers += it
            }
        }

        val distances = IntArray(reindeers.size)
        val points = IntArray(reindeers.size)

        repeat(2503) { second ->
            var lead = 0
            reindeers.forEachIndexed { index, (speed, flySeconds, restSeconds) ->
                if (second % (flySeconds + restSeconds) < flySeconds) {
                    distances[index] += speed
                }

                lead = maxOf(lead, distances[index])
            }

            reindeers.indices.forEach {
                if (distances[it] == lead) {
                    points[it]++
                }
            }
        }

        part1Result = distances.max()
        part2Result = points.max()
    }
}

