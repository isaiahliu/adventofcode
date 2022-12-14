package y2022

import util.input

fun main() {
    val walls = input.map { it.split(" -> ").map { it.split(",").let { it[0].toInt() to it[1].toInt() } } }

    Space.initializeWalls(walls)

    while (Space.dropSand(500, 0)) {
//        Space.print()
    }

    println(Space.map.values.sumOf {
        it.values.count { it == Space.SAND_ON_WALL }
    })

    println(Space.map.values.sumOf {
        it.values.count { it == Space.SAND_ON_WALL || it == Space.SAND_ON_FLOOR }
    })
}


private object Space {
    private const val EMPTY = '.'
    private const val WALL = '#'
    private const val FLOOR = '='
    const val SAND_ON_WALL = 'O'
    const val SAND_ON_FLOOR = 'o'

    var maxY = 0
    var currentSand = SAND_ON_WALL

    fun initializeWalls(walls: List<List<Pair<Int, Int>>>) {
        walls.forEach {
            for (index in 0 until it.size - 1) {
                val first = it[index]
                val second = it[index + 1]

                for (x in first.first.coerceAtMost(second.first)..first.first.coerceAtLeast(second.first)) {
                    for (y in first.second.coerceAtMost(second.second)..first.second.coerceAtLeast(second.second)) {
                        maxY = maxY.coerceAtLeast(y + 2)
                        map[x, y] = WALL
                    }
                }
            }
        }
    }

    val map: MutableMap<Int, MutableMap<Int, Char>> = hashMapOf()

    operator fun MutableMap<Int, MutableMap<Int, Char>>.get(x: Int, y: Int): Char {
        return map.computeIfAbsent(x) { hashMapOf() }.computeIfAbsent(y) {
            if (y < maxY) {
                EMPTY
            } else {
                FLOOR
            }
        }
    }

    operator fun MutableMap<Int, MutableMap<Int, Char>>.set(x: Int, y: Int, value: Char) {
        map.computeIfAbsent(x) { hashMapOf() }[y] = value
    }

    fun print() {
        for (y in 0..maxY) {
            buildString {
                for (x in map.keys.min()..map.keys.max()) {
                    append(map[x, y])
                }
            }.also { println(it) }
        }
        println()
    }

    fun dropSand(startX: Int, startY: Int): Boolean {
        if (map[startX, startY] != EMPTY) {
            return false
        }

        var x = startX
        var y = startY
        while (true) {
            when {
                map[x, y + 1] == EMPTY -> {
                    y++
                }

                map[x - 1, y + 1] == EMPTY -> {
                    y++
                    x--
                }

                map[x + 1, y + 1] == EMPTY -> {
                    y++
                    x++
                }

                (currentSand != SAND_ON_FLOOR) && (map[x - 1, y + 1] == FLOOR || map[x, y + 1] == FLOOR || map[x + 1, y + 1] == FLOOR) -> {
                    currentSand = SAND_ON_FLOOR
                }

                else -> {
                    map[x, y] = currentSand
                    break
                }
            }
        }
        return true
    }
}

