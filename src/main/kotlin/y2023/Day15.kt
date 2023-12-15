package y2023

import util.input

fun main() {
    fun String.hash(): Int {
        return fold(0) { a, b ->
            (a + b.code) * 17 % 256
        }
    }

    var part1Result = 0
    val boxes = Array(256) { LinkedHashMap<String, Int>() }
    input[0].split(",").forEach {
        part1Result += it.hash()

        val (node, value) = it.split('=', '-')

        val hash = node.hash()

        value.toIntOrNull()?.also {
            boxes[hash][node] = it
        } ?: run {
            boxes[hash] -= node
        }
    }

    val part2Result = boxes.mapIndexed { boxIndex, nodes ->
        nodes.entries.mapIndexed { nodeIndex, (_, value) ->
            value * (nodeIndex + 1) * (boxIndex + 1)
        }.sum()
    }.sum()
    println(part1Result)
    println(part2Result)
}
