package y2021

import util.input
import kotlin.math.absoluteValue

fun main() {
    val scannedBeaconsList = arrayListOf<MutableList<Pair<Pair<Int, Int>, Int>>>()

    input.forEach {
        when {
            it.isBlank() -> {}
            it.startsWith("---") -> {
                scannedBeaconsList.add(arrayListOf())
            }

            else -> {
                scannedBeaconsList.last() += it.split(",").map { it.toInt() }.let { it[0] to it[1] to it[2] }
            }
        }
    }

    val matchBeaconsList = arrayListOf(
        scannedBeaconsList.first()
            .sortedWith(compareBy<Pair<Pair<Int, Int>, Int>> { it.first.first }.thenBy { it.first.second }
                .thenBy { it.second })
    )

    val remainingBeaconsList = scannedBeaconsList.drop(1).mapIndexed { index, pairs ->
        index to pairs
    }.toMutableList()

    val scanners = arrayListOf(0 to 0 to 0)

    val directions = arrayOf(
        intArrayOf(1, 2, 3),
        intArrayOf(2, -1, 3),
        intArrayOf(-1, -2, 3),
        intArrayOf(-2, 1, 3),
        intArrayOf(3, 2, -1),
        intArrayOf(-3, 2, 1),
    ).map { (x, y, z) ->
        listOf(
            intArrayOf(x, y, z),
            intArrayOf(x, z, -y),
            intArrayOf(x, -y, -z),
            intArrayOf(x, -z, y),
        )
    }.flatten().map { it[0] to it[1] to it[2] }

    fun Pair<Pair<Int, Int>, Int>.adjust(direction: Pair<Pair<Int, Int>, Int>): Pair<Pair<Int, Int>, Int> {
        val d = intArrayOf(direction.first.first, direction.first.second, direction.second)
        return this.let { intArrayOf(it.first.first, it.first.second, it.second) }.let { arr ->
            IntArray(3) {
                d[it].let { arr[it.absoluteValue - 1] * (it / it.absoluteValue) }
            }
        }.let { it[0] to it[1] to it[2] }
    }

    operator fun Pair<Pair<Int, Int>, Int>.plus(target: Pair<Pair<Int, Int>, Int>): Pair<Pair<Int, Int>, Int> {
        return first.first + target.first.first to first.second + target.first.second to second + target.second
    }

    operator fun Pair<Pair<Int, Int>, Int>.minus(target: Pair<Pair<Int, Int>, Int>): Pair<Pair<Int, Int>, Int> {
        return first.first - target.first.first to first.second - target.first.second to second - target.second
    }

    val cache = Array(remainingBeaconsList.size) {
        hashMapOf<Pair<Pair<Int, Int>, Int>, List<Pair<Pair<Int, Int>, Int>>>()
    }

    fun List<Pair<Pair<Int, Int>, Int>>.adjust(
        index: Int, direction: Pair<Pair<Int, Int>, Int>
    ): List<Pair<Pair<Int, Int>, Int>> {
        return cache[index].computeIfAbsent(direction) {
            map { it.adjust(direction) }.sortedWith(compareBy<Pair<Pair<Int, Int>, Int>> { it.first.first }.thenBy { it.first.second }
                .thenBy { it.second })
        }
    }

    done@ while (remainingBeaconsList.isNotEmpty()) {
        for ((index, remainingBeacons) in remainingBeaconsList) {
            for (matchBeacons in matchBeaconsList) {
                for (direction in directions) {
                    val adjustedBeacons = remainingBeacons.adjust(index, direction)

                    for (x in adjustedBeacons) {
                        for (y in matchBeacons) {
                            val diff = y - x

                            val t = adjustedBeacons.map { it + diff }

                            if (t.count { it in matchBeacons } >= 12) {
                                matchBeaconsList += t
                                remainingBeaconsList.removeIf { it.first == index }
                                scanners += diff
                                continue@done
                            }
                        }
                    }
                }
            }
        }
    }

    println(matchBeaconsList.flatten().toSet().size)

    var result2 = 0
    for (x in 0 until scanners.size) {
        for (y in x + 1 until scanners.size) {
            val distance =
                (scanners[x].first.first - scanners[y].first.first).absoluteValue + (scanners[x].first.second - scanners[y].first.second).absoluteValue + (scanners[x].second - scanners[y].second).absoluteValue

            result2 = result2.coerceAtLeast(distance)
        }
    }

    println(result2)
}