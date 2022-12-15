package y2017

import util.input

fun main() {
    val line = input.first()

    var array = IntArray(256) { it }

    fun reverse(startIndex: Int, length: Int) {
        repeat(length / 2) {
            val t = array[(startIndex + it) % array.size]
            array[(startIndex + it) % array.size] = array[(startIndex + length - it - 1) % array.size]
            array[(startIndex + length - it - 1) % array.size] = t
        }
    }

    val used = Array(128) {
        IntArray(128)
    }

    repeat(128) {
        val hashKey = "${line}-${it}"

        val lengths = buildList {
            addAll(hashKey.map { it.code })
            addAll(listOf(17, 31, 73, 47, 23))
        }

        array = IntArray(256) { it }
        var index = 0
        var skipSize = 0
        repeat(64) {
            lengths.forEach {
                reverse(index, it)
                index = (index + it + skipSize) % array.size
                skipSize++
            }
        }

        val knotHash = (0 until 16).joinToString("") {
            array.drop(16 * it).take(16).reduce { a, b -> a xor b }.toString(16).padStart(2, '0')
        }

        used[it] = (knotHash.toCharArray().joinToString("") {
            it.toString().toInt(16).toString(2).padStart(4, '0')
        }).map { it - '0' }.toIntArray()
    }

    println(used.sumOf { it.sum() })

    fun walkRegion(rowIndex: Int, columnIndex: Int): Boolean {
        return if (used.getOrNull(rowIndex)?.getOrNull(columnIndex) == 1) {
            used[rowIndex][columnIndex] = 0

            walkRegion(rowIndex - 1, columnIndex)
            walkRegion(rowIndex + 1, columnIndex)
            walkRegion(rowIndex, columnIndex - 1)
            walkRegion(rowIndex, columnIndex + 1)

            true
        } else {
            false
        }
    }

    var regionCount = 0
    for ((rowIndex, row) in used.withIndex()) {
        repeat(row.size) { columnIndex ->
            if (walkRegion(rowIndex, columnIndex)) {
                regionCount++
            }
        }
    }

    println(regionCount)
}