package y2015

import util.expect
import util.input

fun main() {
    expect("", "") {
        val targetAunt: Map<String, Pair<Int, (Int, Int) -> Boolean>> = mapOf(
            "children" to (3 to Int::equals),
            "cats" to (7 to { a, b -> a > b }),
            "samoyeds" to (2 to Int::equals),
            "pomeranians" to (3 to { a, b -> a < b }),
            "akitas" to (0 to Int::equals),
            "vizslas" to (0 to Int::equals),
            "goldfish" to (5 to { a, b -> a < b }),
            "trees" to (3 to { a, b -> a > b }),
            "cars" to (2 to Int::equals),
            "perfumes" to (1 to Int::equals),
        )

        val regex = "Sue (\\d+): (\\w+): (\\d+), (\\w+): (\\d+), (\\w+): (\\d+)".toRegex()

        input.forEach {
            val match = regex.matchEntire(it) ?: return@expect

            var sameCount = 0
            var likeCount = 0
            arrayOf(
                match.groupValues[2] to match.groupValues[3].toInt(),
                match.groupValues[4] to match.groupValues[5].toInt(),
                match.groupValues[6] to match.groupValues[7].toInt(),
            ).forEach { (key, value) ->
                val (num, comp) = targetAunt[key] ?: return@forEach
                if (num == value) {
                    sameCount++
                }

                if (comp(value, num)) {
                    likeCount++
                }
            }

            if (sameCount == 3) {
                part1Result = match.groupValues[1]
            }

            if (likeCount == 3) {
                part2Result = match.groupValues[1]
            }
        }
    }
}
