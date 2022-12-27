package y2018

import util.input

fun main() {
    val regex = "Step (\\w+) must be finished before step (\\w+) can begin.".toRegex()

    val map = hashMapOf<String, MutableSet<String>>()

    val awaitingNodes = hashSetOf<String>()
    input.mapNotNull { regex.matchEntire(it)?.groupValues?.drop(1) }.forEach { (from, to) ->
        map.computeIfAbsent(to) { hashSetOf() } += from

        awaitingNodes += from
        awaitingNodes += to
    }

    var sortedNodes = awaitingNodes.sorted().toMutableList()

    val route = arrayListOf<String>()

    while (sortedNodes.isNotEmpty()) {
        val node = sortedNodes.first {
            map[it].orEmpty().all { it in route }
        }

        route += node
        sortedNodes -= node
    }

    println(route.joinToString(""))

    sortedNodes = awaitingNodes.sorted().toMutableList()
    route.clear()

    val elves = arrayOfNulls<Pair<String, Int>>(5)
    var seconds = 0
    while (sortedNodes.isNotEmpty()) {
        for (index in elves.indices) {
            elves[index]?.also { (node, time) ->
                elves[index] = if (time == 1) {
                    route += node
                    sortedNodes -= node
                    null
                } else {
                    node to time - 1
                }
            }
        }

        val idleElfIndex = elves.indices.filter { elves[it] == null }
        sortedNodes.filter {
            it !in elves.mapNotNull { it?.first } && map[it].orEmpty().all { it in route }
        }.take(idleElfIndex.size).forEachIndexed { index, node ->
            elves[idleElfIndex[index]] = node to node[0] - 'A' + 1 + 60
        }

        seconds++
    }

    println(seconds - 1)
}