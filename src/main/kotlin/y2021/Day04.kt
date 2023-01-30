package y2021

import util.input

fun main() {
    val drawNums = input.first().split(",").map { it.toInt() }

    val boards: MutableList<MutableMap<Int, Pair<Int, Int>>> = arrayListOf()

    var board = hashMapOf<Int, Pair<Int, Int>>()

    val regex = "\\s*(\\d+)\\s*(\\d+)\\s*(\\d+)\\s*(\\d+)\\s*(\\d+)\\s*".toRegex()
    input.drop(1).mapNotNull {
        regex.matchEntire(it)?.groupValues?.drop(1)?.map { it.toInt() }
    }.forEach {
        val row = board.size / 5

        it.forEachIndexed { column, num ->
            board[num] = row to column
        }

        if (row == 4) {
            boards += board
            board = hashMapOf()
        }
    }

    var result1 = 0
    var result2 = 0
    for (drawNum in drawNums) {
        if (boards.isEmpty()) {
            break
        }

        boards.forEach {
            it -= drawNum
        }

        val winners = boards.filter {
            it.values.map { it.first }.distinct().size < 5 || it.values.map { it.second }.distinct().size < 5
        }.toSet()

        winners.firstOrNull()?.also {
            result2 = it.keys.sum() * drawNum
        }

        if (result1 == 0 && result2 > 0) {
            result1 = result2
        }

        boards -= winners
    }

    println(result1)
    println(result2)
}

