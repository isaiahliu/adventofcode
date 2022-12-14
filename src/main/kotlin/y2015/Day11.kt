package y2015

import input

fun main() {
    val chars = input.first().toCharArray()

    while (true) {
        chars.increase()

        if (chars.valid) {
            break
        }
    }

    val part1Result = String(chars)

    while (true) {
        chars.increase()

        if (chars.valid) {
            break
        }
    }

    val part2Result = String(chars)

    println(part1Result)
    println(part2Result)
}

private fun CharArray.increase() {
    this[this.size - 1]++
    for (index in this.size - 1 downTo 0) {
        if (this[index] <= 'z') {
            return
        } else {
            this[index] = 'a'
            this[index - 1]++
        }
    }
}

private val invalidChars = arrayOf('i', 'o', 'l')

private val CharArray.valid: Boolean
    get() {
        var seq = false
        val dup = hashSetOf<Char>()
        for ((index, c) in this.withIndex()) {
            if (c in invalidChars) {
                return false
            }

            if (index > 0 && c == this[index - 1]) {
                dup += c
            }

            if (!seq && index > 1 && c - this[index - 1] == 1 && c - this[index - 2] == 2) {
                seq = true
            }
        }

        return seq && dup.size >= 2
    }

