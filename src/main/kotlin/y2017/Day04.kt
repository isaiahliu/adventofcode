package y2017

import util.expect
import util.input

fun main() {
    expect(0, 0) {
        input.forEach {
            val str1 = StringBuilder()
            val str2 = IntArray(26)
            val set1 = hashSetOf<String>()
            val set2 = hashSetOf<String>()

            var valid1 = 1
            var valid2 = 1
            "${it} ".forEach {
                if (it == ' ') {
                    if (!set1.add(str1.toString())) {
                        valid1 = 0
                    }

                    if (!set2.add(buildString {
                            str2.forEachIndexed { index, count ->
                                repeat(count) {
                                    append('a' + index)
                                }

                                str2[index] = 0
                            }
                        })) {
                        valid2 = 0
                    }

                    str1.clear()
                } else {
                    str1.append(it)
                    str2[it - 'a']++
                }
            }

            part1Result += valid1
            part2Result += valid2
        }
    }
}