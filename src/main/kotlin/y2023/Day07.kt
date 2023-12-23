package y2023

import util.expect
import util.input

fun main() {
    expect(0) {
        val HIGH_CARD = 1
        val ONE_PAIR = 2
        val TWO_PAIR = 3
        val THREE_OF_A_KIND = 4
        val FULL_HOUSE = 5
        val FOUR_OF_A_KIND = 6
        val FIVE_OF_A_KIND = 7

        val cards = input.map { it.split(" ").let { it[0] to it[1].toInt() } }.toTypedArray()

        fun Array<Pair<String, Int>>.calculate(cards: CharArray, wildCard: Char? = null): Int {
            val types = map { (card, _) ->
                val patterns = buildSet {
                    if (wildCard != null && wildCard in card) {
                        cards.forEach {
                            if (it != wildCard) {
                                add(card.replace(wildCard, it))
                            }
                        }
                    } else {
                        add(card)
                    }
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

            return input.indices.sortedWith(compareBy<Int> { types[it] }.thenComparing { a, b ->
                val left = this[a].first.map { cards.indexOf(it) }.toIntArray()
                val right = this[b].first.map { cards.indexOf(it) }.toIntArray()

                for (i in left.indices) {
                    (left[i] - right[i]).takeIf { it != 0 }?.also {
                        return@thenComparing it
                    }
                }

                0
            }).mapIndexed { index, i ->
                this[i].second * (index + 1)
            }.sum()
        }

        part1Result = cards.calculate(charArrayOf('2', '3', '4', '5', '6', '7', '8', '9', 'T', 'J', 'Q', 'K', 'A'))
        part2Result = cards.calculate(charArrayOf('J', '2', '3', '4', '5', '6', '7', '8', '9', 'T', 'Q', 'K', 'A'), 'J')
    }
}