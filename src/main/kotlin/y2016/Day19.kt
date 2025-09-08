package y2016

import util.expect
import util.input

fun main() {
    expect(0, 0) {
        val count = input.first().toInt()

        var elves = Array(count) {
            (it + 1) % count
        }

        while (elves[part1Result] != part1Result) {
            elves[part1Result] = elves[elves[part1Result]]

            part1Result = elves[part1Result]
        }

        part1Result++

        elves = Array(count) {
            (it + 1) % count
        }

        part2Result = count / 2 - 1

        if (count % 2 == 1) {
            elves[part2Result] = elves[elves[part2Result]]
            part2Result += 2
        }

        while (elves[elves[part2Result]] != part2Result) {
            elves[part2Result] = elves[elves[elves[part2Result]]]

            part2Result = elves[part2Result]
        }

        part2Result++
    }
}
