package y2016

import input

fun main() {
    fun process(rowCount: Int): Int {
        var line = input.first().map { it == '^' }.toBooleanArray()
        val map = arrayListOf(line)
        repeat(rowCount - 1) {
            line = (line.indices).map {
                val left = line.getOrNull(it - 1) ?: false
                val center = line.getOrNull(it) ?: false
                val right = line.getOrNull(it + 1) ?: false

                left && center && !right || !left && center && right || left && !center && !right || !left && !center && right
            }.toBooleanArray()

            map += line
        }
        return map.sumOf { it.count { !it } }
    }

    println(process(40))
    println(process(400000))
}