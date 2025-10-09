package y2017

import util.expect
import util.input
import kotlin.math.sign

fun main() {
    expect(0, 0) {
        val array = input.first().split("\\s+".toRegex()).map { it.toInt() }.toIntArray()
        val visited = hashMapOf<String, Int>()

        while (true) {
            part2Result = visited.put(array.joinToString("_") { it.toString() }, part1Result)?.let {
                visited.size - it
            } ?: 0

            if (part2Result > 0) {
                break
            }

            val max = array.max()

            val maxIndex = array.indexOfFirst { it == max }

            array[maxIndex] = 0

            repeat(array.size) {
                array[(maxIndex + it + 1) % array.size] += max / array.size + (max % array.size / (it + 1)).sign
            }

            part1Result++
        }
    }
}