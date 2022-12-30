package y2019

import util.input
import kotlin.math.absoluteValue

fun main() {
    val ware1 = hashMapOf<Pair<Int, Int>, Int>()
    val ware2 = hashMapOf<Pair<Int, Int>, Int>()

    val (ware1Path, ware2Path) = input.map { it.split(",").map { it[0] to it.substring(1).toInt() } }

    fun draw(paths: List<Pair<Char, Int>>, map: MutableMap<Pair<Int, Int>, Int>) {
        var x = 0
        var y = 0
        var totalStep = 1
        paths.forEach { (direction, steps) ->
            var deltaX = 0
            var deltaY = 0
            when (direction) {
                'U' -> {
                    deltaY = 1
                }

                'D' -> {
                    deltaY = -1
                }

                'L' -> {
                    deltaX = -1
                }

                'R' -> {
                    deltaX = 1
                }
            }

            repeat(steps) {
                x += deltaX
                y += deltaY

                map[x to y] = totalStep++
            }
        }
    }

    draw(ware1Path, ware1)
    draw(ware2Path, ware2)

    val intersects = ware1.keys.intersect(ware2.keys)
    
    println(intersects.minOf { (x, y) -> x.absoluteValue + y.absoluteValue })
    println(intersects.minOf { (x, y) -> ware1[x to y]!! + ware2[x to y]!! })
}