package y2023

import util.input
import java.util.*

fun main() {
    class Game {
        var r = 0
        var c = 0

        val JOIN_BOTH = 0
        val JOIN_UP = 1
        val JOIN_DOWN = 2

        val columns = sortedMapOf<Int, SortedSet<Pair<Int, Int>>>()

        fun move(direction: String, step: Int) {
            when (direction) {
                "R" -> {
                    c += step
                }

                "D" -> {
                    columns.computeIfAbsent(r++) { TreeSet(compareBy { it.first }) } += c to JOIN_DOWN

                    repeat(step - 1) {
                        columns.computeIfAbsent(r++) { TreeSet(compareBy { it.first }) } += c to JOIN_BOTH
                    }

                    columns.computeIfAbsent(r) { TreeSet(compareBy { it.first }) } += c to JOIN_UP
                }

                "L" -> {
                    c -= step
                }

                "U" -> {
                    columns.computeIfAbsent(r--) { TreeSet(compareBy { it.first }) } += c to JOIN_UP

                    repeat(step - 1) {
                        columns.computeIfAbsent(r--) { TreeSet(compareBy { it.first }) } += c to JOIN_BOTH
                    }

                    columns.computeIfAbsent(r) { TreeSet(compareBy { it.first }) } += c to JOIN_DOWN
                }
            }

        }

        val size: Long
            get() {
                return columns.values.sumOf {
                    var result = 0L

                    var prevC = 0
                    var prevType = JOIN_BOTH
                    var valid = false

                    it.forEach { (c, type) ->
                        if (valid || prevType != JOIN_BOTH) {
                            result += c - prevC
                        } else {
                            result++
                        }

                        when {
                            type == JOIN_BOTH || type == JOIN_UP && prevType == JOIN_DOWN || type == JOIN_DOWN && prevType == JOIN_UP -> {
                                valid = !valid
                                prevType = JOIN_BOTH
                            }

                            type == prevType -> {
                                prevType = JOIN_BOTH
                            }

                            else -> {
                                prevType = type
                            }
                        }

                        prevC = c
                    }

                    result
                }
            }
    }

    val game1 = Game()
    val game2 = Game()

    val directions = arrayOf("R", "D", "L", "U")

    input.forEach {
        val (direction, step, color) = it.split(" ")

        game1.move(direction, step.toInt())

        "\\(#(.{5})(\\d)\\)".toRegex().matchEntire(color)?.groupValues?.drop(1)?.also { (s, d) ->
            game2.move(directions[d.toInt()], s.toInt(16))
        }
    }

    val part1Result = game1.size
    val part2Result = game2.size

    println(part1Result)
    println(part2Result)
}
