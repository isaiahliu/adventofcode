package y2022

import input

fun main() {
    val stackSize = (input.first().length + 1) / 4

    val stacksPart1 = Array(stackSize) { arrayListOf<String>() }
    val stacksPart2 = Array(stackSize) { arrayListOf<String>() }

    val regex = "move (\\d+) from (\\d+) to (\\d+)".toRegex()
    var readingBoxes = true
    input.forEach {
        if (readingBoxes) {
            if (it[1] == '1') {
                readingBoxes = false
                return@forEach
            }

            repeat(stackSize) { index ->
                val box = it[index * 4 + 1]
                if (box != ' ') {
                    stacksPart1[index].add(0, box.toString())
                    stacksPart2[index].add(0, box.toString())
                }
            }
        } else {
            val match = regex.matchEntire(it) ?: return@forEach

            val count = match.groupValues[1].toInt()
            val from = match.groupValues[2].toInt() - 1
            val to = match.groupValues[3].toInt() - 1

            stacksPart2[to].addAll(stacksPart2[from].takeLast(count))
            repeat(count) {
                stacksPart1[to] += stacksPart1[from].removeLast()
                stacksPart2[from].removeLast()
            }
        }
    }

    println(stacksPart1.joinToString("") { it.last() })
    println(stacksPart2.joinToString("") { it.last() })
}