package y2016

import util.expect
import util.input
import util.md5
import java.util.*

fun main() {
    expect(0, 0) {
        val salt = input.first()

        fun process(additionalHashTimes: Int): Int {
            val tripleChars = hashMapOf<Int, Char>()
            val pentChars = hashMapOf<Char, LinkedList<Int>>()

            var foundKeys = 0
            var startSuffix = -1
            var endSuffix = 0

            while (foundKeys < 64) {
                startSuffix++

                while (endSuffix - startSuffix < 1000) {
                    var lastChar = ' '
                    var count = 0

                    var pentChar: Char? = null
                    var tripleChar: Char? = null

                    var t = "${salt}${endSuffix}".md5
                    repeat(additionalHashTimes) {
                        t = t.md5
                    }

                    t.forEach {
                        if (it == lastChar) {
                            count++

                            when (count) {
                                3 -> {
                                    tripleChar = tripleChar ?: it
                                }

                                5 -> {
                                    pentChar = pentChar ?: it
                                }
                            }
                        } else {
                            lastChar = it
                            count = 1
                        }
                    }

                    tripleChar?.also {
                        tripleChars[endSuffix] = it
                    }

                    pentChar?.also {
                        pentChars.computeIfAbsent(it) { LinkedList() } += endSuffix
                    }

                    endSuffix++
                }

                tripleChars[startSuffix]?.let {
                    pentChars[it]
                }?.takeIf {
                    it.last() > startSuffix
                }?.also {
                    foundKeys++
                }
            }

            return startSuffix
        }

        part1Result = process(0)
        part2Result = process(2016)
    }
}
