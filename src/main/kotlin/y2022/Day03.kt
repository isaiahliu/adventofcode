package y2022

import util.expect
import util.input

fun main() {
    expect(0) {
        val part2Group = arrayListOf<String>()

        input.forEach {
            val length = it.length

            val matchResult = "(\\w{${length / 2}})(\\w{${length / 2}})".toRegex().matchEntire(it)!!

            val left = matchResult.groupValues[1].toCharArray()
            val right = matchResult.groupValues[2].toCharArray()

            part1Result += left.intersect(right.toSet()).sumOf { it.priority }

            part2Group += it

            if (part2Group.size == 3) {
                part2Result += part2Group[0].toCharArray().intersect(part2Group[1].toCharArray().toSet())
                    .intersect(part2Group[2].toCharArray().toSet()).sumOf { it.priority }

                part2Group.clear()
            }
        }
    }
}

private val Char.priority: Int
    get() {
        return when (this) {
            in 'a'..'z' -> {
                this.code - 'a'.code + 1
            }

            in 'A'..'Z' -> {
                this.code - 'A'.code + 26 + 1
            }

            else -> 0
        }
    }
