package y2016

import util.expect
import util.input

fun main() {
    expect(0L, 0L) {
        val line = input.first()

        fun dfs(startIndex: Int, endIndex: Int, times: Long) {
            var index = startIndex

            val compressInfo = intArrayOf(0, 0)
            var fillingInfoIndex = 0
            while (index <= endIndex) {
                when (val ch = line[index++]) {
                    '(' -> {
                        compressInfo[0] = 0
                        compressInfo[1] = 0
                        fillingInfoIndex = 0
                    }

                    ')' -> {
                        if (times == 1L) {
                            part1Result += compressInfo[0] * compressInfo[1]
                        }

                        dfs(index, index + compressInfo[0] - 1, times * compressInfo[1])

                        index += compressInfo[0]
                    }

                    'x' -> {
                        fillingInfoIndex++
                    }

                    in '0'..'9' -> {
                        compressInfo[fillingInfoIndex] = compressInfo[fillingInfoIndex] * 10 + (ch - '0')
                    }

                    else -> {
                        if (times == 1L) {
                            part1Result++
                        }

                        part2Result += times
                    }
                }
            }
        }

        dfs(0, line.lastIndex, 1)
    }
}