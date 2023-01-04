package y2019

import util.input

fun main() {
    val layers = arrayListOf<Array<IntArray>>()

    var layerIndex = 0

    val width = 25
    val height = 6

    val line = input.first()
    while (true) {
        val nodes = line.drop(layerIndex * width * height).take(width * height)

        if (nodes.isEmpty()) {
            break
        }

        layers += Array(height) { rowIndex ->
            IntArray(width) { columnIndex ->
                nodes[rowIndex * width + columnIndex] - '0'
            }
        }

        layerIndex++
    }

    val layer = layers.minBy { it.sumOf { it.count { it == 0 } } }

    println(layer.sumOf { it.count { it == 1 } } * layer.sumOf { it.count { it == 2 } })

    val result = Array(height) { rowIndex ->
        IntArray(width) { columnIndex ->
            layers.map { it[rowIndex][columnIndex] }.first { it != 2 }
        }
    }

    result.forEach {
        println(it.joinToString("") {
            when (it) {
                0 -> " "
                else -> "#"
            }
        })
    }
}
