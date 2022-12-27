package y2018

import util.input
import kotlin.math.absoluteValue

fun main() {
    val diff = 'a' - 'A'

    fun process(str: String): Int {
        val chars = str.toCharArray().toMutableList()
        var index = 0

        while (index < chars.size) {
            when {
                index == 0 -> {
                    index++
                }

                (chars[index] - chars[index - 1]).absoluteValue == diff -> {
                    index--

                    chars.removeAt(index)
                    chars.removeAt(index)
                }

                else -> {
                    index++
                }
            }
        }

        return chars.size
    }

    println(process(input.first()))

    val result = ('A'..'Z').minOf {
        process(input.first().replace(it.toString(), "").replace(it.toString().lowercase(), ""))
    }

    println(result)
}