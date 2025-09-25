package y2016

import util.expect
import util.input
import java.util.*

fun main() {
    expect(0, 0) {
        val pointsMap = hashMapOf<Int, Pair<Int, Int>>()

        val map = Array(input.size) { rowIndex ->
            val row = input[rowIndex]
            BooleanArray(row.length) { columnIndex ->
                when (row[columnIndex]) {
                    '#' -> false
                    '.' -> true
                    else -> {
                        pointsMap[row[columnIndex] - '0'] = rowIndex to columnIndex
                        true
                    }
                }
            }
        }

        val points = Array(pointsMap.size) {
            pointsMap[it] ?: throw Exception("Error")
        }

        val distances = Array(points.size) {
            IntArray(points.size)
        }

        val neighbors = setOf(-1 to 0, 1 to 0, 0 to -1, 0 to 1)

        for (i in points.indices) {
            val target = points[i]
            for (j in i + 1 until points.size) {
                val visitMap = Array(map.size) { r ->
                    Array(map[r].size) { -1 }
                }
                visitMap[points[j].first][points[j].second] = 0
                val tasks = LinkedList<Pair<Int, Int>>()
                tasks.add(points[j])

                while (visitMap[target.first][target.second] < 0) {
                    repeat(tasks.size) {
                        val (r, c) = tasks.poll()

                        neighbors.forEach { (dr, dc) ->
                            val newR = r + dr
                            val newC = c + dc

                            if (map.getOrNull(newR)?.getOrNull(newC) == true && visitMap[newR][newC] < 0) {
                                visitMap[newR][newC] = visitMap[r][c] + 1
                                tasks.add(newR to newC)
                            }
                        }
                    }
                }

                distances[i][j] = visitMap[target.first][target.second]
                distances[j][i] = visitMap[target.first][target.second]
            }
        }

        val dp1 = Array(points.size) { IntArray(1 shl points.size) { Int.MAX_VALUE } }
        val dp2 = Array(points.size) { IntArray(1 shl points.size) { Int.MAX_VALUE } }
        repeat(points.size) {
            dp1[it][1 shl it] = 0
            dp2[it][1 shl it] = distances[0][it]
        }

        fun Int.forEachBit(consumer: (Int) -> Unit) {
            var t = this

            var index = 0
            while (t > 0) {
                if (t % 2 == 1) {
                    consumer(index)
                }

                t /= 2
                index++
            }
        }

        repeat(1 shl points.size) { mark ->
            mark.forEachBit { pointIndex ->
                val otherMark = mark xor (1 shl pointIndex)

                otherMark.forEachBit { otherPointIndex ->
                    dp1[pointIndex][mark] = minOf(dp1[pointIndex][mark], dp1[otherPointIndex][otherMark] + distances[pointIndex][otherPointIndex])
                    dp2[pointIndex][mark] = minOf(dp2[pointIndex][mark], dp2[otherPointIndex][otherMark] + distances[pointIndex][otherPointIndex])
                }
            }
        }

        part1Result = dp1[0].last()
        part2Result = dp2[0].last()
    }
}