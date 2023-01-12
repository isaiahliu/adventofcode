package y2020

import util.input

fun main() {
    val map = input.map {
        it.toCharArray()
    }.toTypedArray()

    fun process(moveX: Int, moveY: Int): Int {
        var x = moveX
        var y = moveY
        var result = 0

        while (y < map.size) {
            if (map[y].let { it[x % it.size] } == '#') {
                result++
            }

            x += moveX
            y += moveY
        }
        return result
    }

    println(process(3, 1))

    println(
        arrayOf(
            process(1, 1),
            process(3, 1),
            process(5, 1),
            process(7, 1),
            process(1, 2)
        ).fold(1L) { a, b -> a * b })
}