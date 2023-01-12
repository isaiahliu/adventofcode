package y2020

import util.input

fun main() {
    val FLOOR = '.'
    val EMPTY = 'L'
    val OCCUPIED = '#'

    val around = arrayOf(
        -1 to -1,
        -1 to 0,
        -1 to +1,
        0 to -1,
        0 to +1,
        +1 to -1,
        +1 to 0,
        +1 to +1,
    )

    fun process(v2: Boolean): Int {
        val map = input.map { it.toCharArray() }.toTypedArray()

        val occupied = Array(map.size) {
            IntArray(map[it].size)
        }

        var moved = true
        while (moved) {
            moved = false

            repeat(map.size) { rowIndex ->
                repeat(map[rowIndex].size) { columnIndex ->
                    occupied[rowIndex][columnIndex] = if (v2) {
                        around.count { (r, c) ->
                            var tr = rowIndex + r
                            var tc = columnIndex + c

                            var found = false
                            while (true) {
                                when (map.getOrNull(tr)?.getOrNull(tc)) {
                                    OCCUPIED -> {
                                        found = true
                                        break
                                    }

                                    EMPTY -> {
                                        break
                                    }

                                    null -> {
                                        break
                                    }
                                }

                                tr += r
                                tc += c
                            }

                            found
                        }
                    } else {
                        around.count { (r, c) ->
                            map.getOrNull(rowIndex + r)?.getOrNull(columnIndex + c) == '#'
                        }
                    }
                }
            }

            repeat(map.size) { rowIndex ->
                repeat(map[rowIndex].size) { columnIndex ->
                    val occupyCount = occupied[rowIndex][columnIndex]
                    map[rowIndex][columnIndex] = when (map[rowIndex][columnIndex]) {
                        EMPTY -> {
                            if (occupyCount == 0) {
                                moved = true
                                OCCUPIED
                            } else {
                                EMPTY
                            }
                        }

                        OCCUPIED -> {
                            if (occupyCount >= if (v2) 5 else 4) {
                                moved = true
                                EMPTY
                            } else {
                                OCCUPIED
                            }
                        }

                        FLOOR -> {
                            FLOOR
                        }

                        else -> throw RuntimeException("Error")
                    }
                }
            }
        }
        return map.sumOf { it.count { it == OCCUPIED } }
    }

    println(process(false))
    println(process(true))
}