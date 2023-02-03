package y2021

import util.input

fun main() {
    val EAST = 0
    val SOUTH = 1

    val southCucumbers = hashSetOf<Pair<Int, Int>>()
    val eastCucumbers = hashSetOf<Pair<Int, Int>>()

    val rowCount = input.size
    val columnCount = input[0].length

    input.forEachIndexed { r, row ->
        row.forEachIndexed { c, item ->
            when (item) {
                '>' -> eastCucumbers += r to c
                'v' -> southCucumbers += r to c
            }
        }
    }

    fun printMap() {
        repeat(rowCount) { r ->
            buildString {
                repeat(columnCount) { c ->
                    append(
                        when (r to c) {
                            in eastCucumbers -> ">"
                            in southCucumbers -> "v"
                            else -> "."
                        }
                    )
                }
            }.also { println(it) }
        }
        println()
    }

    var done = false
    var moveCount = 0
    while (!done) {
        moveCount++
        done = true

        arrayOf(eastCucumbers, southCucumbers).forEachIndexed { direction, cucumbers ->
            val moveMethod: (Pair<Int, Int>) -> Pair<Int, Int> = when (direction) {
                EAST -> {
                    { (r, c) ->
                        r to (c + 1) % columnCount
                    }
                }

                SOUTH -> {
                    { (r, c) ->
                        (r + 1) % rowCount to c
                    }
                }

                else -> throw RuntimeException("Error")
            }

            val move = cucumbers.filter {
                moveMethod(it).let { it !in eastCucumbers && it !in southCucumbers }
            }.toSet()

            if (move.isNotEmpty()) {
                cucumbers -= move
                cucumbers += move.map(moveMethod)
                done = false
            }
        }
    }

    println(moveCount)
}