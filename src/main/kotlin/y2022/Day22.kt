package y2022

import util.input
import kotlin.math.sqrt

fun main() {
    val RIGHT = 0
    val DOWN = 1
    val LEFT = 2
    val UP = 3

    val mapInput = input.dropLast(2)

    val map = Array(mapInput.size) {
        IntArray(mapInput.maxOf { it.length }) {
            -1
        }
    }

    operator fun Array<IntArray>.get(rowIndex: Int, columnIndex: Int): Int {
        return this.getOrNull(rowIndex)?.getOrNull(columnIndex) ?: -1
    }

    mapInput.forEachIndexed { rowIndex, s ->
        val row = map[rowIndex]
        s.forEachIndexed { columnIndex, c ->
            when (c) {
                '.' -> {
                    row[columnIndex] = 0
                }

                '#' -> {
                    row[columnIndex] = 999
                }
            }
        }
    }

    val cubeSize = sqrt(map.sumOf { it.count { it >= 0 } } / 6.0).toInt()

    fun process(cubeMode: Boolean) {
        //R D L T
        var direction = 0
        var rowIndex = 0
        var columnIndex = map[0].indexOfFirst { it != -1 }

        fun move(steps: Int) {
            repeat(steps) {
                var nextRow: Int = rowIndex
                var nextColumn: Int = columnIndex
                var nextDirection = direction

                when (direction) {
                    RIGHT -> {
                        nextColumn++
                    }

                    DOWN -> {
                        nextRow++
                    }

                    LEFT -> {
                        nextColumn--
                    }

                    UP -> {
                        nextRow--
                    }
                }

                var current = map[nextRow, nextColumn]

                if (current == -1) {
                    if (cubeMode) {
                        val cubeRowIndex = rowIndex / cubeSize
                        val cubeColumnIndex = columnIndex / cubeSize

                        val rowIndexOfCube = rowIndex % cubeSize
                        val rowIndexOfCubeReversed = cubeSize - rowIndex % cubeSize - 1

                        val columnIndexOfCube = columnIndex % cubeSize
                        val columnIndexOfCubeReversed = cubeSize - columnIndex % cubeSize - 1

                        when (direction) {
                            RIGHT -> {
//                                nextRow = (cubeRowIndex + 1) * cubeSize
//                                nextColumn = (cubeColumnIndex + 1) * cubeSize + rowIndexOfCubeReversed
//                                nextDirection = DOWN

                                //3
                                if (cubeRowIndex == 0 && cubeColumnIndex == 2) {
                                    nextRow = (cubeRowIndex + 2) * cubeSize + rowIndexOfCubeReversed
                                    nextColumn = (cubeColumnIndex + 0) * cubeSize - 1
                                    nextDirection = LEFT
                                }

                                //3
                                if (cubeRowIndex == 2 && cubeColumnIndex == 1) {
                                    nextRow = (cubeRowIndex - 2) * cubeSize + rowIndexOfCubeReversed
                                    nextColumn = (cubeColumnIndex + 2) * cubeSize - 1
                                    nextDirection = LEFT
                                }

                                //4,5
                                if (cubeRowIndex == 1 && cubeColumnIndex == 1 || cubeRowIndex == 3 && cubeColumnIndex == 0) {
                                    nextRow = (cubeRowIndex + 0) * cubeSize - 1
                                    nextColumn = (cubeColumnIndex + 1) * cubeSize + rowIndexOfCube
                                    nextDirection = UP
                                }

                                if (map[nextRow, nextColumn] == -1) {
                                    println("failed on right ${rowIndex},${columnIndex}")
                                } else {
                                    println("${rowIndex}, ${columnIndex} Jump(${direction}) to ${nextRow}, ${nextColumn} (${nextDirection})")
                                }
                            }

                            DOWN -> {
//                                nextRow = cubeRowIndex * cubeSize - 1
//                                nextColumn = (cubeColumnIndex - 2) * cubeSize + columnIndexOfCubeReversed
//                                nextDirection = UP
                                //2
                                if (cubeRowIndex == 3 && cubeColumnIndex == 0) {
                                    nextRow = (cubeRowIndex - 3) * cubeSize
                                    nextColumn = (cubeColumnIndex + 2) * cubeSize + columnIndexOfCube
                                    nextDirection = DOWN
                                }

                                //4,5
                                if (cubeRowIndex == 0 && cubeColumnIndex == 2 || cubeRowIndex == 2 && cubeColumnIndex == 1) {
                                    nextRow = (cubeRowIndex + 1) * cubeSize + columnIndexOfCube
                                    nextColumn = (cubeColumnIndex + 0) * cubeSize - 1
                                    nextDirection = LEFT
                                }

                                if (map[nextRow, nextColumn] == -1) {
                                    println("failed on down ${rowIndex},${columnIndex}")
                                } else {
                                    println("${rowIndex}, ${columnIndex} Jump(${direction}) to ${nextRow}, ${nextColumn} (${nextDirection})")
                                }
                            }

                            LEFT -> {
                                //1
                                if (cubeRowIndex == 3 && cubeColumnIndex == 0) {
                                    nextRow = (cubeRowIndex - 3) * cubeSize
                                    nextColumn = (cubeColumnIndex + 1) * cubeSize + rowIndexOfCube
                                    nextDirection = DOWN
                                }

                                //6
                                if (cubeRowIndex == 0 && cubeColumnIndex == 1) {
                                    nextRow = (cubeRowIndex + 2) * cubeSize + rowIndexOfCubeReversed
                                    nextColumn = (cubeColumnIndex - 1) * cubeSize
                                    nextDirection = RIGHT
                                }

                                //6
                                if (cubeRowIndex == 2 && cubeColumnIndex == 0) {
                                    nextRow = (cubeRowIndex - 2) * cubeSize + rowIndexOfCubeReversed
                                    nextColumn = (cubeColumnIndex + 1) * cubeSize
                                    nextDirection = RIGHT
                                }

                                //7
                                if (cubeRowIndex == 1 && cubeColumnIndex == 1) {
                                    nextRow = (cubeRowIndex + 1) * cubeSize
                                    nextColumn = (cubeColumnIndex - 1) * cubeSize + rowIndexOfCube
                                    nextDirection = DOWN
                                }

                                if (map[nextRow, nextColumn] == -1) {
                                    println("failed on left ${rowIndex},${columnIndex}")
                                } else {
                                    println("${rowIndex}, ${columnIndex} Jump(${direction}) to ${nextRow}, ${nextColumn} (${nextDirection})")
                                }
                            }

                            UP -> {
//                                nextRow = (cubeRowIndex - 1) * cubeSize + columnIndexOfCube
//                                nextColumn = (cubeColumnIndex + 1) * cubeSize
//                                nextDirection = RIGHT

                                //1
                                if (cubeRowIndex == 0 && cubeColumnIndex == 1) {
                                    nextRow = (cubeRowIndex + 3) * cubeSize + columnIndexOfCube
                                    nextColumn = (cubeColumnIndex - 1) * cubeSize
                                    nextDirection = RIGHT
                                }

                                //2
                                if (cubeRowIndex == 0 && cubeColumnIndex == 2) {
                                    nextRow = (cubeRowIndex + 4) * cubeSize - 1
                                    nextColumn = (cubeColumnIndex - 2) * cubeSize + columnIndexOfCube
                                    nextDirection = UP
                                }

                                //7
                                if (cubeRowIndex == 2 && cubeColumnIndex == 0) {
                                    nextRow = (cubeRowIndex - 1) * cubeSize + columnIndexOfCube
                                    nextColumn = (cubeColumnIndex + 1) * cubeSize
                                    nextDirection = RIGHT
                                }

                                if (map[nextRow, nextColumn] == -1) {
                                    println("failed on up ${rowIndex},${columnIndex}")
                                } else {
                                    println("${rowIndex}, ${columnIndex} Jump(${direction}) to ${nextRow}, ${nextColumn} (${nextDirection})")
                                }
                            }
                        }
                    } else {
                        var next = map[rowIndex, columnIndex]
                        while (next != -1) {
                            when (direction) {
                                0 -> {
                                    nextColumn--
                                    next = map[nextRow, nextColumn - 1]
                                }

                                1 -> {
                                    nextRow--
                                    next = map[nextRow - 1, nextColumn]
                                }

                                2 -> {
                                    nextColumn++
                                    next = map[nextRow, nextColumn + 1]
                                }

                                3 -> {
                                    nextRow++
                                    next = map[nextRow + 1, nextColumn]
                                }
                            }
                        }
                    }

                    current = map[nextRow, nextColumn]
                }

                if (current == 0) {
                    rowIndex = nextRow
                    columnIndex = nextColumn
                    direction = nextDirection
                } else {
                    return
                }
            }
        }

        input.last().replace("L", "_L_").replace("R", "_R_").replace("__", "_").split("_").forEach {
            when (it) {
                "L" -> {
                    direction = (direction - 1).mod(4)
                }

                "R" -> {
                    direction = (direction + 1).mod(4)
                }

                else -> {
                    move(it.toInt())
//                println("${rowIndex} -- ${columnIndex}")
                }
            }
        }
        println((rowIndex + 1) * 1000 + (columnIndex + 1) * 4 + direction)
    }

//    process(false)
    process(true)
}

