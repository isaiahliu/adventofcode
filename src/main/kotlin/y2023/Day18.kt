package y2023

import util.expectLong
import util.input

fun main() {
    expectLong {
        val JOIN_BOTH = 0
        val JOIN_UP = 1
        val JOIN_DOWN = 2

        class SegNode(val min: Int, val max: Int, initColumns: Set<Pair<Int, Int>> = emptySet()) {
            val columns = initColumns.toSortedSet(compareBy { it.first })

            val children = arrayOfNulls<SegNode>(2)

            fun mark(left: Int, right: Int, node: Pair<Int, Int>) {
                when {
                    left > max || right < min -> return

                    left > min || right < max -> {
                        val halfCount = (max.toLong() - min) / 2
                        if (children[0] == null) {
                            children[0] = SegNode(min, (min + halfCount).toInt(), columns)
                            children[1] = SegNode((min + halfCount).toInt() + 1, max, columns)
                        }
                    }

                    else -> columns.add(node)
                }

                children.forEach { it?.mark(left, right, node) }
            }

            fun size(): Long {
                return when {
                    children[0] != null -> {
                        children.sumOf { it?.size() ?: 0L }
                    }

                    else -> {
                        var sizePerRow = 0L

                        var prevC = 0
                        var prevType = JOIN_BOTH
                        var valid = false

                        columns.forEach { (c, type) ->
                            if (valid || prevType != JOIN_BOTH) {
                                sizePerRow += c - prevC
                            } else {
                                sizePerRow++
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

                        sizePerRow * (max + 1L - min)
                    }

                }
            }
        }

        class Game {
            var r = 0
            var c = 0

            val root = SegNode(Int.MIN_VALUE, Int.MAX_VALUE)

            fun move(direction: String, step: Int) {
                when (direction) {
                    "R" -> {
                        c += step
                    }

                    "D" -> {
                        root.mark(r, r, c to JOIN_DOWN)

                        if (step > 1) {
                            root.mark(r + 1, r + step - 1, c to JOIN_BOTH)
                        }

                        r += step
                        root.mark(r, r, c to JOIN_UP)
                    }

                    "L" -> {
                        c -= step
                    }

                    "U" -> {
                        root.mark(r, r, c to JOIN_UP)

                        if (step > 1) {
                            root.mark(r - step + 1, r - 1, c to JOIN_BOTH)
                        }

                        r -= step
                        root.mark(r, r, c to JOIN_DOWN)
                    }
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

        part1Result = game1.root.size()
        part2Result = game2.root.size()
    }
}