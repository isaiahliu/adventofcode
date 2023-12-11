package y2023

import util.input
import kotlin.math.absoluteValue

fun main() {
    val rows = BooleanArray(input.size)
    val columns = BooleanArray(input[0].length)

    val stars = arrayListOf<Pair<Int, Int>>()
    for (r in input.indices) {
        for (c in input[r].indices) {
            if (input[r][c] == '#') {
                stars += r to c
                rows[r] = true
                columns[c] = true
            }
        }
    }

    fun calculate(emptySize: Int): Long {
        var sum = 0
        val rowSums = IntArray(input.size) {
            sum += if (rows[it]) 1 else emptySize

            sum
        }

        sum = 0
        val columnSums = IntArray(input[0].length) {
            sum += if (columns[it]) 1 else emptySize

            sum
        }

        var result = 0L
        for (i in stars.indices) {
            for (j in i + 1 until stars.size) {
                result += (rowSums[stars[j].first] - rowSums[stars[i].first]).absoluteValue
                result += (columnSums[stars[j].second] - columnSums[stars[i].second]).absoluteValue
            }
        }

        return result
    }

    val part1Result = calculate(2)
    val part2Result = calculate(1000000)

    println(part1Result)
    println(part2Result)
}
