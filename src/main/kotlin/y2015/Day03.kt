package y2015

import input

fun main() {
    val part1Positions = hashSetOf<String>()
    val part2Positions = hashSetOf<String>()

    var part1X = 0
    var part1Y = 0

    fun part1Position(): String {
        return "${part1X}_${part1Y}"
    }

    var part2X1 = 0
    var part2Y1 = 0
    var part2X2 = 0
    var part2Y2 = 0

    fun part2Position1(): String {
        return "${part2X1}_${part2Y1}"
    }

    fun part2Position2(): String {
        return "${part2X2}_${part2Y2}"
    }

    part1Positions += part1Position()
    part2Positions += part2Position1()

    input.first().toCharArray().forEachIndexed { index, direction ->
        when (direction) {
            '^' -> {
                part1Y++

                when (index % 2) {
                    0 -> part2Y1++
                    1 -> part2Y2++
                }
            }

            '>' -> {
                part1X++

                when (index % 2) {
                    0 -> part2X1++
                    1 -> part2X2++
                }
            }

            'v' -> {
                part1Y--

                when (index % 2) {
                    0 -> part2Y1--
                    1 -> part2Y2--
                }
            }

            '<' -> {
                part1X--

                when (index % 2) {
                    0 -> part2X1--
                    1 -> part2X2--
                }
            }
        }

        part1Positions += part1Position()
        part2Positions += part2Position1()
        part2Positions += part2Position2()
    }

    println(part1Positions.size)
    println(part2Positions.size)
}
