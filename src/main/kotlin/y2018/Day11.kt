package y2018

import util.input

fun main() {
    val serialNo = input.first().toInt()

    val grids = Array(300) { y ->
        IntArray(300) { x ->
            ((x + 1 + 10) * (y + 1) + serialNo) * (x + 1 + 10) / 100 % 10 - 5
        }
    }

    fun sum(x: Int, y: Int, size: Int): Int {
        var result = 0
        repeat(size) { x1 ->
            repeat(size) { y1 ->
                result += grids[y + y1][x + x1]
            }
        }
        return result
    }

    val nodes = (0..297).map { x ->
        (0..297).map { y -> x to y }
    }.flatten()

    val maxNode = nodes.maxBy { (x, y) -> sum(x, y, 3) }

    println("${maxNode.first + 1},${maxNode.second + 1}")

    val nodes2 = (1..300).map { size ->
        (0..300 - size).map { x ->
            (0..300 - size).map { y -> x to y }
        }.flatten().map {
            it to size
        }
    }.flatten()

    val maxNode2 = nodes2.maxBy { (pair, size) -> sum(pair.first, pair.second, size) }
    println("${maxNode2.first.first + 1},${maxNode2.first.second + 1},${maxNode2.second}")
}