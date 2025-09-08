package y2016

import util.expect
import util.input
import util.md5
import java.util.*

fun main() {
    expect("", 0) {
        val line = input.first()

        val directions = arrayOf(
            -1 to 0 to 'U',
            1 to 0 to 'D',
            0 to -1 to 'L',
            0 to 1 to 'R',
        )
        val tasks = LinkedList<Pair<Pair<Int, Int>, String>>()
        tasks.add(0 to 0 to "")

        while (tasks.isNotEmpty()) {
            val (pos, step) = tasks.poll()
            val (r, c) = pos

            if (r + c == 6) {
                if (part1Result.isEmpty()) {
                    part1Result = step
                }
                part2Result = step.length
                continue
            }
            val md5 = "$line$step".md5

            directions.forEachIndexed { index, (offset, direction) ->
                val newR = r + offset.first
                val newC = c + offset.second

                if (md5[index] in 'b'..'f' && newR in 0..3 && newC in 0..3) {
                    tasks.add(newR to newC to step + direction)
                }
            }
        }
    }
}