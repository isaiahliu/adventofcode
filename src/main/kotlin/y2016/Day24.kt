package y2016

import util.input

fun main() {
    val pathCache = mutableMapOf<String, Array<IntArray>>()

    fun process(returnToStart: Boolean): Int {
        var startRowIndex = 0
        var startColumnIndex = 0
        val points = arrayListOf<Pair<Int, Int>>()
        val map = Array(input.size) { rowIndex ->
            val row = input[rowIndex]
            IntArray(row.length) { columnIndex ->
                when (row[columnIndex]) {
                    '#' -> -1
                    '.' -> 0
                    '0' -> {
                        startRowIndex = rowIndex
                        startColumnIndex = columnIndex
                        0
                    }

                    else -> {
                        points += rowIndex to columnIndex
                        0
                    }
                }
            }
        }

        fun findPath(fromRowIndex: Int, fromColumnIndex: Int): Array<IntArray> {
            return pathCache.computeIfAbsent("${fromRowIndex}_${fromColumnIndex}") {
                val result = Array(map.size) { rowIndex ->
                    val row = map[rowIndex]
                    IntArray(row.size) {
                        when (map[rowIndex][it]) {
                            -1 -> -1
                            else -> Int.MAX_VALUE
                        }
                    }
                }

                result[fromRowIndex][fromColumnIndex] = 0
                val tasks = arrayListOf(fromRowIndex to fromColumnIndex)

                while (tasks.isNotEmpty()) {
                    val current = tasks.toList()
                    tasks.clear()

                    for ((rowIndex, columnIndex) in current) {
                        val currentValue = result[rowIndex][columnIndex]

                        result[rowIndex - 1][columnIndex].takeIf { currentValue + 1 < it }?.also {
                            result[rowIndex - 1][columnIndex] = currentValue + 1
                            tasks += rowIndex - 1 to columnIndex
                        }

                        result[rowIndex + 1][columnIndex].takeIf { currentValue + 1 < it }?.also {
                            result[rowIndex + 1][columnIndex] = currentValue + 1
                            tasks += rowIndex + 1 to columnIndex
                        }

                        result[rowIndex][columnIndex - 1].takeIf { currentValue + 1 < it }?.also {
                            result[rowIndex][columnIndex - 1] = currentValue + 1
                            tasks += rowIndex to columnIndex - 1
                        }

                        result[rowIndex][columnIndex + 1].takeIf { currentValue + 1 < it }?.also {
                            result[rowIndex][columnIndex + 1] = currentValue + 1
                            tasks += rowIndex to columnIndex + 1
                        }
                    }
                }

                result
            }
        }

        var minSteps = Int.MAX_VALUE
        fun walk(steps: Int, rowIndex: Int, columnIndex: Int, points: List<Pair<Int, Int>>) {
            if (points.isEmpty()) {
                var additionalSteps = 0
                if (returnToStart) {
                    additionalSteps = findPath(rowIndex, columnIndex)[startRowIndex][startColumnIndex]
                }
                minSteps = minSteps.coerceAtMost(steps + additionalSteps)
            }

            points.forEach { pair ->
                val path = findPath(rowIndex, columnIndex)

                walk(
                    steps + path[pair.first][pair.second],
                    pair.first,
                    pair.second,
                    points.toMutableList().also { it.remove(pair) })
            }
        }

        walk(0, startRowIndex, startColumnIndex, points)

        return minSteps
    }
    println(process(false))
    println(process(true))
}