package y2018

import util.input
import kotlin.math.absoluteValue

fun main() {
    val inputMap = input.map { it.split(", ").map { it.toInt() } }

    val rowRange = 0..inputMap.maxOf { it[1] }
    val columnRange = 0..inputMap.maxOf { it[0] }

    val map = Array(rowRange.last + 1) {
        LongArray(columnRange.last + 1)
    }

    val nums = hashSetOf<Long>()
    inputMap.forEachIndexed { index, (columnIndex, rowIndex) ->
        val num = 1L shl index
        map[rowIndex][columnIndex] = num

        nums += num
    }

    var width = 1

    while (true) {
        val appendMap = Array(map.size) {
            LongArray(map[it].size)
        }

        fun fill(rowIndex: Int, columnIndex: Int, value: Long) {
            if (rowIndex in rowRange && columnIndex in columnRange) {
                if (map[rowIndex][columnIndex] == 0L) {
                    appendMap[rowIndex][columnIndex] += value
                }
            }
        }

        inputMap.forEachIndexed { index, (columnIndex, rowIndex) ->
            val num = 1L shl index

            for (n in 0 until width) {
                fill(rowIndex + n, columnIndex - width + n, num)
                fill(rowIndex + width - n, columnIndex + n, num)
                fill(rowIndex - n, columnIndex + width - n, num)
                fill(rowIndex - width + n, columnIndex - n, num)
            }
        }

        appendMap.forEachIndexed { rowIndex, row ->
            row.forEachIndexed { columnIndex, ap ->
                when (ap.countOneBits()) {
                    0 -> {}
                    1 -> {
                        map[rowIndex][columnIndex] += ap
                    }

                    else -> {
                        map[rowIndex][columnIndex] = -1
                    }
                }
            }
        }

        if (map.all { it.all { it != 0L } }) {
            break
        }

        width++
    }

//    map.forEach {
//        println(it.joinToString("") { if (it > 0L) "#" else "." })
//    }

    for (t in rowRange) {
        nums -= map[t][0]
        nums -= map[t][columnRange.last]
    }

    for (t in columnRange) {
        nums -= map[0][t]
        nums -= map[rowRange.last][t]
    }

    println(nums.maxOf { num ->
        map.sumOf { it.count { it == num } }
    })

    val distanceMap = Array(map.size) { rowIndex ->
        IntArray(map[rowIndex].size) { columnIndex ->
            inputMap.sumOf { (c, r) ->
                (c - columnIndex).absoluteValue + (r - rowIndex).absoluteValue
            }
        }
    }

    println(distanceMap.sumOf { it.count { it < 10000 } })
}