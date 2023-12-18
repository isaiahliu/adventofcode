package y2023

import util.expectInt
import util.input

fun main() {
    expectInt {
        fun String.hash(): Int {
            return fold(0) { a, b ->
                (a + b.code) * 17 % 256
            }
        }

        val boxes = Array(256) { LinkedHashMap<String, Int>() }
        input[0].split(",").forEach {
            part1Result += it.hash()

            val (node, value) = it.split('=', '-')

            val box = boxes[node.hash()]

            value.toIntOrNull()?.also {
                box[node] = it
            } ?: run {
                box -= node
            }
        }

        part2Result = boxes.mapIndexed { boxIndex, nodes ->
            nodes.entries.mapIndexed { nodeIndex, (_, value) ->
                value * (nodeIndex + 1) * (boxIndex + 1)
            }.sum()
        }.sum()
    }
}