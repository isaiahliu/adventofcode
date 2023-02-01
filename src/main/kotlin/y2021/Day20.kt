package y2021

import util.input

fun main() {
    val algorithm = input.first()

    var pixels = hashSetOf<Pair<Int, Int>>()

    input.drop(2).forEachIndexed { rowIndex, row ->
        row.forEachIndexed { columnIndex, c ->
            if (c == '#') {
                pixels += rowIndex to columnIndex
            }
        }
    }

    fun printPixels() {
        val minRowIndex = pixels.minOf { it.first }
        val maxRowIndex = pixels.maxOf { it.first }
        val minColumnIndex = pixels.minOf { it.second }
        val maxColumnIndex = pixels.maxOf { it.second }

        for (r in minRowIndex - 1..maxRowIndex + 1) {
            buildString {
                for (c in minColumnIndex - 1..maxColumnIndex + 1) {
                    append(if (r to c in pixels) "#" else ".")
                }
            }.also { println(it) }
        }
        println()
    }

    var result1 = 0
    var borderLit = false
    repeat(50) {
        val minRowIndex = pixels.minOf { it.first }
        val maxRowIndex = pixels.maxOf { it.first }
        val minColumnIndex = pixels.minOf { it.second }
        val maxColumnIndex = pixels.maxOf { it.second }

        val newPixels = hashSetOf<Pair<Int, Int>>()

        for (r in minRowIndex - 1..maxRowIndex + 1) {
            for (c in minColumnIndex - 1..maxColumnIndex + 1) {
                val pos = arrayOf(
                    r - 1 to c - 1,
                    r - 1 to c,
                    r - 1 to c + 1,
                    r to c - 1,
                    r to c,
                    r to c + 1,
                    r + 1 to c - 1,
                    r + 1 to c,
                    r + 1 to c + 1
                ).joinToString("") {
                    if (it.takeIf {
                            it.first in minRowIndex..maxRowIndex && it.second in minColumnIndex..maxColumnIndex
                        }?.let {
                            it in pixels
                        } ?: borderLit) {
                        "1"
                    } else {
                        "0"
                    }
                }.toInt(2)

                if (algorithm[pos] == '#') {
                    newPixels += r to c
                }
            }
        }

        pixels = newPixels

        if (algorithm[0] == '#') {
            borderLit = !borderLit
        }

        if (it == 1) {
            result1 = pixels.size
        }
//        printPixels()
    }

    println(result1)
    println(pixels.size)
}