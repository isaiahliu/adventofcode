package y2022

import util.expect
import util.input

fun main() {
    expect(0) {
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

        part1Result = sumList.first()
        part2Result = sumList.take(3).sum()
    }
}