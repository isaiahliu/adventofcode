package y2025

import util.expect
import util.input
import kotlin.math.absoluteValue

fun main() {
    expect(0L, 0L) {
        val nodes = input.map { it.split(",").let { it[0].toInt() to it[1].toInt() } }

        val startIndex = nodes.indices.minWith(compareBy<Int> { nodes[it].second }.thenBy { nodes[it].first })
        val d = nodes[(startIndex + 1) % nodes.size].let { next ->
            if (next.second == nodes[startIndex].second && next.first > nodes[startIndex].first) {
                1
            } else {
                -1
            }
        }

        class QuartNode(val minX: Int, val minY: Int, val maxX: Int, val maxY: Int, val mark: Boolean) {
            val children = arrayListOf<QuartNode>()

            fun horizontalSplit(y: Int, fromX: Int, toX: Int, bottomMark: Boolean) {
                when {
                    y !in minY..maxY || fromX > maxX || toX < minX -> Unit
                    !children.isEmpty() -> {
                        children.forEach {
                            it.horizontalSplit(y, fromX, toX, bottomMark)
                        }
                    }

                    else -> {
                        children.add(QuartNode(minX, minY, maxX, y - 1, !bottomMark))
                        children.add(QuartNode(minX, y, maxX, y, true))
                        children.add(QuartNode(minX, y + 1, maxX, maxY, bottomMark))
                    }
                }
            }

            fun verticalSplit(x: Int, fromY: Int, toY: Int, rightMark: Boolean) {
                when {
                    x !in minX..maxX || fromY >= maxY || toY <= minY -> Unit
                    !children.isEmpty() -> {
                        children.forEach {
                            it.verticalSplit(x, fromY, toY, rightMark)
                        }
                    }

                    else -> {
                        children.add(QuartNode(minX, minY, x - 1, maxY, !rightMark))
                        children.add(QuartNode(x, minY, x, maxY, true))
                        children.add(QuartNode(x + 1, minY, maxX, maxY, rightMark))
                    }
                }
            }

            fun sum(leftX: Int, topY: Int, rightX: Int, bottomY: Int): Long {
                return when {
                    leftX > maxX || rightX < minX || topY > maxY || bottomY < minY -> {
                        0L
                    }

                    children.isNotEmpty() -> {
                        children.sumOf {
                            it.sum(leftX, topY, rightX, bottomY)
                        }
                    }

                    mark -> {
                        (minOf(rightX, maxX) - maxOf(leftX, minX) + 1L) * (minOf(bottomY, maxY) - maxOf(topY, minY) + 1L)
                    }

                    else -> 0L
                }
            }
        }

        val root = QuartNode(0, 0, Int.MAX_VALUE, Int.MAX_VALUE, false)

        repeat(nodes.size) { index ->
            val (cx, cy) = nodes[(startIndex + index * d).mod(nodes.size)]
            val (nx, ny) = nodes[(startIndex + (index + 1) * d).mod(nodes.size)]

            when {
                nx > cx -> root.horizontalSplit(cy, cx, nx, true)
                nx < cx -> root.horizontalSplit(cy, nx, cx, false)
                ny > cy -> root.verticalSplit(cx, cy, ny, false)
                else -> root.verticalSplit(cx, ny, cy, true)
            }
        }

        for (i in nodes.indices) {
            for (j in i + 1 until nodes.size) {
                val (x1, y1) = nodes[i]
                val (x2, y2) = nodes[j]

                val totalSize = ((x1 - x2).absoluteValue + 1L) * ((y1 - y2).absoluteValue + 1L)
                part1Result = maxOf(part1Result, totalSize)
                val matchSize = root.sum(minOf(x1, x2), minOf(y1, y2), maxOf(x1, x2), maxOf(y1, y2))
                if (totalSize == matchSize) {
                    part2Result = maxOf(part2Result, matchSize)
                }
            }
        }
    }
}


