package y2019

import util.input

fun main() {
    fun Int.isBug(index: Int): Boolean {
        return this and (1 shl index) > 0
    }

    fun Int.isBug(rowIndex: Int, columnIndex: Int): Boolean {
        return isBug(rowIndex * 5 + columnIndex)
    }

    var map = input.joinToString("").replace('#', '1').replace('.', '0').reversed().toInt(2)

    val cache = hashSetOf<Int>()
    while (true) {
        val bugCounts = Array(25) {
            val r = it / 5
            val c = it % 5

            arrayOf(r - 1 to c, r to c - 1, r to c + 1, r + 1 to c).filter {
                it.first in 0 until 5 && it.second in 0 until 5
            }.count {
                map.isBug(it.first, it.second)
            }
        }

        map = bugCounts.mapIndexed { index, count ->
            if (if (map.isBug(index)) count == 1 else count in 1..2) 1 shl index else 0
        }.sum()

        if (!cache.add(map)) {
            break
        }
    }
    println(map)

    val bugList = arrayListOf(input.joinToString("").replace('#', '1').replace('.', '0').reversed().toInt(2))

    repeat(200) {
        if (bugList.first() > 0) {
            bugList.add(0, 0)
        }

        if (bugList.last() > 0) {
            bugList.add(0)
        }

        val bugsCounts = arrayListOf<IntArray>()

        bugList.indices.forEach {
            val previousMap = bugList.getOrNull(it - 1) ?: 0
            val thisMap = bugList[it]
            val nextMap = bugList.getOrNull(it + 1) ?: 0

            val bugsCount = IntArray(25).also { bugsCounts += it }

            (0 until 5).forEach { rowIndex ->
                (0 until 5).forEach { columnIndex ->
                    if (rowIndex != 2 || columnIndex != 2) {
                        val index = rowIndex * 5 + columnIndex

                        bugsCount[index] = arrayOf(
                            rowIndex - 1 to columnIndex,
                            rowIndex to columnIndex - 1,
                            rowIndex to columnIndex + 1,
                            rowIndex + 1 to columnIndex
                        ).sumOf { (r, c) ->
                            when {
                                r < 0 -> {
                                    if (previousMap.isBug(1, 2)) 1 else 0
                                }

                                c < 0 -> {
                                    if (previousMap.isBug(2, 1)) 1 else 0
                                }

                                r > 4 -> {
                                    if (previousMap.isBug(3, 2)) 1 else 0
                                }

                                c > 4 -> {
                                    if (previousMap.isBug(2, 3)) 1 else 0
                                }

                                r == 2 && c == 2 -> {
                                    when {
                                        rowIndex == 1 -> {
                                            (0 until 5).count { nextMap.isBug(0, it) }
                                        }

                                        rowIndex == 3 -> {
                                            (0 until 5).count { nextMap.isBug(4, it) }
                                        }

                                        columnIndex == 1 -> {
                                            (0 until 5).count { nextMap.isBug(it, 0) }
                                        }

                                        columnIndex == 3 -> {
                                            (0 until 5).count { nextMap.isBug(it, 4) }
                                        }

                                        else -> throw RuntimeException("Error")
                                    }
                                }

                                else -> {
                                    if (thisMap.isBug(r, c)) 1 else 0
                                }
                            }
                        }
                    }
                }
            }
        }

        bugsCounts.forEachIndexed { bugIndex, bc ->
            val t = bugList[bugIndex]
            bugList[bugIndex] = bc.mapIndexed { index, count ->
                if (if (t.isBug(index)) count == 1 else count in 1..2) 1 shl index else 0
            }.sum()
        }
    }

    println(bugList.sumOf { it.countOneBits() })
}