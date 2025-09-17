package y2016

import util.expect
import util.input
import java.util.*

fun main() {
    expect(0, 0) {
        val regex = "/dev/grid/node-x(\\d+)-y(\\d+)\\s+(\\d+)T\\s+(\\d+)T\\s+\\d+T\\s+\\d+%".toRegex()

        val frees = PriorityQueue<Int>()
        val uses = PriorityQueue<Int>()

        var freeNode = 0 to 0
        val forbidden = hashSetOf<Pair<Int, Int>>()

        val (maxX, maxY) = regex.matchEntire(input.last())?.let {
            it.groupValues.drop(1).take(2).map { it.toInt() }
        } ?: return@expect

        input.forEach {
            val match = regex.matchEntire(it) ?: return@forEach

            val (x, y, total, used) = match.groupValues.drop(1).map { it.toInt() }

            if (used > 0) {
                uses.add(used)
                if (used * 2 <= total) {
                    part1Result--
                }
            }

            if (used == 0) {
                freeNode = x to y
            }

            frees.add(total - used)

            if (used > 100) {
                forbidden += x to y
            }
        }

        while (uses.isNotEmpty() && frees.isNotEmpty()) {
            val used = uses.poll()
            while (frees.isNotEmpty() && used > frees.peek()) {
                frees.poll()
            }

            part1Result += frees.size
        }

        fun bfs(fromPos: Pair<Int, Int>, toPos: Pair<Int, Int>, lockPos: Pair<Int, Int>? = null): List<Pair<Int, Int>> {
            val routes = Array(maxY + 1) { arrayOfNulls<List<Pair<Int, Int>>>(maxX + 1) }
            routes[fromPos.second][fromPos.first] = listOf(fromPos)

            val tasks = LinkedList<Pair<Int, Int>>()
            tasks.add(fromPos)

            while (routes[toPos.second][toPos.first] == null) {
                val (x, y) = tasks.poll()
                val route = routes[y][x].orEmpty()

                arrayOf(x - 1 to y, x + 1 to y, x to y - 1, x to y + 1).forEach { (nx, ny) ->
                    val newPos = nx to ny
                    if (nx in 0..maxX && ny in 0..maxY && routes[ny][nx] == null && newPos !in forbidden && newPos != lockPos) {
                        routes[ny][nx] = route + newPos
                        tasks.add(newPos)
                    }
                }
            }

            return routes[toPos.second][toPos.first].orEmpty()
        }

        bfs(maxX to 0, 0 to 0).reduce { from, to ->
            part2Result += bfs(freeNode, to, from).size

            freeNode = from
            to
        }
    }
}
