package y2016

import util.input

fun main() {
    val regex = "\\s*(\\d+)\\s*(\\d+)\\s*(\\d+)".toRegex()

    var result1 = 0
    var result2 = 0

    val column1 = arrayListOf<Int>()
    val column2 = arrayListOf<Int>()
    val column3 = arrayListOf<Int>()
    input.forEach {
        val match = regex.matchEntire(it) ?: return

        val sides = match.groupValues.drop(1).take(3).map { it.toInt() }.also {
            column1 += it[0]
            column2 += it[1]
            column3 += it[2]
        }.sorted()

        if (sides[0] + sides[1] > sides[2]) {
            result1++
        }
    }

    val temp = arrayListOf<Int>()
    fun read(columns: List<Int>) {
        columns.forEach {
            temp += it

            if (temp.size == 3) {
                val sides = temp.sorted()

                if (sides[0] + sides[1] > sides[2]) {
                    result2++
                }

                temp.clear()
            }
        }
    }

    read(column1)
    read(column2)
    read(column3)

    println(result1)
    println(result2)
}