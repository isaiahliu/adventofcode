package y2022

import util.expectInt
import util.input

fun main() {
    expectInt {
        val trees = input.map {
            it.toCharArray().map { it - '0' }.toIntArray()
        }.toTypedArray()

        val visible1 = Array(trees.size) {
            IntArray(trees[it].size)
        }

        val visible2 = Array(trees.size) {
            IntArray(trees[it].size)
        }

        val width = trees[0].size
        val height = trees.size

        repeat(height) { row ->
            var currentLeft = -1
            var currentRight = -1

            repeat(width) { column ->
                trees[row][column].also {
                    if (it > currentLeft) {
                        visible1[row][column] = 1
                        currentLeft = it
                    }
                }

                trees[row][width - column - 1].also {
                    if (it > currentRight) {
                        visible1[row][width - column - 1] = 1
                        currentRight = it
                    }
                }
            }
        }

        repeat(width) { column ->
            var currentTop = -1
            var currentBottom = -1

            repeat(height) { row ->
                trees[row][column].also {
                    if (it > currentTop) {
                        visible1[row][column] = 1
                        currentTop = it
                    }
                }

                trees[height - row - 1][column].also {
                    if (it > currentBottom) {
                        visible1[height - row - 1][column] = 1
                        currentBottom = it
                    }
                }
            }
        }

        part1Result = visible1.sumOf { it.sum() }

        for (row in 1 until width - 1) {
            for (column in 1 until height - 1) {
                val current = trees[row][column]

                var upCount = 0
                for (up in row - 1 downTo 0) {
                    upCount++

                    if (trees[up][column] >= current) {
                        break
                    }
                }

                var downCount = 0
                for (down in row + 1 until height) {
                    downCount++

                    if (trees[down][column] >= current) {
                        break
                    }
                }

                var leftCount = 0
                for (left in column - 1 downTo 0) {
                    leftCount++

                    if (trees[row][left] >= current) {
                        break
                    }
                }

                var rightCount = 0
                for (right in column + 1 until width) {
                    rightCount++

                    if (trees[row][right] >= current) {
                        break
                    }
                }

                visible2[row][column] = upCount * downCount * leftCount * rightCount
            }
        }

        part2Result = visible2.maxOf { it.max() }
    }
}