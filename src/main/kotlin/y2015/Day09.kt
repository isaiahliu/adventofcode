package y2015

import util.input

fun main() {
    val regex = "(.*) to (.*) = (.*)".toRegex()

    val distanceMap = hashMapOf<String, MutableMap<String, Int>>()

    input.forEach {
        val match = regex.matchEntire(it) ?: return

        val city1 = match.groupValues[1]
        val city2 = match.groupValues[2]
        val distance = match.groupValues[3].toInt()

        distanceMap.computeIfAbsent(city1) { hashMapOf() }[city2] = distance
        distanceMap.computeIfAbsent(city2) { hashMapOf() }[city1] = distance
    }

    val cities = distanceMap.keys.toTypedArray()
    val cityCount = cities.size

    val distances = Array(cityCount) { x ->
        val cityX = cities[x]
        IntArray(cityCount) { y ->
            val cityY = cities[y]

            distanceMap[cityX]?.get(cityY) ?: 0
        }
    }

    val route = IntArray(cityCount)

    var min = Int.MAX_VALUE
    var max = 0

    var rowIndex = 0
    var columnIndex = 0

    while (true) {
        if (columnIndex == cityCount) {
            rowIndex--
            if (rowIndex < 0) {
                break
            } else {
                columnIndex = route[rowIndex] + 1
                continue
            }
        }

        if (rowIndex == cityCount) {
            val distance = (1 until cityCount).sumOf { distances[route[it]][route[it - 1]] }

            min = min.coerceAtMost(distance)
            max = max.coerceAtLeast(distance)

            rowIndex -= 2
            columnIndex = route[rowIndex] + 1
            continue
        }

        if (route.take(rowIndex).any { it == columnIndex }) {
            columnIndex++
        } else {
            route[rowIndex] = columnIndex
            rowIndex++
            columnIndex = 0
        }
    }

    println(min)
    println(max)
}