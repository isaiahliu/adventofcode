package y2018

import util.input

fun main() {
    val fabric = Array(1000) { IntArray(1000) }

    val regex = "#(\\d+) @ (\\d+),(\\d+): (\\d+)x(\\d+)".toRegex()
    val claims = input.mapNotNull { regex.matchEntire(it)?.groupValues?.drop(1)?.map { it.toInt() } }

    claims.forEach { (_, skipX, skipY, width, height) ->
        repeat(height) { y ->
            repeat(width) { x ->
                fabric[skipY + y][skipX + x]++
            }
        }
    }

    println(fabric.sumOf { it.count { it > 1 } })

    println(claims.filter { (_, skipX, skipY, width, height) ->
        fabric.drop(skipY).take(height).all {
            it.drop(skipX).take(width).all { it == 1 }
        }
    }[0][0])
}