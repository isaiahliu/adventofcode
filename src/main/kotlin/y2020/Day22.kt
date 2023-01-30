package y2020

import util.input
import java.util.*

fun main() {
    fun process(init: Array<out List<Int>>, game: Int, debug: Boolean = false): Int {
        val playersRoute = Array(init.size) { hashSetOf<String>() }

        val decks = init.map { LinkedList(it) }.toTypedArray()

        if (debug) {
            println("Game ${game}")
        }
        while (decks.all { it.isNotEmpty() }) {
            if (game > 0) {
                if (!playersRoute.withIndex().all { (index, route) -> route.add(decks[index].joinToString()) }) {
                    return 0
                }
                if (debug) {
                    println("${decks[0]} | ${decks[1]}")
                }
            }

            val draws = decks.map { queue ->
                queue.pop()
            }

            val winnerIndex = if (game > 0) {
                if (draws.withIndex().all { (index, value) -> decks[index].size >= value }) {
                    process(
                        decks.mapIndexed { index, deck -> deck.take(draws[index]) }.toTypedArray(),
                        game + 1,
                        debug
                    ).also {
                        if (debug) {
                            println("Back to Game ${game}")
                        }
                    }
                } else {
                    draws.indexOf(draws.max())
                }
            } else {
                draws.indexOf(draws.max())
            }

            decks[winnerIndex].add(draws[winnerIndex])
            decks[winnerIndex].add(draws[1 - winnerIndex])
        }

        return if (game > 1) {
            decks.indexOfFirst { it.isNotEmpty() }
        } else {
            val winner = decks.first { it.isNotEmpty() }

            winner.reversed().withIndex().sumOf { (index, value) ->
                (index + 1) * value
            }
        }
    }

    val decks: Array<MutableList<Int>> = Array(2) { arrayListOf() }

    var deckIndex = -1

    input.forEach {
        if (it.startsWith("Player")) {
            deckIndex++
        } else {
            it.toIntOrNull()?.also {
                decks[deckIndex] += it
            }
        }
    }

    println(process(decks, 0))
    println(process(decks, 1))
}