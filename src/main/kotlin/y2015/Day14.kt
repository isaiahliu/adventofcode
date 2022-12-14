package y2015

import input

fun main() {
    val regex = "\\w+ can fly (\\d+) km/s for (\\d+) seconds, but then must rest for (\\d+) seconds.".toRegex()

    val reindeers = arrayListOf<Reindeer>()

    input.forEach {
        val match = regex.matchEntire(it) ?: return@forEach

        val speed = match.groupValues[1].toInt()
        val flySeconds = match.groupValues[2].toInt()
        val restSeconds = match.groupValues[3].toInt()

        reindeers += Reindeer(speed, flySeconds, restSeconds)
    }

    repeat(2503) {
        reindeers.forEach { it.nextSecond() }

        val lead = reindeers.maxOf { it.distance }

        reindeers.filter { it.distance == lead }.forEach { it.point++ }
    }

    val part1Result = reindeers.maxOf { it.distance }
    val part2Result = reindeers.maxOf { it.point }

    println(part1Result)
    println(part2Result)
}

private class Reindeer(private val speed: Int, private val maxFlySeconds: Int, private val maxRestSeconds: Int) {
    private var flySeconds = maxFlySeconds

    private var restSeconds = maxRestSeconds

    private var resting = false

    var distance = 0

    var point = 0

    fun nextSecond() {
        if (resting) {
            restSeconds--

            if (restSeconds == 0) {
                flySeconds = maxFlySeconds
                resting = false
            }
        } else {
            distance += speed
            flySeconds--

            if (flySeconds == 0) {
                restSeconds = maxRestSeconds
                resting = true
            }
        }
    }
}