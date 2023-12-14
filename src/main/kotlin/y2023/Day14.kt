package y2023

import util.input

fun main() {
    val chars = input.map {
        it.toCharArray()
    }.toTypedArray()

    val NORTH = 0
    val EAST = 1
    val SOUTH = 2
    val WEST = 3

    operator fun Array<CharArray>.get(direction: Int, r: Int, c: Int): Char? {
        return when (direction) {
            NORTH -> {
                getOrNull(r)?.getOrNull(c)
            }

            WEST -> {
                getOrNull(this[0].lastIndex - c)?.getOrNull(r)
            }

            SOUTH -> {
                getOrNull(this.lastIndex - r)?.getOrNull(this[0].lastIndex - c)
            }

            else -> {
                getOrNull(c)?.getOrNull(this.lastIndex - r)
            }
        }
    }

    operator fun Array<CharArray>.set(direction: Int, r: Int, c: Int, char: Char) {
        when (direction) {
            NORTH -> {
                this[r][c] = char
            }

            WEST -> {
                this[this[0].lastIndex - c][r] = char
            }

            SOUTH -> {
                this[this.lastIndex - r][this[0].lastIndex - c] = char
            }

            else -> {
                this[c][this.lastIndex - r] = char
            }
        }
    }

    fun process(direction: Int) {
        var columnIndex = 0

        while (chars[direction, 0, columnIndex] != null) {
            var emptyRowIndex = 0
            while (true) {
                when {
                    chars[direction, emptyRowIndex, columnIndex] == null -> {
                        break
                    }

                    chars[direction, emptyRowIndex, columnIndex] != '.' -> {
                        emptyRowIndex++
                    }

                    else -> {
                        var checkRowIndex = emptyRowIndex + 1

                        while (true) {
                            when (chars[direction, checkRowIndex, columnIndex]) {
                                'O' -> {
                                    chars[direction, emptyRowIndex, columnIndex] = 'O'
                                    chars[direction, checkRowIndex, columnIndex] = '.'
                                    emptyRowIndex++
                                    checkRowIndex++
                                }

                                '#', null -> {
                                    emptyRowIndex = checkRowIndex + 1
                                    break
                                }

                                '.' -> {
                                    checkRowIndex++
                                }
                            }
                        }
                    }
                }
            }

            columnIndex++
        }
    }

    process(NORTH)
    val part1Result = chars.mapIndexed { index, row -> row.count { it == 'O' } * (chars.size - index) }.sum()

    process(WEST)
    process(SOUTH)
    process(EAST)

    var step = 1
    var remain = 1000000000

    val cache = hashMapOf<String, Int>()
    while (step < remain) {
        process(NORTH)
        process(WEST)
        process(SOUTH)
        process(EAST)

        val cacheKey = chars.joinToString("") { it.concatToString() }

        cache[cacheKey]?.also {
            val diff = step - it

            remain -= (remain - step) / diff * diff
        } ?: run {
            cache[cacheKey] = step
        }

        step++
    }
    val part2Result = chars.mapIndexed { index, row -> row.count { it == 'O' } * (chars.size - index) }.sum()

    println(part1Result)
    println(part2Result)
}
