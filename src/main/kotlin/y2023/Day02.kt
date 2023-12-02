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
            s.split("; ", ", ").forEach {
                colorRegex.matchEntire(it)?.groupValues?.drop(1)?.also { (countStr, color) ->
                    val count = countStr.toInt()
                    when (color) {
                        "red" -> {
                            red = maxOf(red, count)
                        }

                        "green" -> {
                            green = maxOf(green, count)
                        }

                        "blue" -> {
                            blue = maxOf(blue, count)
                        }
                    }
                }
            }
            if (red <= 12 && green <= 13 && blue <= 14) {
                part1Result += id.toInt()
            }
        }
        part2Result += red * green * blue
    }

    println(part1Result)
    println(part2Result)
}
