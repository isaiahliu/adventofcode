package y2020

import util.input

fun main() {
    val nums = input.map { it.toInt() }.sorted()

    val diff = (1 until nums.size).map { nums[it] - nums[it - 1] }

    println((diff.count { it == 1 } + 1) * (diff.count { it == 3 } + 1))

    var currentNum = 0
    var currentGroup = arrayListOf(currentNum)
    val groups = arrayListOf<List<Int>>(currentGroup)

    nums.forEach {
        if (it - currentNum == 3) {
            currentGroup = arrayListOf()
            groups += currentGroup
        }
        currentGroup += it

        currentNum = it
    }

    fun List<Int>.calculatePossibilities(): Int {
        var result = 0

        if (this.size > 2) {
            repeat(1 shl this.size - 2) { p ->
                var success = true

                var current = this[0]
                for (pos in 1 until this.size - 1) {
                    if (p and (1 shl (pos - 1)) > 0) {
                        val t = this[pos]
                        if (t - current > 3) {
                            success = false
                            break
                        } else {
                            current = t
                        }
                    }
                }

                if (success && this.last() - current <= 3) {
                    result++
                }
            }
        } else {
            result++
        }

        return result
    }

    val g = groups.map { it.calculatePossibilities() }

    println(groups.fold(1L) { a, b ->
        a * b.calculatePossibilities()
    })
}