package y2020

import util.input
import java.util.*

fun main() {
    val map = hashSetOf<Pair<Int, Int>>()

    input.map { LinkedList(it.toList()) }.forEach {
        var rowIndex = 0
        var columnIndex = 0
        while (it.isNotEmpty()) {
            when (it.pop()) {
                'n' -> {
                    rowIndex--
                    when (it.pop()) {
                        'w' -> {
                            columnIndex--
                        }
                    }
                }

                's' -> {
                    rowIndex++
                    when (it.pop()) {
                        'e' -> {
                            columnIndex++
                        }
                    }
                }

                'w' -> {
                    columnIndex--
                }

                'e' -> {
                    columnIndex++
                }
            }
        }

        val pos = rowIndex to columnIndex
        if (pos in map) {
            map -= pos
        } else {
            map += pos
        }
    }

    println(map.size)

    repeat(100) {
        val blackRemains = hashSetOf<Pair<Int, Int>>()
        val whiteTouches = hashMapOf<Pair<Int, Int>, Int>()

        map.forEach { (r, c) ->
            val neighbors = arrayOf(
                r - 1 to c - 1, r - 1 to c, r to c - 1, r to c + 1, r + 1 to c, r + 1 to c + 1
            )

            if (neighbors.count { it in map } in 1..2) {
                blackRemains += r to c
            }

            neighbors.filter { it !in map }.forEach { white ->
                whiteTouches[white] = (whiteTouches[white] ?: 0) + 1
            }
        }

        map.clear()
        map += blackRemains

        whiteTouches.filter { it.value == 2 }.forEach { (white, _) ->
            map += white
        }
    }
    println(map.size)
}