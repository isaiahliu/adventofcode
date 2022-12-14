package y2022

import util.input
import kotlin.math.absoluteValue

fun main() {
    val rope = Array(10) { RopeNode(0, 0) }

    input.map { it.split(" ") }.forEach {
        val direction = it[0]

        repeat(it[1].toInt()) {
            rope[0].move(direction)
            repeat(rope.size - 1) {
                rope[it + 1].follow(rope[it])
            }
        }
    }

    println(rope[1].visited.size)
    println(rope.last().visited.size)
}

private class RopeNode(var x: Int, var y: Int) {
    val visited: MutableSet<String> = hashSetOf()

    init {
        move(x, y)
    }

    fun move(direction: String) {
        when (direction) {
            "R" -> {
                move(toX = x + 1)
            }

            "L" -> {
                move(toX = x - 1)
            }

            "U" -> {
                move(toY = y + 1)
            }

            "D" -> {
                move(toY = y - 1)
            }
        }
    }

    fun move(toX: Int = x, toY: Int = y) {
        x = toX
        y = toY

        visited += "${x}_${y}"
    }

    fun follow(previous: RopeNode) {
        val diffX = previous.x - x
        val diffY = previous.y - y

        var newX = x
        var newY = y
        if (diffX.absoluteValue > 1 || diffY.absoluteValue > 1) {
            newX += diffX.compareTo(0)
            newY += diffY.compareTo(0)
        }

        move(newX, newY)
    }
}