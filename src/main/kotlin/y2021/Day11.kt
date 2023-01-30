package y2021

import util.input

fun main() {
    val nums = input.map { it.map { it - '0' }.toIntArray() }.toTypedArray()

    var result1 = 0

    var step = 0
    while (true) {
        step++

        nums.forEachIndexed { rowIndex, row ->
            row.forEachIndexed { columnIndex, num ->
                nums[rowIndex][columnIndex] = num + 1
            }
        }

        while (true) {
            var flash = 0

            nums.forEachIndexed { r, row ->
                row.forEachIndexed { c, num ->
                    if (num > 9) {
                        nums[r][c] = -1
                        flash++

                        arrayOf(
                            r - 1 to c - 1,
                            r - 1 to c,
                            r - 1 to c + 1,
                            r to c - 1,
                            r to c + 1,
                            r + 1 to c - 1,
                            r + 1 to c,
                            r + 1 to c + 1
                        ).filter {
                            nums.getOrNull(it.first)?.getOrNull(it.second)?.takeIf { it >= 0 } != null
                        }.forEach {
                            nums[it.first][it.second]++
                        }
                    }
                }
            }

            if (flash == 0) {
                break
            }

            if (step <= 100) {
                result1 += flash
            }
        }

        var allFlash = true
        nums.forEachIndexed { rowIndex, row ->
            row.forEachIndexed { columnIndex, num ->
                if (num < 0) {
                    nums[rowIndex][columnIndex] = 0
                } else {
                    allFlash = false
                }
            }
        }

        if (allFlash) {
            break
        }
    }

    println(result1)
    println(step)
}