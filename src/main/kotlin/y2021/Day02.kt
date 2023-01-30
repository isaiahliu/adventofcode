package y2021

import util.input

fun main() {
    fun process(v2: Boolean): Int {
        var horizontal = 0
        var depth = 0
        var aim = 0
        input.map { it.split(" ").let { it[0] to it[1].toInt() } }.forEach { (method, value) ->
            when (method) {
                "down" -> {
                    if (v2) {
                        aim += value
                    } else {
                        depth += value
                    }
                }

                "up" -> {
                    if (v2) {
                        aim -= value
                    } else {
                        depth -= value
                    }
                }

                "forward" -> {
                    horizontal += value
                    if (v2) {
                        depth += aim * value
                    }
                }
            }
        }

        return horizontal * depth
    }

    println(process(false))
    println(process(true))
}