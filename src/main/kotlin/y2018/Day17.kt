package y2018

import util.input

fun main() {
    val WALL = 1
    val FLOW_WATER = 2
    val STILL_WATER = 3
    val map = hashMapOf<Pair<Int, Int>, Int>()

    val regex = "(\\w)=(\\d+), \\w=(\\d+)..(\\d+)".toRegex()
    input.mapNotNull { regex.matchEntire(it)?.groupValues?.drop(1) }.forEach { (xy, v, v1, v2) ->
        var x1 = 0
        var x2 = 0
        var y1 = 0
        var y2 = 0
        when (xy) {
            "x" -> {
                x1 = v.toInt()
                x2 = v.toInt()
                y1 = v1.toInt()
                y2 = v2.toInt()
            }

            "y" -> {
                y1 = v.toInt()
                y2 = v.toInt()
                x1 = v1.toInt()
                x2 = v2.toInt()
            }
        }

        (x1..x2).forEach { x ->
            (y1..y2).forEach { y ->
                map[x to y] = WALL
            }
        }
    }

    fun printMap() {
        (map.keys.minOf { it.second }..map.keys.maxOf { it.second }).forEach { y ->
            buildString {
                (map.keys.minOf { it.first }..map.keys.maxOf { it.first }).forEach { x ->
                    append(
                        when (map[x to y]) {
                            WALL -> "#"
                            FLOW_WATER -> "|"
                            STILL_WATER -> "~"
                            else -> "."
                        }
                    )
                }
            }.also { println(it) }
        }
        println()
    }

//    printMap()

    val rangeY = map.keys.minOf { (_, y) -> y }..map.keys.maxOf { (_, y) -> y }

    fun drop(x: Int, y: Int) {
        var ty = y

        fun scanBorder(direction: Int, baseX: Int): Int? {
            var tx = baseX
            var drop = false

            while (!drop) {
                tx += direction

                when (map[tx to ty]) {
                    WALL -> {
                        break
                    }

                    FLOW_WATER -> {
                        drop = true
                        break
                    }

                    else -> {
                        map[tx to ty] = FLOW_WATER

                        when (map[tx to ty + 1]) {
                            WALL -> {}
                            STILL_WATER -> {}
                            FLOW_WATER -> {
                                drop = true
                            }

                            else -> {
                                drop(tx, ty + 1)

                                if (map[tx to ty + 1] == FLOW_WATER) {
                                    drop = true
                                    break
                                }
                            }
                        }
                    }
                }
            }

            return tx.takeIf { !drop }
        }

        //drop
        while (true) {
            if (ty > rangeY.last) {
                return
            }

            map[x to ty] = FLOW_WATER

            ty++
            when (map[x to ty]) {
                WALL -> {
                    break
                }

                STILL_WATER -> {
                    break
                }

                FLOW_WATER -> {
                    return
                }

                else -> {
                }
            }
        }

        while (true) {
            ty--

            if (ty < y) {
                return
            }

            val leftBorder = scanBorder(-1, x)
            val rightBorder = scanBorder(1, x)

            if (leftBorder != null && rightBorder != null) {
                (leftBorder + 1 until rightBorder).forEach {
                    map[it to ty] = STILL_WATER
                }
            } else {
                break
            }
        }
    }

    drop(500, 0)

//    printMap()

    println(map.count { (key, value) -> key.second in rangeY && (value == STILL_WATER || value == FLOW_WATER) })
    println(map.count { (key, value) -> key.second in rangeY && value == STILL_WATER })
}