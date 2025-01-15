package y2015

import util.expect
import util.input

fun main() {
    expect(0, 0) {
        val part1Positions = hashSetOf(0 to 0)
        val part2Positions = hashSetOf(0 to 0)

        val part1 = intArrayOf(0, 0)
        val part2 = arrayOf(intArrayOf(0, 0), intArrayOf(0, 0))

        input.first().toCharArray().forEachIndexed { index, direction ->
            when (direction) {
                '^' -> {
                    part1[1]++
                    part2[index % 2][1]++
                }

                '>' -> {
                    part1[0]++
                    part2[index % 2][0]++
                }

                'v' -> {
                    part1[1]--
                    part2[index % 2][1]--
                }

                '<' -> {
                    part1[0]--
                    part2[index % 2][0]--
                }
            }

            part1Positions += part1[0] to part1[1]

            part2.forEach {
                part2Positions += it[0] to it[1]
            }
        }

        part1Result = part1Positions.size
        part2Result = part2Positions.size
    }
}
