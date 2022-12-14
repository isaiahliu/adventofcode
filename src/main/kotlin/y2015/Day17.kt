package y2015

import input

fun main() {
    val target = 150

    val bottleCounts = input.map { it.toInt() }.groupingBy { it }.eachCount()

    val bottles = bottleCounts.keys.sorted().toTypedArray()

    val usages = IntArray(bottles.size)

    val part2 = IntArray(bottleCounts.values.sum())

    fun calculateScore(): Int {
        return usages.mapIndexed { index, usage ->
            usage * bottles[index]
        }.sum()
    }

    fun calculatePossibility(): Int {
        return usages.mapIndexed { index, usage ->
            var t = 1
            val bottleCount = bottleCounts[bottles[index]] ?: 1
            repeat(usage) {
                t *= (bottleCount - it)
            }
            repeat(usage) {
                t /= (it + 1)
            }

            t
        }.fold(1) { a, b -> a * b }
    }

    var bottleIndex = bottles.size - 1

    var part1Sum = 0
    while (true) {
        val maxCount = bottleCounts[bottles[bottleIndex]] ?: 0
        val usage = usages[bottleIndex]

        if (usage > maxCount) {
            usages[bottleIndex] = 0

            bottleIndex--
            if (bottleIndex < 0) {
                break
            }

            usages[bottleIndex]++
            continue
        }

        val score = calculateScore()
        if (score == target) {
            val possibility = calculatePossibility()
            part1Sum += possibility

            part2[usages.sum()] += possibility
        }
        bottleIndex = bottles.size - 1
        usages[bottleIndex]++
    }

    println(part1Sum)
    println(part2.first { it > 0 })
}

