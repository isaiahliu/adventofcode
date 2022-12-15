package y2017

import util.input

fun main() {
    val line = input.first()

    val chars = ('a'..'p').map { it }.toCharArray()
    var startIndex = 0

    val path = arrayListOf<String>()

    var cycleStart: Int = -1
    var cycle: Int = -1
    while (cycleStart < 0) {
        line.split(",").forEach {
            when (it[0]) {
                's' -> {
                    startIndex += (0 - it.substring(1).toInt()).mod(chars.size)
                }

                'x' -> {
                    val (a, b) = it.substring(1).split("/").map { it.toInt() }

                    val t = chars[(a + startIndex) % chars.size]
                    chars[(a + startIndex) % chars.size] = chars[(b + startIndex) % chars.size]
                    chars[(b + startIndex) % chars.size] = t
                }

                'p' -> {
                    val (a, b) = it.substring(1).split("/").map { it[0] }

                    val aIndex = chars.indexOf(a)
                    val bIndex = chars.indexOf(b)

                    chars[aIndex] = b
                    chars[bIndex] = a
                }
            }
        }

        val result = String(chars.indices.map { chars[(it + startIndex) % chars.size] }.toCharArray())

        if (path.contains(result)) {
            cycleStart = path.indexOf(result)
            cycle = path.size - cycleStart
        } else {
            path += result
        }
    }

    println(path[0])
    println(path[cycleStart + (1_000_000_000 - cycleStart - 1) % cycle])
}