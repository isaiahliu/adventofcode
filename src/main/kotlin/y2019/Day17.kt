package y2019

import util.input
import java.util.*
import kotlin.math.pow

fun main() {
    val UP = 0
    val RIGHT = 1
    val DOWN = 2
    val LEFT = 3

    fun process(firstNum: Long? = null): Pair<Long, ArrayList<ArrayList<Char>>> {
        var result = 0L
        var currentLine = arrayListOf<Char>()
        val map = arrayListOf(currentLine)

        val memory = input.first().let {
            it.split(",").map { it.toLong() }.toLongArray()
        }.mapIndexed { index, l ->
            index.toLong() to l
        }.toMap().toMutableMap()

        firstNum?.also {
            memory[0] = it
        }

        var rowIndex = 0
        var columnIndex = 0

        var index = 0L
        var relativeBase = 0L
        var done = false

        val inputs = LinkedList<Int>()

        fun readParam(paramIndex: Long): Long {
            return when ((memory[index]!!.toInt() / 10 / (10.0.pow(paramIndex.toInt())).toInt()) % 10) {
                0 -> memory[memory[index + paramIndex] ?: 0L] ?: 0L
                1 -> memory[index + paramIndex] ?: 0L
                else -> memory[relativeBase + (memory[index + paramIndex] ?: 0L)] ?: 0L
            }
        }

        fun writeParam(paramIndex: Long, value: Long) {
            when ((memory[index]!!.toInt() / 10 / (10.0.pow(paramIndex.toInt())).toInt()) % 10) {
                0 -> memory[memory[index + paramIndex] ?: 0L] = value
                1 -> {
                    memory[index + paramIndex] = value
                }

                else -> {
                    memory[relativeBase + (memory[index + paramIndex] ?: 0L)] = value
                }
            }
        }

        while (!done) {
            when (memory[index]!!.toInt() % 100) {
                1 -> {
                    writeParam(3, readParam(1) + readParam(2))
                    index += 4
                }

                2 -> {
                    writeParam(3, readParam(1) * readParam(2))
                    index += 4
                }

                3 -> {
                    if (inputs.isEmpty()) {
                        var direction = UP
                        val movements = arrayListOf<String>()

                        fun walk() {
                            val move = arrayOf((direction - 1).mod(4), (direction + 1).mod(4)).indexOfFirst {
                                var tempRow = rowIndex
                                var tempColumn = columnIndex
                                when (it) {
                                    UP -> {
                                        tempRow--
                                    }

                                    DOWN -> {
                                        tempRow++
                                    }

                                    RIGHT -> {
                                        tempColumn++
                                    }

                                    LEFT -> {
                                        tempColumn--
                                    }

                                    else -> {
                                        throw RuntimeException("Error")
                                    }
                                }

                                map.getOrNull(tempRow)?.getOrNull(tempColumn) == '#'
                            }

                            val result = when (move) {
                                0 -> {
                                    direction = (direction - 1).mod(4)
                                    "L"
                                }

                                1 -> {
                                    direction = (direction + 1).mod(4)
                                    "R"
                                }

                                else -> return
                            }

                            var moveRow = 0
                            var moveColumn = 0

                            when (direction) {
                                UP -> moveRow--
                                DOWN -> moveRow++
                                LEFT -> moveColumn--
                                RIGHT -> moveColumn++
                            }

                            var moveCount = 0
                            while (true) {
                                rowIndex += moveRow
                                columnIndex += moveColumn

                                if (map.getOrNull(rowIndex)?.getOrNull(columnIndex) == '#') {
                                    moveCount++
                                } else {
                                    rowIndex -= moveRow
                                    columnIndex -= moveColumn
                                    break
                                }
                            }

                            movements += "${result},${moveCount},"
                            walk()
                        }

                        walk()

                        loop@ for (aSize in 1..5) {
                            for (bSize in 1..5) {
                                next@ for (cSize in 1..5) {
                                    val sizes = arrayOf(aSize, bSize, cSize)
                                    val tokens = arrayListOf<String>()

                                    var t = movements.joinToString("")
                                    val tm = movements.toMutableList()

                                    val results = arrayListOf<Int>()
                                    while (tm.isNotEmpty()) {
                                        val tokenIndex = tokens.indexOfFirst { t.startsWith(it) }

                                        if (tokenIndex >= 0) {
                                            t = t.substring(tokens[tokenIndex].length)
                                            repeat(sizes[tokenIndex]) {
                                                tm.removeFirst()
                                            }

                                            results += tokenIndex
                                        } else if (tokens.size < 3) {
                                            tokens.add(tm.take(sizes[tokens.size]).joinToString(""))
                                        } else {
                                            continue@next
                                        }
                                    }

                                    if (results.size > 10) {
                                        continue@next
                                    }

                                    results.joinToString(",") { ('A' + it).toString() }.forEach {
                                        inputs += it.code
                                    }

                                    inputs += 10

                                    tokens.forEach {
                                        it.trimEnd(',').forEach {
                                            inputs += it.code
                                        }

                                        inputs += 10
                                    }

                                    inputs += 'n'.code
                                    inputs += 10

                                    break@loop
                                }
                            }
                        }
                    }
                    writeParam(1, inputs.pop().toLong())
                    index += 2
                }

                4 -> {
                    result = readParam(1)
                    when (val output = result.toInt().toChar()) {
                        '#' -> {
                            currentLine += output
                        }

                        '.' -> {
                            currentLine += output
                        }

                        '\n' -> {
                            currentLine = arrayListOf()
                            map += currentLine
                        }

                        '^' -> {
                            rowIndex = map.lastIndex
                            columnIndex = currentLine.size

                            currentLine += output
                        }

                        else -> {
//                            println("ERROR -- ${output} -- ${result}")
                        }
                    }
                    index += 2
                }

                5 -> {
                    if (readParam(1) != 0L) {
                        index = readParam(2)
                    } else {
                        index += 3
                    }
                }

                6 -> {
                    if (readParam(1) == 0L) {
                        index = readParam(2)
                    } else {
                        index += 3
                    }
                }

                7 -> {
                    writeParam(
                        3, if (readParam(1) < readParam(2)) {
                            1
                        } else {
                            0
                        }
                    )
                    index += 4
                }

                8 -> {
                    writeParam(
                        3, if (readParam(1) == readParam(2)) {
                            1
                        } else {
                            0
                        }
                    )
                    index += 4
                }

                9 -> {
                    relativeBase += readParam(1)

                    index += 2
                }

                99 -> {
                    done = true
                }

                else -> {
                    println("Error")
                }
            }
        }
        return result to map
    }

    val map = process().second

    var result1 = 0
    for (rowIndex in 1 until map.lastIndex) {
        val previousRow = map[rowIndex - 1]
        val row = map[rowIndex]
        val nextRow = map[rowIndex + 1]
        for (columnIndex in 1 until row.lastIndex) {
            if (row[columnIndex] == '#' && row[columnIndex - 1] == '#' && row[columnIndex + 1] == '#' && previousRow[columnIndex] == '#' && nextRow[columnIndex] == '#') {
                result1 += rowIndex * columnIndex
            }
        }
    }

    println(result1)

    println(process(2).first)
}