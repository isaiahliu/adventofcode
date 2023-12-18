package y2022

import util.expectInt
import util.input

fun main() {
    expectInt {
        val walls = input.map { it.split(" -> ").map { it.split(",").let { it[0].toInt() to it[1].toInt() } } }

        val space: MutableMap<Int, MutableMap<Int, Char>> = hashMapOf()

        val empty = '.'
        val wall = '#'
        val floor = '='
        val sandOnWall = 'O'
        val sandOnFloor = 'o'

        var maxY = 0
        var currentSand = sandOnWall

        operator fun MutableMap<Int, MutableMap<Int, Char>>.get(x: Int, y: Int): Char {
            return computeIfAbsent(x) { hashMapOf() }.computeIfAbsent(y) {
                if (y < maxY) {
                    empty
                } else {
                    floor
                }
            }
        }

        operator fun MutableMap<Int, MutableMap<Int, Char>>.set(x: Int, y: Int, value: Char) {
            computeIfAbsent(x) { hashMapOf() }[y] = value
        }

        walls.forEach {
            for (index in 0 until it.lastIndex) {
                val first = it[index]
                val second = it[index + 1]

                for (x in first.first.coerceAtMost(second.first)..first.first.coerceAtLeast(second.first)) {
                    for (y in first.second.coerceAtMost(second.second)..first.second.coerceAtLeast(second.second)) {
                        maxY = maxY.coerceAtLeast(y + 2)
                        space[x, y] = wall
                    }
                }
            }
        }

        fun print() {
            for (y in 0..maxY) {
                buildString {
                    for (x in space.keys.min()..space.keys.max()) {
                        append(space[x, y])
                    }
                }.also { println(it) }
            }
            println()
        }

        fun dropSand(startX: Int, startY: Int): Boolean {
            if (space[startX, startY] != empty) {
                return false
            }

            var x = startX
            var y = startY
            while (true) {
                when {
                    space[x, y + 1] == empty -> {
                        y++
                    }

                    space[x - 1, y + 1] == empty -> {
                        y++
                        x--
                    }

                    space[x + 1, y + 1] == empty -> {
                        y++
                        x++
                    }

                    (currentSand != sandOnFloor) && (space[x - 1, y + 1] == floor || space[x, y + 1] == floor || space[x + 1, y + 1] == floor) -> {
                        currentSand = sandOnFloor
                    }

                    else -> {
                        space[x, y] = currentSand
                        break
                    }
                }
            }
            return true
        }

        while (dropSand(500, 0)) {
//        print()
        }

        part1Result = space.values.sumOf {
            it.values.count { it == sandOnWall }
        }

        part2Result = space.values.sumOf {
            it.values.count { it == sandOnWall || it == sandOnFloor }
        }
    }
}