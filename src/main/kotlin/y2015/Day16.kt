package y2015

import input

fun main() {
    val targetAunt = Aunt(children = 3, cats = 7, samoyeds = 2, pomeranians = 3, akitas = 0, vizslas = 0, goldfish = 5, trees = 3, cars = 2, perfumes = 1)

    val regex = "Sue (\\d+): (\\w+): (\\d+), (\\w+): (\\d+), (\\w+): (\\d+)".toRegex()

    var part1Result = 0
    var part2Result = 0

    input.forEach {
        val match = regex.matchEntire(it) ?: return

        val num = match.groupValues[1].toInt()

        val auntPart1 = targetAunt.copy()
        val auntPart2 = targetAunt.copy()

        fun fillProp(property: String, count: Int) {
            when (property) {
                "children" -> {
                    auntPart1.children = count
                    auntPart2.children = count
                }

                "cats" -> {
                    auntPart1.cats = count
                    if (auntPart2.cats >= count) {
                        auntPart2.cats = -1
                    }
                }

                "trees" -> {
                    auntPart1.trees = count
                    if (auntPart2.trees >= count) {
                        auntPart2.trees = -1
                    }
                }

                "samoyeds" -> {
                    auntPart1.samoyeds = count
                    auntPart2.samoyeds = count
                }

                "pomeranians" -> {
                    auntPart1.pomeranians = count
                    if (auntPart2.pomeranians <= count) {
                        auntPart2.pomeranians = -1
                    }
                }

                "goldfish" -> {
                    auntPart1.goldfish = count
                    if (auntPart2.goldfish <= count) {
                        auntPart2.goldfish = -1
                    }
                }

                "akitas" -> {
                    auntPart1.akitas = count
                    auntPart2.akitas = count
                }

                "vizslas" -> {
                    auntPart1.vizslas = count
                    auntPart2.vizslas = count
                }

                "cars" -> {
                    auntPart1.cars = count
                    auntPart2.cars = count
                }

                "perfumes" -> {
                    auntPart1.perfumes = count
                    auntPart2.perfumes = count
                }
            }
        }

        fillProp(match.groupValues[2], match.groupValues[3].toInt())
        fillProp(match.groupValues[4], match.groupValues[5].toInt())
        fillProp(match.groupValues[6], match.groupValues[7].toInt())

        if (auntPart1 == targetAunt) {
            part1Result = num
        }

        if (auntPart2 == targetAunt) {
            part2Result = num
        }
    }

    println(part1Result)
    println(part2Result)
}

private data class Aunt(
        var children: Int,
        var cats: Int,
        var samoyeds: Int,
        var pomeranians: Int,
        var akitas: Int,
        var vizslas: Int,
        var goldfish: Int,
        var trees: Int,
        var cars: Int,
        var perfumes: Int
)