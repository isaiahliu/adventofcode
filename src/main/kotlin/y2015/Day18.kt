package y2015

import util.expect
import util.input

fun main() {
    val size = 100
    fun process(vararg fixed: Pair<Int, Int>): Int {
        var lights = hashSetOf(*fixed)

        input.forEachIndexed { r, line ->
            line.forEachIndexed { c, ch ->
                if (ch == '#') {
                    lights += r to c
                }
            }
        }

        repeat(100) {
            val newState = hashSetOf(*fixed)
            repeat(size) { r ->
                repeat(size) { c ->
                    when (arrayOf(
                        r - 1 to c - 1, r - 1 to c, r - 1 to c + 1, r to c - 1, r to c + 1, r + 1 to c - 1, r + 1 to c, r + 1 to c + 1
                    ).count { it in lights }) {
                        3 -> newState += r to c
                        2 if r to c in lights -> newState += r to c
                    }
                }
            }

            lights = newState
        }

        return lights.size
    }

    expect(0, 0) {
        part1Result = process()
        part2Result = process(
            0 to 0, 0 to size - 1, size - 1 to 0, size - 1 to size - 1
        )
    }
}