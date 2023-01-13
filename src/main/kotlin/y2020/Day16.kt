package y2020

import util.input

fun main() {
    val rangeRegex = "(.*): (\\d+)-(\\d+) or (\\d+)-(\\d+)".toRegex()

    var readingStep = 0

    val rangeMap = arrayListOf<Pair<String, List<IntRange>>>()
    var myTicket = intArrayOf()
    val tickets = arrayListOf<IntArray>()
    input.forEach {
        if (it.isBlank()) {
            readingStep++
            return@forEach
        }

        when (readingStep) {
            0 -> {
                val match = rangeRegex.matchEntire(it)?.groupValues?.drop(1) ?: return

                rangeMap += match[0] to listOf(match[1].toInt()..match[2].toInt(), match[3].toInt()..match[4].toInt())
            }

            1 -> {
                if (!it.endsWith(":")) {
                    myTicket = it.split(",").map { it.toInt() }.toIntArray()

                    tickets += myTicket
                }
            }

            2 -> {
                if (!it.endsWith(":")) {
                    tickets += it.split(",").map { it.toInt() }.toIntArray()
                }
            }
        }
    }

    println(tickets.sumOf {
        it.filter { n ->
            rangeMap.none { (_, ranges) ->
                ranges.any {
                    n in it
                }
            }
        }.sum()
    })

    tickets.removeIf {
        it.any { n ->
            rangeMap.none { (_, ranges) ->
                ranges.any {
                    n in it
                }
            }
        }
    }

    val possibilities = myTicket.indices.map { index ->
        rangeMap.filter { (field, ranges) ->
            tickets.all {
                ranges.any { range ->
                    it[index] in range
                }
            }
        }.map { it.first }.toMutableSet()
    }

    val result = Array(possibilities.size) { "" }

    while (true) {
        val index = possibilities.indexOfFirst { it.size == 1 }.takeIf { it >= 0 } ?: break

        result[index] = possibilities[index].first()

        possibilities.forEach {
            it.remove(result[index])
        }
    }

    println(result.withIndex().filter { it.value.startsWith("departure") }.fold(1L) { a, b ->
        a * myTicket[b.index]
    })
}