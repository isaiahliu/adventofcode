package y2022

import util.input

fun main() {
    val grids = input.map { it.split(",").map { it.toInt() }.toIntArray() }

    val maxX = grids.maxOf { it[0] }
    val maxY = grids.maxOf { it[1] }
    val maxZ = grids.maxOf { it[2] }

    val space = Array(maxX + 3) { _ ->
        Array(maxY + 3) { _ ->
            IntArray(maxZ + 3) { _ ->
                -1
            }
        }
    }

    grids.forEach { (x, y, z) ->
        space[x + 1][y + 1][z + 1] = 0
    }

    fun walk(x: Int, y: Int, z: Int) {
        when (space.getOrNull(x)?.getOrNull(y)?.getOrNull(z) ?: return) {
            -1 -> {
                space[x][y][z] = 999

                walk(x - 1, y, z)
                walk(x + 1, y, z)
                walk(x, y - 1, z)
                walk(x, y + 1, z)
                walk(x, y, z - 1)
                walk(x, y, z + 1)
            }

            999 -> {}
            else -> {
                space[x][y][z]++
            }
        }
    }

    walk(0, 0, 0)
    val result2 = space.sumOf { it.sumOf { it.filter { it in 0..6 }.sum() } }

    while (true) {
        var found = false
        for ((xIndex, y) in space.withIndex()) {
            for ((yIndex, z) in y.withIndex()) {
                for ((zIndex, it) in z.withIndex()) {
                    if (it < 0) {
                        walk(xIndex, yIndex, zIndex)
                        found = true
                    }
                }
            }
        }

        if (!found) {
            break
        }
    }
    val result1 = space.sumOf { it.sumOf { it.filter { it in 0..6 }.sum() } }

    println(result1)
    println(result2)
}