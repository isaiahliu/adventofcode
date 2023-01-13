package y2020

import util.input

fun main() {
    val cube3D = hashSetOf<Pair<Pair<Int, Int>, Int>>()
    val cube4D = hashSetOf<Pair<Pair<Pair<Int, Int>, Int>, Int>>()

    input.forEachIndexed { y, row ->
        row.forEachIndexed { x, c ->
            if (c == '#') {
                cube3D += x to y to 0
                cube4D += x to y to 0 to 0
            }
        }
    }

    repeat(6) {
        val actives3D = hashMapOf<Pair<Pair<Int, Int>, Int>, Int>()

        cube3D.forEach { (xy, z) ->
            val (x, y) = xy

            for (z1 in z - 1..z + 1) {
                for (y1 in y - 1..y + 1) {
                    for (x1 in x - 1..x + 1) {
                        if (x1 != x || y1 != y || z1 != z) {
                            actives3D[x1 to y1 to z1] = (actives3D[x1 to y1 to z1] ?: 0) + 1
                        }
                    }
                }
            }
        }

        cube3D.toSet().forEach { pos ->
            if ((actives3D[pos] ?: 0) !in 2..3) {
                cube3D -= pos
            }
        }
        cube3D += (actives3D.filterValues { it == 3 }.keys)

        val actives4D = hashMapOf<Pair<Pair<Pair<Int, Int>, Int>, Int>, Int>()

        cube4D.forEach { (xyz, m) ->
            val (xy, z) = xyz
            val (x, y) = xy

            for (m1 in m - 1..m + 1) {
                for (z1 in z - 1..z + 1) {
                    for (y1 in y - 1..y + 1) {
                        for (x1 in x - 1..x + 1) {
                            if (x1 != x || y1 != y || z1 != z || m1 != m) {
                                actives4D[x1 to y1 to z1 to m1] = (actives4D[x1 to y1 to z1 to m1] ?: 0) + 1
                            }
                        }
                    }
                }
            }
        }

        cube4D.toSet().forEach { pos ->
            if ((actives4D[pos] ?: 0) !in 2..3) {
                cube4D -= pos
            }
        }
        cube4D += (actives4D.filterValues { it == 3 }.keys)
    }

    println(cube3D.size)
    println(cube4D.size)
}