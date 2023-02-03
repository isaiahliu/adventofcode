package y2018

import util.input
import java.util.*

fun main() {
    val NORTH = 1 shl 1
    val EAST = 1 shl 2
    val SOUTH = 1 shl 3
    val WEST = 1 shl 4

    class TreeNode(var rowIndex: Int, var columnIndex: Int) {
        var length: Int = 0

        var popWithParent = false

        var children = arrayListOf<TreeNode>()

        val maxLength: Int get() = length + (children.maxOfOrNull { it.maxLength } ?: 0)

        val sum: Int get() = length + children.sumOf { it.sum }
    }

    input.map { it.replace("^", "(").replace("$", ")") }.forEach {
        val root = TreeNode(0, 0)

        val nodeStack = Stack<TreeNode>()

        var current = root

        val map = hashMapOf(0 to 0 to 0)

        it.forEach {
            when (it) {
                '(' -> {
                    nodeStack.push(current)

                    current = TreeNode(current.rowIndex, current.columnIndex)
                    nodeStack.peek().children += current
                }

                ')' -> {
                    if (current.length == 0 && !current.popWithParent) {
                        nodeStack.peek().children.forEach { it.length /= 2 }

                        current.popWithParent = true
                    } else {
                        while (current.popWithParent) {
                            current = nodeStack.pop()
                        }

                        current = nodeStack.pop()
                    }
                }

                '|' -> {
                    while (current.popWithParent) {
                        current = nodeStack.pop()
                    }
                    current = TreeNode(nodeStack.peek().rowIndex, nodeStack.peek().columnIndex)
                    nodeStack.peek().children += current
                }

                'W' -> {
                    map[current.rowIndex to current.columnIndex] = (map[current.rowIndex to current.columnIndex]
                        ?: 0) or WEST
                    current.columnIndex--
                    current.length++

                    map[current.rowIndex to current.columnIndex] = (map[current.rowIndex to current.columnIndex]
                        ?: 0) or EAST
                }

                'S' -> {
                    map[current.rowIndex to current.columnIndex] = (map[current.rowIndex to current.columnIndex]
                        ?: 0) or SOUTH
                    current.rowIndex++
                    current.length++
                    map[current.rowIndex to current.columnIndex] = (map[current.rowIndex to current.columnIndex]
                        ?: 0) or NORTH
                }

                'N' -> {
                    map[current.rowIndex to current.columnIndex] = (map[current.rowIndex to current.columnIndex]
                        ?: 0) or NORTH
                    current.rowIndex--
                    current.length++
                    map[current.rowIndex to current.columnIndex] = (map[current.rowIndex to current.columnIndex]
                        ?: 0) or SOUTH
                }

                'E' -> {
                    map[current.rowIndex to current.columnIndex] = (map[current.rowIndex to current.columnIndex]
                        ?: 0) or EAST
                    current.columnIndex++
                    current.length++
                    map[current.rowIndex to current.columnIndex] = (map[current.rowIndex to current.columnIndex]
                        ?: 0) or WEST
                }
            }
        }

        val minRowIndex = map.keys.minOf { it.first }
        val minColumnIndex = map.keys.minOf { it.second }

        val distances = Array(map.keys.maxOf { it.first } - minRowIndex + 1) {
            IntArray(map.keys.maxOf { it.second } - minColumnIndex + 1) { Int.MAX_VALUE }
        }

        fun walk(num: Int, rowIndex: Int, columnIndex: Int) {
            if (distances[rowIndex - minRowIndex][columnIndex - minColumnIndex] < num) {
                return
            }
            distances[rowIndex - minRowIndex][columnIndex - minColumnIndex] = num

            val direction = map[rowIndex to columnIndex] ?: 0
            if ((direction and NORTH) > 0) {
                walk(num + 1, rowIndex - 1, columnIndex)
            }

            if ((direction and EAST) > 0) {
                walk(num + 1, rowIndex, columnIndex + 1)
            }

            if ((direction and WEST) > 0) {
                walk(num + 1, rowIndex, columnIndex - 1)
            }

            if ((direction and SOUTH) > 0) {
                walk(num + 1, rowIndex + 1, columnIndex)
            }
        }

        walk(0, 0, 0)

        println(distances.maxOf { it.max() })
        println(distances.sumOf { it.count { it >= 1000 } })
    }
}