package y2016

import util.input

fun main() {
    fun decompress(line: String, applyDecompressRecursively: Boolean = false): Long {
        var result = 0L

        var inParentheses = false
        var index = 0

        val repeatStr = StringBuilder()
        while (index < line.length) {
            val char = line[index]
            when {
                char == '(' -> {
                    inParentheses = true
                    repeatStr.clear()
                }

                inParentheses && char == ')' -> {
                    inParentheses = false

                    val (a, b) = repeatStr.split("x")

                    result += if (applyDecompressRecursively) {
                        decompress(line.substring(index + 1, index + 1 + a.toInt()), true) * b.toInt()
                    } else {
                        a.toLong() * b.toInt()
                    }

                    index += a.toInt()
                }

                else -> {
                    if (inParentheses) {
                        repeatStr.append(char)
                    } else {
                        result++
                    }
                }
            }
            index++
        }

        return result
    }

    val line = input.first()
    val part1Result = decompress(line)
    val part2Result = decompress(line, true)

    println(part1Result)
    println(part2Result)
}