package y2023

import util.expectLong
import util.input

fun main() {
    expectLong {
        val strs = arrayListOf<String>()

        fun Array<CharArray>.findMirror(targetDiffCount: Int): Int? {
            return (1 until size).firstOrNull {
                var index1 = it - 1
                var index2 = it

                var diffCount = 0
                while (index1 >= 0 && index2 < size && diffCount <= targetDiffCount) {
                    val row1 = this[index1--]
                    val row2 = this[index2++]

                    diffCount += row1.indices.count { row1[it] != row2[it] }
                }

                diffCount == targetDiffCount
            }
        }

        (input + "").forEach {
            if (it.isEmpty()) {
                val rows = Array(strs.size) { r ->
                    CharArray(strs[r].length) { c ->
                        strs[r][c]
                    }
                }

                val columns = Array(strs[0].length) { c ->
                    CharArray(strs.size) { r ->
                        strs[r][c]
                    }
                }

                rows.findMirror(0)?.also {
                    part1Result += it * 100
                }

                columns.findMirror(0)?.also {
                    part1Result += it
                }

                rows.findMirror(1)?.also {
                    part2Result += it * 100
                }

                columns.findMirror(1)?.also {
                    part2Result += it
                }

                strs.clear()
            } else {
                strs += it
            }
        }
    }
}