package y2025

import util.expect
import util.input
import kotlin.math.sign

fun main() {
    expect(0, 0) {
        var index = 0
        val regex = "(\\d+)x(\\d+): (.*)".toRegex()

        var gridIndex = 0

        val marks = Array(62) {
            1L shl (it + 1) - 1
        }
        val nodes = hashMapOf<Long, Int>()
        while (index < input.size) {
            val line = input[index++]
            when {
                line.endsWith(":") -> {
                    arrayOf(0..2 to 0..2, 0..2 to (2 downTo 0), 2 downTo 0 to 0..2, 2 downTo 0 to (2 downTo 0)).forEach {
                        val (r1, r2) = it

                        var node1 = 0L
                        var node2 = 0L
                        r1.forEachIndexed { i1, p1 ->
                            r2.forEachIndexed { i2, p2 ->
                                if (input[p1 + index][p2] == '#') {
                                    node1 += (1L shl i2) shl (i1 * 3)
                                    node2 += (1L shl i1) shl (i2 * 3)
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
                        val board = LongArray(height)
                        val counts = c.split(" ").map { it.toInt() }.toIntArray()
                        val sizes = IntArray(counts.size) { nodeIndex ->
                            nodes.entries.first { it.value == nodeIndex }.key.countOneBits()
                        }

                        fun match(node: Long, row: Int, column: Int): Boolean {
                            val r1 = board.getOrNull(row)?.let { it shr column }?.let { it and 0b111 } ?: return false
                            val r2 = board.getOrNull(row + 1)?.let { it shr column }?.let { it and 0b111 } ?: return false
                            val r3 = board.getOrNull(row + 2)?.let { it shr column }?.let { it and 0b111 } ?: return false

                            return (node and r1 == 0L) && ((node shr 3) and r2 == 0L) && ((node shr 6) and r3 == 0L)
                        }

                        var remainingSpaces = width * height
                        var remainingSizes = counts.indices.sumOf { counts[it] * sizes[it] }

                        fun use(node: Long, row: Int, column: Int) {
                            nodes[node]?.also {
                                counts[it]--
                            }

                            board[row] += (node and 0b111) shl column
                            board[row + 1] += ((node shr 3) and 0b111) shl column
                            board[row + 2] += ((node shr 6) and 0b111) shl column
                            remainingSizes -= node.countOneBits()
                        }

                        fun unuse(node: Long, row: Int, column: Int) {
                            nodes[node]?.also {
                                counts[it]--
                            }

                            board[row] -= (node and 0b111) shl column
                            board[row + 1] -= ((node shr 3) and 0b111) shl column
                            board[row + 2] -= ((node shr 6) and 0b111) shl column
                            remainingSizes += node.countOneBits()
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

                                        remainingSpaces -= (board[row] and (1L shl column)).sign
                                        if (dfs(row - 1, column + 1)) {
                                            return true
                                        }
                                        remainingSpaces += (board[row] and (1L shl column)).sign
                                        unuse(it, row, column)
                                    }

                                    remainingSpaces -= (board[row] and (1L shl column)).sign
                                    dfs(row - 1, column + 1).also {
                                        remainingSpaces += (board[row] and (1L shl column)).sign
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
