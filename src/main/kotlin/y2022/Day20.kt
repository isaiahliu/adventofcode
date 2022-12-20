package y2022

import util.input

fun main() {
    fun process(key: Int, times: Int): Long {
        val nodes = input.map {
            Day20Node(it.toLong() * key)
        }.toTypedArray()

        val nodeSize = nodes.size

        repeat(nodeSize) {
            nodes[it].next = nodes[(it + 1) % nodeSize]
            nodes[it].previous = nodes[(it - 1).mod(nodeSize)]
        }

        val zeroNode = nodes.first { it.value == 0L }

        var moveIndex = 0
        repeat(nodeSize * times) {
            val a = nodes[moveIndex]

            val moveCount = a.value.mod(nodeSize - 1)
            if (moveCount != 0 && moveCount != nodeSize - 1) {
                var b = a
                repeat(moveCount) {
                    b = b.next
                }

                when (a.next) {
                    b -> {
                        a.previous.next = b
                        b.next.previous = a

                        a.next = b.next
                        b.previous = a.previous

                        b.next = a
                        a.previous = b
                    }

                    else -> {
                        val aPrevious = a.previous
                        val aNext = a.next

                        aPrevious.next = aNext
                        aNext.previous = aPrevious

                        b.next.previous = a
                        a.next = b.next

                        b.next = a
                        a.previous = b
                    }
                }
            }

            moveIndex = (moveIndex + 1) % nodeSize
        }

        var currentNode = zeroNode
        var result = 0L
        repeat(3000) {
            currentNode = currentNode.next

            if (it % 1000 == 999) {
                result += currentNode.value
            }
        }

        return result
    }
    println(process(1, 1))
    println(process(811589153, 10))
}

private class Day20Node(val value: Long) {
    lateinit var previous: Day20Node

    lateinit var next: Day20Node
}