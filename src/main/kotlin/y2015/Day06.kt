package y2015

import input

fun main() {
    val regex = "(turn on|turn off|toggle) (\\d+),(\\d+) through (\\d+),(\\d+)".toRegex()

    val lights1 = Array(1000) { IntArray(1000) }
    val lights2 = Array(1000) { IntArray(1000) }

    input.mapNotNull { regex.matchEntire(it) }.forEach {
        val fromX = it.groupValues[2].toInt()
        val fromY = it.groupValues[3].toInt()
        val toX = it.groupValues[4].toInt()
        val toY = it.groupValues[5].toInt()

        val lightSwitch1: (Int) -> Int = { stat ->
            when (it.groupValues[1]) {
                "turn off" -> 0
                "turn on" -> 1
                else -> 1 - stat
            }
        }

        val lightSwitch2: (Int) -> Int = { stat ->
            when (it.groupValues[1]) {
                "turn off" -> (stat - 1).coerceAtLeast(0)
                "turn on" -> stat + 1
                else -> stat + 2
            }
        }

        for (x in fromX..toX) {
            for (y in fromY..toY) {
                lights1[x][y] = lightSwitch1(lights1[x][y])
                lights2[x][y] = lightSwitch2(lights2[x][y])
            }
        }

    }
    println(lights1.sumOf { it.sum() })
    println(lights2.sumOf { it.sum() })
}
