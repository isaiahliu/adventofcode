package y2022

import util.input

fun main() {
    val sumList = arrayListOf<Int>()
    var sum = 0
    input.forEach {
        if (it.isEmpty()) {
            sumList += sum
            sum = 0
        } else {
            sum += it.toInt()
        }
    }

    sumList.sortDescending()

    val part1Result = sumList.first()
    val part2Result = sumList.take(3).sum()

    println(part1Result)
    println(part2Result)
}
