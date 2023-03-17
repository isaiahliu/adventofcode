package y2016

import util.input

fun main() {
    val regex = "/dev/grid/node-x(\\d+)-y(\\d+)\\s+(\\d+)T\\s+(\\d+)T\\s+\\d+T\\s+\\d+%".toRegex()

    val (maxX, maxY) = regex.matchEntire(input.last())?.groupValues?.drop(1).orEmpty()

    val gridList = arrayListOf<Grid>()

    val grids = Array(maxY.toInt() + 1) {
        Array(maxX.toInt() + 1) { Grid(0, 0) }
    }

    input.forEach {
        val match = regex.matchEntire(it) ?: return@forEach

        val (x, y, total, used) = match.groupValues.drop(1)

        grids[y.toInt()][x.toInt()] = Grid(total.toInt(), used.toInt()).also {
            gridList += it
        }
    }

    fun Array<Array<Grid>>.dupGrids(): Array<Array<Grid>> {
        return Array(this.size) {
            val row = this[it]
            Array(row.size) {
                row[it].copy()
            }
        }
    }

    var result1 = 0
    for (x in gridList.indices) {
        for (y in x + 1 until gridList.size) {
            if (gridList[x].used > 0 && gridList[x].used <= gridList[y].available || gridList[y].used > 0 && gridList[y].used <= gridList[x].available) {
                result1++
            }
        }
    }

    println(result1)

    val goal = grids[0].last()
    goal.goal = true

    val swapSpace = gridList.first { it.used == 0 }.total
    val goalSpace = goal.used

    var swapSpaceX = 0
    var swapSpaceY = 0

    val mark = grids.mapIndexed { y, row ->
        row.mapIndexed { x, grid ->
            when {
                grid.goal -> "G"
                grid.used == 0 -> {
                    swapSpaceX = x
                    swapSpaceY = y

                    "_"
                }

                grid.used <= swapSpace && grid.total >= goalSpace -> "."
                else -> "#"
            }
        }.toTypedArray()
    }.toTypedArray()

    fun findPath(
        fromX: Int,
        fromY: Int,
        excludeX: Int = Int.MAX_VALUE,
        excludeY: Int = Int.MAX_VALUE
    ): Array<IntArray> {
        val result = Array(mark.size) {
            IntArray(mark[it].size) {
                Int.MAX_VALUE
            }
        }

        result[fromY][fromX] = 0

        val tasks = arrayListOf(fromX to fromY)

        while (tasks.isNotEmpty()) {
            val current = tasks.toList()
            tasks.clear()

            for ((x, y) in current) {
                val currentValue = result[y][x]

                result.getOrNull(y - 1)?.getOrNull(x)
                    ?.takeIf { !(y - 1 == excludeY && x == excludeX) && mark[y - 1][x] != "#" && it > currentValue + 1 }
                    ?.also {
                        result[y - 1][x] = currentValue + 1
                        tasks += x to y - 1
                    }

                result.getOrNull(y + 1)?.getOrNull(x)
                    ?.takeIf { !(y + 1 == excludeY && x == excludeX) && mark[y + 1][x] != "#" && it > currentValue + 1 }
                    ?.also {
                        result[y + 1][x] = currentValue + 1
                        tasks += x to y + 1
                    }

                result.getOrNull(y)?.getOrNull(x - 1)
                    ?.takeIf { !(y == excludeY && x - 1 == excludeX) && mark[y][x - 1] != "#" && it > currentValue + 1 }
                    ?.also {
                        result[y][x - 1] = currentValue + 1
                        tasks += x - 1 to y
                    }

                result.getOrNull(y)?.getOrNull(x + 1)
                    ?.takeIf { !(y == excludeY && x + 1 == excludeX) && mark[y][x + 1] != "#" && it > currentValue + 1 }
                    ?.also {
                        result[y][x + 1] = currentValue + 1
                        tasks += x + 1 to y
                    }
            }
        }

        return result
    }

    val path = findPath(0, 0)

    var goalX = grids[0].lastIndex
    var goalY = 0
    var currentPath = path[goalY][goalX]

    var result2 = 0
    while (goalX > 0 || goalY > 0) {
        val swapPath = findPath(swapSpaceX, swapSpaceY, goalX, goalY)
        swapSpaceX = goalX
        swapSpaceY = goalY
        if (path.getOrNull(goalY - 1)?.getOrNull(goalX) == currentPath - 1) {
            goalY--
        } else if (path.getOrNull(goalY + 1)?.getOrNull(goalX) == currentPath - 1) {
            goalY++
        } else if (path.getOrNull(goalY)?.getOrNull(goalX - 1) == currentPath - 1) {
            goalX--
        } else if (path.getOrNull(goalY)?.getOrNull(goalX + 1) == currentPath - 1) {
            goalX++
        }

        currentPath--

        result2++
        result2 += swapPath[goalY][goalX]
    }

    println(result2)
}

private data class Grid(val total: Int, var used: Int, var goal: Boolean = false) {
    val available: Int get() = total - used
}