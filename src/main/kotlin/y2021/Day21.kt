package y2021

import util.input

fun main() {
    val init = input.map { it.split(" ").last().toInt() - 1 }.toIntArray()

    val pos = init.map { it }.toIntArray()

    val scores = IntArray(2)

    var rollTimes = 0
    var currentRoll = 1
    while (true) {
        val player = (rollTimes++) % 2

        pos[player] += currentRoll * 3 + 3
        pos[player] %= 10

        scores[player] += pos[player] + 1

        if (scores[player] >= 1000) {
            break
        }

        currentRoll += 3
    }

    println(scores.min() * rollTimes * 3)

    val possibles = hashMapOf(
        3 to 1, 4 to 3, 5 to 6, 6 to 7, 7 to 6, 8 to 3, 9 to 1
    )

    fun calculatePossibilities(
        currentPos: Int, currentScore: Int = 0, route: Map<Int, Long> = mapOf(0 to 1)
    ): Pair<Map<Int, Long>, Map<Int, Long>> {
        val success = hashMapOf<Int, Long>()
        val fail = hashMapOf<Int, Long>()

        possibles.forEach { (roll, times) ->
            val newPos = (currentPos + roll) % 10

            val newScore = currentScore + newPos + 1

            if (newScore >= 21) {
                route.forEach { (rollTimes, count) ->
                    success[rollTimes + 1] = (success[rollTimes + 1] ?: 0L) + count * times
                }
            } else {
                route.forEach { (rollTimes, count) ->
                    fail[rollTimes + 1] = (fail[rollTimes + 1] ?: 0L) + count * times
                }

                val (newSuccess, newFail) = calculatePossibilities(newPos, newScore, route.map { (key, value) ->
                    key + 1 to value * times
                }.toMap())

                newSuccess.forEach { (rollTimes, count) ->
                    success[rollTimes] = (success[rollTimes] ?: 0L) + count
                }

                newFail.forEach { (rollTimes, count) ->
                    fail[rollTimes] = (fail[rollTimes] ?: 0L) + count
                }
            }
        }

        return success to fail
    }

    val (p1, p2) = init.map { calculatePossibilities(it) }

    var p1Win = 0L
    var p2Win = 0L
    p1.first.forEach { (rollTimes, p) ->
        p1Win += (p2.second[rollTimes - 1] ?: 0L) * p
    }

    p2.first.forEach { (rollTimes, p) ->
        p2Win += (p1.second[rollTimes] ?: 0L) * p
    }

    println(p1Win.coerceAtLeast(p2Win))
}