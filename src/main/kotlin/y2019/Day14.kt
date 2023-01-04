package y2019

import util.input

fun main() {
    class Node(val name: String) {
        var count = 0

        var formulae: MutableList<Pair<Node, Int>> = arrayListOf()

        var cost = 0
    }

    val nodeMap = hashMapOf<String, Node>()
    input.map { it.split(" ").map { it.trim(',') } }.forEach {
        val (count, name) = it.takeLast(2)
        val node = nodeMap.computeIfAbsent(name) { Node(name) }

        node.count = count.toInt()

        if (it[1] == "ORE") {
            node.cost = it[0].toInt()
        } else {
            val f = it.dropLast(3)

            for (index in f.indices step 2) {
                node.formulae += nodeMap.computeIfAbsent(f[index + 1]) { Node(it) } to f[index].toInt()
            }

        }
    }

    fun process(fuelCount: Long): Long {
        var cost = 0L
        val lacks = hashMapOf(nodeMap["FUEL"]!! to fuelCount)

        while (lacks.any { it.value > 0 }) {
            val (lackNode, lackCount) = lacks.entries.first { it.value > 0 }

            val times = lackCount / lackNode.count + (if (lackCount % lackNode.count == 0L) 0 else 1)

            lacks[lackNode] = lackCount - lackNode.count * times
            if (lackNode.cost > 0) {
                cost += lackNode.cost * times
            } else {
                lackNode.formulae.forEach { (fNode, fCount) ->
                    lacks[fNode] = (lacks[fNode] ?: 0) + times * fCount
                }
            }

        }

        return cost
    }

    val count = process(1)

    println(count)

    val target = 1000000000000L
    var t = (1000000000000L / count * 1.85).toLong()

    while (true) {
        val process = process(t)
        if (process > target) {
            break
        }

        t++
    }

    println(t - 1)
}