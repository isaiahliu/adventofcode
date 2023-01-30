package y2021

import util.input

fun main() {
    val nums = input.map {
        it.map { it - '0' }.toIntArray()
    }.toTypedArray()

    var result1 = 0
    val basinSizes = arrayListOf<Int>()
    nums.forEachIndexed { rowIndex, row ->
        row.forEachIndexed { columnIndex, num ->
            if (arrayOf(
                    rowIndex - 1 to columnIndex,
                    rowIndex + 1 to columnIndex,
                    rowIndex to columnIndex - 1,
                    rowIndex to columnIndex + 1
                ).map { (r, c) ->
                    nums.getOrNull(r)?.getOrNull(c) ?: 10
                }.all { num < it }
            ) {
                result1 += num + 1

                val basin = hashSetOf(rowIndex to columnIndex)
                val tasks = hashMapOf(rowIndex to columnIndex to num)

                while (tasks.isNotEmpty()) {
                    val current = tasks.toList()
                    tasks.clear()

                    for ((pos, n) in current) {
                        arrayOf(
                            pos.first - 1 to pos.second,
                            pos.first + 1 to pos.second,
                            pos.first to pos.second - 1,
                            pos.first to pos.second + 1
                        ).mapNotNull { (r, c) ->
                            nums.getOrNull(r)?.getOrNull(c)?.takeIf { it < 9 }?.takeIf { it > n }
                                ?.takeIf { basin.add(r to c) }?.let { r to c to it }
                        }.forEach { (pos, n) ->
                            tasks[pos] = n
                        }
                    }
                }

                basinSizes += basin.size
            }
        }
    }

    println(result1)

    println(basinSizes.sortedDescending().take(3).fold(1) { a, b -> a * b })
}