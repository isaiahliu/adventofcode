package y2015

import util.input

fun main() {
    val happinessMap = hashMapOf<String, MutableMap<String, Int>>()

    val regex = "(\\w+) would (gain|lose) (\\d+) happiness units? by sitting next to (\\w+).".toRegex()
    input.forEach {
        val match = regex.matchEntire(it) ?: return

        val from = match.groupValues[1]
        val to = match.groupValues[4]
        val happiness = match.groupValues[3].toInt() * if (match.groupValues[2] == "gain") 1 else -1

        happinessMap.computeIfAbsent(from) { hashMapOf() }[to] = happiness
    }

    val people = happinessMap.keys.toTypedArray()
    val peopleCount = people.size

    val happiness = Array(peopleCount) { x ->
        IntArray(peopleCount) { y ->
            happinessMap[people[x]]!![people[y]] ?: 0
        }
    }

    val seat = IntArray(peopleCount)

    var maxPart1 = 0
    var maxPart2 = 0

    var rowIndex = 0
    var columnIndex = 0

    while (true) {
        if (columnIndex == peopleCount) {
            rowIndex--
            if (rowIndex < 0) {
                break
            } else {
                columnIndex = seat[rowIndex] + 1
                continue
            }
        }

        if (rowIndex == peopleCount) {
            val scorePart1 =
                (0 until peopleCount).sumOf { happiness[seat[it]][seat[(it + 1) % peopleCount]] } + (0 until peopleCount).sumOf { happiness[seat[(it + 1) % peopleCount]][seat[it]] }
            val scorePart2 = (0 until peopleCount).sumOf {
                seat.getOrNull(it + 1)?.let { t -> happiness[seat[it]][t] } ?: 0
            } + (0 until peopleCount).sumOf {
                seat.getOrNull(it + 1)?.let { t -> happiness[t][seat[it]] } ?: 0
            }

            maxPart1 = maxPart1.coerceAtLeast(scorePart1)
            maxPart2 = maxPart2.coerceAtLeast(scorePart2)

            rowIndex -= 2
            columnIndex = seat[rowIndex] + 1
            continue
        }

        if (seat.take(rowIndex).any { it == columnIndex }) {
            columnIndex++
        } else {
            seat[rowIndex] = columnIndex
            rowIndex++
            columnIndex = 0
        }
    }

    println(maxPart1)
    println(maxPart2)
}
