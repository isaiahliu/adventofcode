package y2025

import util.expect
import util.input

fun main() {
    expect(0, "Merry Christmas!") {
        var index = 0
        val regex = "(\\d+)x(\\d+): (.*)".toRegex()

        var gridIndex = 0

        val nodes = hashMapOf<Array<BooleanArray>, Int>()

        while (index < input.size) {
            val line = input[index++]
            when {
                line.endsWith(":") -> {
                    arrayOf(0..2 to 0..2, 0..2 to (2 downTo 0), 2 downTo 0 to 0..2, 2 downTo 0 to (2 downTo 0)).forEach {
                        val (r1, r2) = it

                        val node1 = Array(3) { BooleanArray(3) }
                        val node2 = Array(3) { BooleanArray(3) }
                        r1.forEachIndexed { i1, p1 ->
                            r2.forEachIndexed { i2, p2 ->
                                if (input[p1 + index][p2] == '#') {
                                    node1[i1][i2] = true
                                    node2[i2][i1] = true
                                }
                            }
                        }

                        nodes[node1] = gridIndex
                        nodes[node2] = gridIndex
                    }

                    gridIndex++
                    index += 3
                }

                line.isBlank() -> Unit
                else -> {
                    regex.matchEntire(line)?.groupValues?.drop(1)?.also { (w, h, c) ->
                        val height = h.toInt()
                        val width = w.toInt()
                        val board = Array(height) { BooleanArray(width) }
                        val counts = c.split(" ").map { it.toInt() }.toIntArray()
                        var remainingSizes = counts.indices.sumOf { nodeIndex -> counts[nodeIndex] * nodes.entries.first { it.value == nodeIndex }.key.sumOf { it.count { it } } }

                        fun match(node: Array<BooleanArray>, row: Int, column: Int): Boolean {
                            return node.indices.all { rowIndex ->
                                node[rowIndex].indices.all { columnIndex ->
                                    !node[rowIndex][columnIndex] || board.getOrNull(row + rowIndex)?.getOrNull(column + columnIndex) == false
                                }
                            }
                        }

                        var remainingSpaces = width * height

                        fun use(node: Array<BooleanArray>, row: Int, column: Int) {
                            nodes[node]?.also {
                                counts[it]--
                            }

                            node.forEachIndexed { rowIndex, r ->
                                r.forEachIndexed { columnIndex, value ->
                                    if (value) {
                                        board[row + rowIndex][column + columnIndex] = true
                                        remainingSizes--
                                    }
                                }
                            }
                        }

                        fun unuse(node: Array<BooleanArray>, row: Int, column: Int) {
                            nodes[node]?.also {
                                counts[it]++
                            }

                            node.forEachIndexed { rowIndex, r ->
                                r.forEachIndexed { columnIndex, value ->
                                    if (value) {
                                        board[row + rowIndex][column + columnIndex] = false
                                        remainingSizes++
                                    }
                                }
                            }
                        }

                        fun dfs(row: Int, column: Int): Boolean {
                            return when {
                                remainingSpaces < remainingSizes -> false
                                counts.all { it == 0 } -> true
                                row + column > width + height - 2 -> false
                                row < 0 || column == width -> {
                                    val sum = row + column + 1
                                    val r = minOf(sum, height - 1)
                                    val c = sum - r

                                    dfs(r, c)
                                }

                                else -> {
                                    nodes.keys.filter { nodes[it]?.takeIf { counts[it] > 0 } != null && match(it, row, column) }.forEach {
                                        use(it, row, column)

                                        if (board[row][column]) {
                                            remainingSpaces--
                                        }
                                        if (dfs(row - 1, column + 1)) {
                                            return true
                                        }

                                        if (board[row][column]) {
                                            remainingSpaces++
                                        }

                                        unuse(it, row, column)
                                    }

                                    if (board[row][column]) {
                                        remainingSpaces--
                                    }
                                    dfs(row - 1, column + 1).also {
                                        if (board[row][column]) {
                                            remainingSpaces++
                                        }
                                    }
                                }
                            }
                        }

                        if (dfs(0, 0)) {
                            part1Result++
                        }
                    }
                }
            }
        }
    }
}
