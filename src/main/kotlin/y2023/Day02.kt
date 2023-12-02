package y2023

import util.input

fun main() {
    var part1Result = 0
    var part2Result = 0

    val gameRegex = "Game (\\d+): (.*)".toRegex()
    val colorRegex = "(\\d+) (.*)".toRegex()
    input.forEach {
        var red = 0
        var green = 0
        var blue = 0

        gameRegex.matchEntire(it)?.groupValues?.drop(1)?.also { (id, s) ->
            var match = true
            s.split("; ").forEach {
                it.split(", ").forEach {
                    colorRegex.matchEntire(it)?.groupValues?.drop(1)?.also { (countStr, color) ->
                        val count = countStr.toInt()
                        when (color) {
                            "red" -> {
                                match = match && count <= 12
                                red = maxOf(red, count)
                            }

                            "green" -> {
                                match = match && count <= 13
                                green = maxOf(green, count)
                            }

                            "blue" -> {
                                blue = maxOf(blue, count)
                                match = match && count <= 14
                            }
                        }
                    }
                }
            }
            if (match) {
                part1Result += id.toInt()
            }
        }
        part2Result += red * green * blue
    }

    println(part1Result)
    println(part2Result)
}
