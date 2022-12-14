package y2015

import util.input

fun main() {
    var line = input.first()

    var part1Result = 0

    val nums = arrayListOf<Int>()

    repeat(50) {
        val counts = IntArray(line.length)
        nums.clear()

        if (it == 40) {
            part1Result = line.length
        }

        var previousChar = ' '
        var index = -1
        line.forEach {
            if (it == previousChar) {
                counts[index]++
            } else {
                nums.add(it - '0')
                counts[++index] = 1
            }

            previousChar = it
        }

        line = nums.mapIndexed { index, i -> "${counts[index]}${i}" }.joinToString("")
    }

    val part2Result = line.length

    println(part1Result)
    println(part2Result)
}

