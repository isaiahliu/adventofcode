package y2016

import util.input

fun main() {
    val favNum = input.first().toInt()

    val targetX = 31
    val targetY = 39

    fun Pair<Int, Int>.isOpen(): Boolean {
        val (x, y) = this

        return if (x < 0 || y < 0) {
            false
        } else {
            (x * x + 3 * x + 2 * x * y + y + y * y + favNum).countOneBits() % 2 == 0
        }
    }

    val walked = hashSetOf<String>()

    val current = arrayListOf(1 to 1)

    fun Pair<Int, Int>.canWalk(): Boolean {
        return walked.add("${first}_${second}")
    }

    var step = 0

    var part1Result = -1
    var part2Result = -1

    while (part1Result < 0 || part2Result < 0) {
        if (step == 50) {
            part2Result = walked.size
        }
        val tasks = current.toList()

        current.clear()

        for (pair in tasks) {
            val (x, y) = pair

            if (part1Result < 0 && x == targetX && y == targetY) {
                part1Result = step
            }

            (x - 1 to y).takeIf { it.isOpen() && it.canWalk() }?.also { current += it }
            (x + 1 to y).takeIf { it.isOpen() && it.canWalk() }?.also { current += it }
            (x to y - 1).takeIf { it.isOpen() && it.canWalk() }?.also { current += it }
            (x to y + 1).takeIf { it.isOpen() && it.canWalk() }?.also { current += it }
        }

        step++
    }


    println(part1Result)
    println(part2Result)
}

