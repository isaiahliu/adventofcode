package y2015

import input

fun main() {
    var part1Sum = 0
    var part2Sum = 0
    input.forEach {
        part1Sum += (it.length - it.replace("\\\\", " ").replace("\\\"", " ").replace("\\\\x..".toRegex(), " ").length + 2)

        part2Sum += it.count { it == '"' || it == '\\' } + 2
    }

    println(part1Sum)
    println(part2Sum)
}