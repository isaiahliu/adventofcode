package y2023

import util.input

fun main() {
    val HIGH_CARD = 1
    val ONE_PAIR = 2
    val TWO_PAIR = 3
    val THREE_OF_A_KIND = 4
    val FULL_HOUSE = 5
    val FOUR_OF_A_KIND = 6
    val FIVE_OF_A_KIND = 7

    val scores = input.map { it.split(" ")[1].toInt() }.toIntArray()
    val cards = input.map { it.split(" ")[0] }.toTypedArray()

    fun Array<String>.calculate(cards: CharArray, wildCard: Char? = null): Int {
        val types = map {
            val patterns = arrayListOf<String>()

            if (wildCard != null && wildCard in it) {
                cards.forEach { c ->
                    if (c != wildCard) {
                        patterns += it.replace(wildCard, c)
                    }
                }
            } else {
                patterns += it
            }

            patterns.maxOf { p ->
                val cardGroups = p.groupingBy { p.indexOf(it) }
                    .eachCount().entries.sortedWith(compareByDescending<Map.Entry<Int, Int>> { it.value }.thenByDescending { it.key })

                val type = when {
                    cardGroups.size == 1 -> FIVE_OF_A_KIND
                    cardGroups[0].value == 4 -> FOUR_OF_A_KIND
                    cardGroups[0].value == 3 && cardGroups[1].value == 2 -> FULL_HOUSE
                    cardGroups[0].value == 3 -> THREE_OF_A_KIND
                    cardGroups[0].value == 2 && cardGroups[1].value == 2 -> TWO_PAIR
                    cardGroups[0].value == 2 -> ONE_PAIR
                    else -> HIGH_CARD
                }

                type
            }
        }.toIntArray()

        val sortedIndices = input.indices.sortedWith(compareBy<Int> { types[it] }.thenComparing { a, b ->
            val left = this[a].map { cards.indexOf(it) }.toIntArray()
            val right = this[b].map { cards.indexOf(it) }.toIntArray()

            for (i in left.indices) {
                (left[i] - right[i]).takeIf { it != 0 }?.also {
                    return@thenComparing it
                }
            }

            0
        })

        return sortedIndices.mapIndexed { index, i ->
            scores[i] * (index + 1)
        }.sum()
    }

    val part1Result = cards.calculate(charArrayOf('2', '3', '4', '5', '6', '7', '8', '9', 'T', 'J', 'Q', 'K', 'A'))
    val part2Result = cards.calculate(charArrayOf('J', '2', '3', '4', '5', '6', '7', '8', '9', 'T', 'Q', 'K', 'A'), 'J')

    println(part1Result)
    println(part2Result)
}
