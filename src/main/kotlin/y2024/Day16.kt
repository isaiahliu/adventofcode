package y2024

import util.expect
import util.input
import java.util.*

fun main() {
    expect(0, 0) {
        val NORTH = 0
        val EAST = 1
        val SOUTH = 2
        val WEST = 3

        val walls = hashSetOf<Pair<Int, Int>>()

        var start = 0 to 0
        var end = 0 to 0
        input.forEachIndexed { r, row ->
            row.forEachIndexed { c, ch ->
                when (ch) {
                    '#' -> walls.add(r to c)
                    'S' -> start = r to c
                    'E' -> end = r to c
                }
            }
        }

        val bestRoutes = hashSetOf<Pair<Int, Int>>()

        data class Node(val r: Int, val c: Int, val direction: Int, val point: Int) {
            val pos = r to c
            val state = pos to direction

            val parents: MutableSet<Node> = hashSetOf()
            var filled = false
            fun forward(): Node {
                return when (direction) {
                    NORTH -> copy(r = r - 1, point = point + 1).also { it.parents += this }
                    EAST -> copy(c = c + 1, point = point + 1).also { it.parents += this }
                    SOUTH -> copy(r = r + 1, point = point + 1).also { it.parents += this }
                    else -> copy(c = c - 1, point = point + 1).also { it.parents += this }
                }
            }

            fun turnLeft(): Node {
                return copy(direction = (direction - 1).mod(4), point = point + 1000).also { it.parents += this }
            }

            fun turnRight(): Node {
                return copy(direction = (direction + 1).mod(4), point = point + 1000).also { it.parents += this }
            }

            fun fillBestRoutes() {
                if (!filled) {
                    bestRoutes += pos

                    parents.forEach {
                        it.fillBestRoutes()
                    }
                    filled = true
                }
            }
        }

        val visited = hashMapOf<Pair<Pair<Int, Int>, Int>, Node>()
        val queue = PriorityQueue<Node>(compareBy { it.point })
        queue.add(Node(start.first, start.second, EAST, 0))

        var maxPoint = Int.MAX_VALUE
        while (queue.isNotEmpty()) {
            val node = queue.poll()
            val visitedNode = visited[node.state]
            when {
                node.point > maxPoint -> break
                node.pos in walls -> {}
                node.pos == end -> {
                    part1Result = node.point
                    maxPoint = node.point
                    node.fillBestRoutes()
                }

                visitedNode?.point == node.point -> {
                    visitedNode.parents += node.parents
                }

                visitedNode == null -> {
                    visited[node.state] = node
                    node.forward().takeIf { it.pos !in walls }?.also { queue.add(it) }
                    node.turnLeft().takeIf { it.pos !in walls }?.also { queue.add(it) }
                    node.turnRight().takeIf { it.pos !in walls }?.also { queue.add(it) }
                }
            }
        }
        part2Result = bestRoutes.size
    }
}