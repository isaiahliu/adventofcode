package y2018

import util.input

fun main() {
    val regex = "position=<\\s*(-?\\d+), \\s*(-?\\d+)> velocity=<\\s*(-?\\d+), \\s*(-?\\d+)>".toRegex()

    data class Star(var px: Int, var py: Int, val vx: Int, val vy: Int)

    val stars = input.mapNotNull { regex.matchEntire(it)?.groupValues?.drop(1)?.map { it.toInt() } }.map { (px, py, vx, vy) ->
        Star(px, py, vx, vy)
    }

    var currentSquare: Long = Long.MAX_VALUE
    var seconds = 0
    while (true) {
        stars.forEach {
            it.px += it.vx
            it.py += it.vy
        }

        val width = (stars.maxOf { it.px } - stars.minOf { it.px }).toLong()
        val height = (stars.maxOf { it.py } - stars.minOf { it.py }).toLong()

        val square: Long = (width * height)

        if (square < currentSquare) {
            currentSquare = square
            seconds++
        } else {
            stars.forEach {
                it.px -= it.vx
                it.py -= it.vy
            }

            for (y in stars.minOf { it.py }..stars.maxOf { it.py }) {
                buildString {
                    for (x in stars.minOf { it.px }..stars.maxOf { it.px }) {
                        if (stars.any { it.px == x && it.py == y }) {
                            append("#")
                        } else {
                            append(".")
                        }
                    }
                }.also { println(it) }
            }

            println(seconds)
            break
        }
    }
}