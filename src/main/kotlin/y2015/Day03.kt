package y2015

import util.expect
import util.input

fun main() {
    expect(0, 0) {
        val houses = arrayOf(hashSetOf(0 to 0), hashSetOf(0 to 0))
        val pos2 = arrayOf(intArrayOf(0, 0), intArrayOf(0, 0))
        val positions = arrayOf(intArrayOf(0, 0), intArrayOf(0, 0))

        input.first().toCharArray().forEachIndexed { index, direction ->
            positions[1] = pos2[index % 2]

            positions.forEachIndexed { index, p ->
                when (direction) {
                    '^' -> {
                        p[1]++
                    }

                    '>' -> {
                        p[0]++
                    }

                    'v' -> {
                        p[1]--
                    }

                    '<' -> {
                        p[0]--
                    }
                }

                houses[index] += p[0] to p[1]
            }
        }

        part1Result = houses[0].size
        part2Result = houses[1].size
    }
}
