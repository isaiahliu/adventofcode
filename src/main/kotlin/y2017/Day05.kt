package y2017

import util.input

fun main() {
    var array = input.map { it.toInt() }.toIntArray()
    var index = 0
    var step = 0
    while (index < array.size) {
        index += array[index]++

        step++
    }

    println(step)

    array = input.map { it.toInt() }.toIntArray()
    index = 0
    step = 0
    while (index < array.size) {
        val offset = array[index]

        array[index] += if (offset < 3) {
            1
        } else {
            -1
        }

        index += offset
        step++
    }

    println(step)
}