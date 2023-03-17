package y2015

import util.input

fun main() {
    val regex = "(\\w+) => (\\w+)".toRegex()

    val replacements1 = arrayListOf<Pair<String, String>>()

    input.forEach {
        val match = regex.matchEntire(it) ?: return@forEach

        replacements1 += match.groupValues[1] to match.groupValues[2]
    }

    val line = input.last()

    val resultSet1 = hashSetOf<String>()

    replacements1.forEach { pair ->
        val nodes = line.split(pair.first)

        if (nodes.size == 1) {
            return@forEach
        }

        repeat(nodes.lastIndex) { pos ->
            resultSet1 += buildString {
                nodes.forEachIndexed { index, node ->
                    if (index > 0) {
                        if (index - 1 == pos) {
                            append(pair.second)
                        } else {
                            append(pair.first)
                        }
                    }
                    append(node)
                }
            }
        }
    }
    val part1Result = resultSet1.size

    val replacements2 = replacements1.sortedByDescending { it.second.length - it.first.length }

    fun process(line: String, step: Int): Int {
        if (line == "e") {
            return step
        }

        for ((first, second) in replacements2) {
            val nodes = line.split(second)

            if (nodes.size == 1) {
                continue
            }
            repeat(nodes.lastIndex) {
                val result = buildString {
                    nodes.forEachIndexed { index, node ->
                        if (index > 0) {
                            append(
                                if (index - 1 == it) {
                                    first
                                } else {
                                    second
                                }
                            )
                        }
                        append(node)
                    }
                }

                process(result, step + 1).takeIf { it > 0 }?.also { return it }
            }
        }

        return -1
    }

    val step = process(line, 0)

    println(part1Result)
    println(step)
}