package y2015

import util.expect
import util.input

fun main() {
    expect(0, 0) {
        val houses1 = hashSetOf(0 to 0)
        val houses2 = hashSetOf(0 to 0)

        val pos1 = intArrayOf(0, 0)
        val pos2 = arrayOf(intArrayOf(0, 0), intArrayOf(0, 0))

        input.first().toCharArray().forEachIndexed { index, direction ->
            when (direction) {
                '^' -> {
                    pos1[1]++
                    pos2[index % 2][1]++
                }

                '>' -> {
                    pos1[0]++
                    pos2[index % 2][0]++
                }

                'v' -> {
                    pos1[1]--
                    pos2[index % 2][1]--
                }

                '<' -> {
                    pos1[0]--
                    pos2[index % 2][0]--
                }
            }

            houses1 += pos1[0] to pos1[1]
            houses2 += pos2[index % 2][0] to pos2[index % 2][1]
        }

        part1Result = houses1.size
        part2Result = houses2.size
    }
}
