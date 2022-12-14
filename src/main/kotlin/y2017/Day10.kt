package y2017

import util.input

fun main() {
    var array = IntArray(256) { it }

    fun reverse(startIndex: Int, length: Int) {
        repeat(length / 2) {
            val t = array[(startIndex + it) % array.size]
            array[(startIndex + it) % array.size] = array[(startIndex + length - it - 1) % array.size]
            array[(startIndex + length - it - 1) % array.size] = t
        }
    }

    var index = 0
    var skipSize = 0
    input.first().split(",").map { it.toInt() }.forEach {
        reverse(index, it)
        index = (index + it + skipSize) % array.size
        skipSize++
    }

    println(array[0] * array[1])

    val lengths = buildList {
        addAll(input.first().map { it.code })
        addAll(listOf(17, 31, 73, 47, 23))
    }

    array = IntArray(256) { it }
    index = 0
    skipSize = 0
    repeat(64) {
        lengths.forEach {
            reverse(index, it)
            index = (index + it + skipSize) % array.size
            skipSize++
        }
    }

    val result2 = (0 until 16).joinToString("") {
        array.drop(16 * it).take(16).reduce { a, b -> a xor b }.toString(16).padStart(2, '0')
    }

    println(result2)
}