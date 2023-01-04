package y2019

import util.input
import kotlin.math.PI
import kotlin.math.absoluteValue
import kotlin.math.atan

fun main() {
    val map = hashSetOf<Pair<Int, Int>>()
    input.forEachIndexed { y, row ->
        row.forEachIndexed { x, c ->
            if (c == '#') {
                map += x to y
            }
        }
    }

    val result = hashMapOf<Pair<Int, Int>, Int>()
    map.forEach { (x, y) ->
        var count = 0

        map.forEach { (targetX, targetY) ->
            val deltaX = targetX - x
            val deltaY = targetY - y

            var match = true
            if (deltaX != 0) {
                var xIncrement = deltaX / deltaX.absoluteValue

                while (xIncrement != deltaX) {
                    if (xIncrement * deltaY % deltaX == 0) {
                        if (x + xIncrement to y + xIncrement * deltaY / deltaX in map) {
                            match = false
                            break
                        }
                    }

                    xIncrement += deltaX / deltaX.absoluteValue
                }
            } else if (deltaY != 0) {
                var yIncrement = deltaY / deltaY.absoluteValue

                while (yIncrement != deltaY) {
                    if (yIncrement * deltaX % deltaY == 0) {
                        if (x + yIncrement * deltaX / deltaY to y + yIncrement in map) {
                            match = false
                            break
                        }
                    }

                    yIncrement += deltaY / deltaY.absoluteValue
                }
            } else {
                match = false
            }

            if (match) {
                count++
            }
        }

        result[x to y] = count
    }

    val (bestPoint, max) = result.maxBy { it.value }
    println(max)

    val t = map.toMutableSet()
    t -= bestPoint

    val (bestX, bestY) = bestPoint

    val groups = t.groupBy { (targetX, targetY) ->
        val deltaX = targetX - bestX
        val deltaY = bestY - targetY

        when {
            deltaX > 0 -> {
                when {
                    deltaY == 0 -> 0.0
                    else -> {
                        atan(deltaY.toDouble() / deltaX) / PI * 180
                    }
                }
            }

            deltaX < 0 -> {
                when {
                    deltaY == 0 -> 180.0
                    else -> {
                        180 + atan(deltaY.toDouble() / deltaX) / PI * 180
                    }
                }
            }

            else -> {
                when {
                    deltaY > 0 -> 90.0
                    else -> 270.0
                }
            }
        }
    }.entries.map {
        it.key to it.value.sortedWith(compareBy<Pair<Int, Int>> { (it.first - bestX).absoluteValue }.thenBy { (it.second - bestY).absoluteValue })
            .toMutableList()
    }
        .sortedByDescending { it.first }

//    println(bestPoint)
    var index = groups.indexOfFirst { it.first <= 90 }
    var part2Result: Pair<Int, Int> = 0 to 0
    repeat(200) {
        while (true) {
            val matchedGroups = groups[index].second

            index = (index + 1) % groups.size
            if (matchedGroups.isNotEmpty()) {
                part2Result = matchedGroups.first()

                matchedGroups -= part2Result

                break
            }
        }
//        println("${it + 1} -- ${part2Result}")
    }
    println(part2Result.first * 100 + part2Result.second)

}