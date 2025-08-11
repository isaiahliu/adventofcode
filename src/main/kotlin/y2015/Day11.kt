package y2015

import util.expect
import util.input

fun main() {
    expect("", "") {
        val invalidChars = setOf('i', 'o', 'l')

        val chars = input.first().toCharArray()

        fun CharArray.valid(): Boolean {
            var seq = false
            val dup = hashSetOf<Char>()
            var diff2 = -1
            var prev = Char(0)
            for (c in this) {
                when (c) {
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

        fun CharArray.increase() {
            do {
                this[this.lastIndex]++

                for (index in this.lastIndex downTo 0) {
                    when {
                        this[index] <= 'z' -> {
                            if (this[index] in invalidChars) {
                                this[index]++
                            }
                            break
                        }

                        else -> {
                            this[index] = 'a'
                            this[index - 1]++
                        }
                    }
                }
            } while (!valid())
        }

        chars.increase()
        part1Result = chars.concatToString()

        chars.increase()
        part2Result = chars.concatToString()
    }
}

