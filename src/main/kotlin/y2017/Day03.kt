package y2017

import util.input
import kotlin.math.absoluteValue
import kotlin.math.sqrt

fun main() {
    val target = input.first().toInt()

    var part1Result = 0

    val temp = sqrt(target.toDouble())

    val size = (temp.toInt() + if (temp - temp.toInt() > 0) {
        1
    } else {
        0
    }) / 2 * 2 + 1

    if (size > 1) {
        var remaining = target

        remaining -= (size - 2) * (size - 2)

        var x = 0
        var y = 0

        x = size / 2
        for (yIndex in size / 2 - 1 downTo 0 - size / 2) {
            remaining--

            if (remaining == 0) {
                part1Result = x.absoluteValue + yIndex.absoluteValue
                break
            }
        }

        if (remaining > 0) {
            y = size / 2
            for (xIndex in size / 2 - 1 downTo 0 - size / 2) {
                remaining--

                if (remaining == 0) {
                    part1Result = y.absoluteValue + xIndex.absoluteValue
                    break
                }
            }
        }

        if (remaining > 0) {
            x = 0 - size / 2
            for (yIndex in 1 - size / 2..size / 2) {
                remaining--

                if (remaining == 0) {
                    part1Result = x.absoluteValue + yIndex.absoluteValue
                    break
                }
            }
        }

        if (remaining > 0) {
            y = 0 - size / 2
            for (xIndex in 1 - size / 2..size / 2) {
                remaining--

                if (remaining == 0) {
                    part1Result = y.absoluteValue + xIndex.absoluteValue
                    break
                }
            }
        }
    }

    println(part1Result)

    var part2Result = 1

    val part2Array = Array(size) { IntArray(size) }
    fun sumNumbersAround(x: Int, y: Int): Int {
        part2Array[x][y] = (part2Array.getOrNull(x - 1)?.getOrNull(y - 1) ?: 0) +
                (part2Array.getOrNull(x - 1)?.getOrNull(y) ?: 0) +
                (part2Array.getOrNull(x - 1)?.getOrNull(y + 1) ?: 0) +
                (part2Array.getOrNull(x)?.getOrNull(y - 1) ?: 0) +
                (part2Array.getOrNull(x)?.getOrNull(y) ?: 0) +
                (part2Array.getOrNull(x)?.getOrNull(y + 1) ?: 0) +
                (part2Array.getOrNull(x + 1)?.getOrNull(y - 1) ?: 0) +
                (part2Array.getOrNull(x + 1)?.getOrNull(y) ?: 0) +
                (part2Array.getOrNull(x + 1)?.getOrNull(y + 1) ?: 0)

        return part2Array[x][y]
    }


    var x = size / 2
    var y = size / 2
    part2Array[x][y] = 1

    x++
    var direction = 0
    var found = false
    while (!found) {
        when (direction) {
            0 -> {
                while (part2Array[x - 1][y] > 0) {
                    val sum = sumNumbersAround(x, y)
                    if (sum > target) {
                        part2Result = sum
                        found = true
                        break
                    }
                    y++
                }
            }

            1 -> {
                while (part2Array[x][y - 1] > 0) {
                    val sum = sumNumbersAround(x, y)
                    if (sum > target) {
                        part2Result = sum
                        found = true
                        break
                    }
                    x--
                }
            }

            2 -> {
                while (part2Array[x + 1][y] > 0) {
                    val sum = sumNumbersAround(x, y)
                    if (sum > target) {
                        part2Result = sum
                        found = true
                        break
                    }
                    y--
                }
            }

            3 -> {
                while (part2Array[x][y + 1] > 0) {
                    val sum = sumNumbersAround(x, y)
                    if (sum > target) {
                        part2Result = sum
                        found = true
                        break
                    }
                    x++
                }
            }
        }

        direction = (direction + 1) % 4
    }

    println(part2Result)
}