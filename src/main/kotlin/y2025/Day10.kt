package y2025

import util.expect
import util.input
import java.util.*

fun main() {
    expect(0, 0) {
        input.forEach { line ->
            val nodes = line.split(" ")

            var target1 = 0L
            nodes[0].also {
                it.drop(1).dropLast(1).forEachIndexed { index, ch ->
                    if (ch == '#') {
                        target1 += 1L shl index
                    }
                }
            }

            val buttons = nodes.drop(1).dropLast(1).map {
                it.drop(1).dropLast(1).split(",").map { it.toInt() }.toSet()
            }.sortedByDescending { it.size }.toTypedArray()

            val visited1 = hashSetOf(0L)
            val tasks1 = LinkedList<Long>()
            tasks1 += 0L

            while (target1 !in visited1) {
                part1Result++

                repeat(tasks1.size) {
                    val number = tasks1.poll()
                    buttons.forEach { button ->
                        button.fold(number) { a, b ->
                            a xor (1L shl b)
                        }.also {
                            if (visited1.add(it)) {
                                tasks1.add(it)
                            }
                        }
                    }
                }
            }

            val matrix = nodes.last().drop(1).dropLast(1).split(",").map { it.toInt() }.map { target ->
                IntArray(buttons.size + 1).also {
                    it[buttons.size] = target
                }
            }.toTypedArray()

            buttons.forEachIndexed { buttonIndex, b ->
                b.forEach { pos ->
                    matrix[pos][buttonIndex] = 1
                }
            }

            fun mul(rowIndex: Int, m: Int) {
                matrix[rowIndex].forEachIndexed { index, i ->
                    matrix[rowIndex][index] = i * m
                }
            }

            val remainingButtonIndices = buttons.indices.reversed().toMutableList()
            val processed = IntArray(buttons.size) { -1 }
            repeat(buttons.size) { buttonIndex ->
                val indices =
                    matrix.indices.filter { rowIndex -> matrix[rowIndex][buttonIndex] != 0 }.sortedWith { i1, i2 ->
                        val r1 = matrix[i1]
                        val r2 = matrix[i2]

                        r1.forEachIndexed { index, n1 ->
                            val n2 = r2[index]
                            when {
                                n1 == n2 -> Unit
                                n1 == 0 -> return@sortedWith -1
                                n2 == 0 -> return@sortedWith 1
                                else -> return@sortedWith n1 - n2
                            }
                        }

                        i1 - i2
                    }

                if (matrix[indices[0]].indices.firstOrNull { matrix[indices[0]][it] != 0 } != buttonIndex) {
                    return@repeat
                }

                remainingButtonIndices -= buttonIndex

                if (matrix[indices[0]][buttonIndex] < 0) {
                    mul(indices[0], -1)
                }

                processed[buttonIndex] = indices[0]

                for (i in 1 until indices.size) {
                    if (matrix[indices[i]][buttonIndex] % matrix[indices[0]][buttonIndex] != 0) {
                        mul(indices[i], matrix[indices[0]][buttonIndex])
                    }
                    val a = matrix[indices[i]][buttonIndex] / matrix[indices[0]][buttonIndex]
                    matrix[indices[i]].indices.forEach { index ->
                        matrix[indices[i]][index] -= matrix[indices[0]][index] * a
                    }
                }
            }

            val remainingLast = IntArray(matrix.size) { matrixIndex ->
                remainingButtonIndices.indices.lastOrNull { matrix[matrixIndex][remainingButtonIndices[it]] != 0 } ?: 0

            }

            var result = Int.MAX_VALUE
            fun dfs(prev: Int, remainingIndex: Int) {
                when {
                    prev >= result -> Unit
                    remainingIndex < remainingButtonIndices.size -> {
                        val buttonIndex = remainingButtonIndices[remainingIndex]
                        val matchMatrixIndices = matrix.indices.filter { matrix[it][buttonIndex] != 0 }
                        var count = 0

                        var failed = false
                        while (!failed && count < result) {
                            var skip = false
                            matchMatrixIndices.forEach { matrixIndex ->
                                val row = matrix[matrixIndex]

                                row[buttons.size] -= row[buttonIndex] * count

                                when {
                                    remainingLast[matrixIndex] != remainingIndex -> Unit
                                    row[buttons.size] >= 0 -> Unit
                                    row[buttonIndex] > 0 -> failed = true
                                    else -> skip = true
                                }
                            }

                            if (!skip && !failed) {
                                dfs(prev + count, remainingIndex + 1)
                            }

                            matchMatrixIndices.forEach { matrixIndex ->
                                val row = matrix[matrixIndex]

                                row[buttons.size] += row[buttonIndex] * count
                            }

                            count++
                        }

                        result.takeIf { it < Int.MAX_VALUE }
                    }

                    else -> {
                        var count = prev
                        processed.forEachIndexed { buttonIndex, i ->
                            if (i >= 0) {
                                if (matrix[i][buttons.size] % matrix[i][buttonIndex] != 0) {
                                    return
                                }
                                count += matrix[i][buttons.size] / matrix[i][buttonIndex]
                            }
                        }
                        result = minOf(result, count)
                    }
                }
            }

            dfs(0, 0)
            part2Result += result
        }
    }
}
