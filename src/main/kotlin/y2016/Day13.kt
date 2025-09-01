package y2016

import util.expect
import util.input
import java.util.*

fun main() {
    expect(0, 0) {
        val favNum = input.first().toInt()
        val target = 31 to 39

        val visited = hashSetOf(1 to 1)
        val tasks = LinkedList<Pair<Int, Int>>()
        tasks.add(1 to 1)

        var step = 1
        while (target !in visited || part2Result == 0) {
            if (target !in visited) {
                part1Result++
            }

            repeat(tasks.size) {
                val (px, py) = tasks.poll()

                arrayOf(px - 1 to py, px + 1 to py, px to py - 1, px to py + 1).filter { (x, y) ->
                    x >= 0 && y >= 0 && (x * x + 3 * x + 2 * x * y + y + y * y + favNum).countOneBits() % 2 == 0 && visited.add(x to y)
                }.forEach {
                    tasks.add(it)
                }
            }

            if (step++ == 50) {
                part2Result = visited.size
            }
        }
    }
}

