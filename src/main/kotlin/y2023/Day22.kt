package y2023

import util.expectInt
import util.input
import kotlin.math.sign

fun main() {
    expectInt {
        class Brick(line: String) {
            var x: IntRange
            var y: IntRange
            var z: IntRange

            init {
                line.split("~").also { (from, to) ->
                    val (x1, y1, z1) = from.split(",").map { it.toInt() }
                    val (x2, y2, z2) = to.split(",").map { it.toInt() }

                    x = x1..x2
                    y = y1..y2
                    z = z1..z2
                }
            }

            val parents = hashSetOf<Brick>()
        }

        val bricks = input.map { Brick(it) }.sortedBy { it.z.first() }

        val floors = hashMapOf<Pair<Int, Int>, Brick>()

        bricks.forEach { brick ->
            var maxZ = 0
            brick.x.forEach { x ->
                brick.y.forEach { y ->
                    floors[x to y]?.also {
                        if (it.z.last > maxZ) {
                            maxZ = it.z.last
                            brick.parents.clear()
                        }

                        if (it.z.last == maxZ) {
                            brick.parents.add(it)
                        }
                    }

                    floors[x to y] = brick
                }
            }

            brick.z = maxZ + 1..maxZ + 1 + brick.z.last - brick.z.first
        }

        bricks.forEachIndexed { index, brick ->
            val removed = hashSetOf(brick)
            var result = 0
            for (j in index + 1 until bricks.size) {
                val checkBrick = bricks[j]

                if (checkBrick.parents.isNotEmpty() && checkBrick.parents.all { it in removed }) {
                    result++
                    removed += checkBrick
                }
            }

            part1Result += result.sign xor 1
            part2Result += result
        }
    }
}
