package y2015

import util.expect
import util.input

fun main() {
    expect("", "") {
        val chars = input.first().toCharArray()

        chars.increase()
        part1Result = chars.concatToString()

        chars.increase()
        part2Result = chars.concatToString()
    }
}

private fun CharArray.increase() {
    do {
        this[this.lastIndex]++

        for (index in this.lastIndex downTo 0) {
            if (this[index] <= 'z') {
                break
            } else {
                this[index] = 'a'
                this[index - 1]++
            }
        }
    } while (!valid)
}

private val invalidChars = setOf('i', 'o', 'l')

private val CharArray.valid: Boolean
    get() {
        var seq = false
        val dup = hashSetOf<Char>()
        var diff2 = -1
        var prev = Char(0)
        for (c in this) {
            when (c) {
                in invalidChars -> {
                    return false
                }

                prev -> {
                    dup += c
                }

                prev + 1 -> {
                    seq = diff2 == 1
                }
            }

            diff2 = c - prev
            prev = c
        }

        return seq && dup.size >= 2
    }

