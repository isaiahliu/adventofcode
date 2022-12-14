package y2017

import util.input

fun main() {
    val array = input.first().split("\\s+".toRegex()).map { it.toInt() }.toIntArray()

    val cache = linkedSetOf<String>()

    fun walked(): Int {
        val path = array.joinToString("_") { it.toString() }
        return if (cache.add(path)) {
            0
        } else {
            cache.size - cache.indexOf(path)
        }
    }

    var steps = 0
    var cycleSize: Int
    while (true) {
        cycleSize = walked()
        if (cycleSize > 0) {
            break
        }

        var max = array.max()

        var maxIndex = array.indexOfFirst { it == max }

        array[maxIndex] = 0

        while (max-- > 0) {
            maxIndex = (maxIndex + 1) % array.size

            array[maxIndex]++
        }

        steps++
    }

    println(steps)
    println(cycleSize)
}