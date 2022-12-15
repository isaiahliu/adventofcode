package y2017

import util.input

fun main() {
    val step = input.first().toInt()
//    val step = 3

    var currentNum = 0
    var currentNode = Day17Node(currentNum++)
    currentNode.next = currentNode

    while (currentNum <= 2017) {
        repeat(step % currentNum) {
            currentNode = currentNode.next
        }

        currentNode = Day17Node(currentNum++, currentNode)
    }

    println(currentNode.next.value)

    var currentIndex = 0

    var result = -1
    repeat(50_000_000) {
        currentIndex = (currentIndex + step) % (it + 1) + 1

        if (currentIndex == 1) {
            result = it + 1
        }
    }

    println(result)
}

private class Day17Node(val value: Int, initPrevious: Day17Node? = null) {
    init {
        initPrevious?.also {
            this.next = it.next
            it.next = this
        }
    }

    private lateinit var _next: Day17Node


    var next: Day17Node
        get() {
            return _next
        }
        set(value) {
            _next = value
        }
}