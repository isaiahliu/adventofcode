package y2018

import util.input

fun main() {
    val regex = "(\\d+) players; last marble is worth (\\d+) points".toRegex()
    val (players, points) = input.first().let { regex.matchEntire(it) }?.groupValues?.drop(1)?.map { it.toInt() }
        .orEmpty()

    class Day09Node(val value: Int) {
        lateinit var previous: Day09Node
        lateinit var next: Day09Node
    }

    fun process(times: Int): Long {
        val scores = LongArray(players)

        var playerIndex = 0

        var currentNode = Day09Node(0).also {
            it.previous = it
            it.next = it
        }

        repeat(points * times + 1) {
            if (it == 0) {
                return@repeat
            }

            if (it % 23 == 0) {
                scores[playerIndex] += it.toLong()

                repeat(7) {
                    currentNode = currentNode.previous
                }

                scores[playerIndex] += currentNode.value.toLong()

                currentNode.previous.next = currentNode.next
                currentNode.next.previous = currentNode.previous
                currentNode = currentNode.next
            } else {
                val n1 = currentNode.next
                val n2 = n1.next

                val newNode = Day09Node(it)
                n1.next = newNode
                newNode.previous = n1
                n2.previous = newNode
                newNode.next = n2

                currentNode = newNode
            }

            playerIndex = (playerIndex + 1) % players
        }
        return scores.max()
    }

    println(process(1))
    println(process(100))
}

