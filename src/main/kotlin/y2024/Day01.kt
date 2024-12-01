package y2024

import util.expect
import util.input
import java.util.*
import kotlin.math.absoluteValue

fun main() {
    expect(0, 0) {
        val leftQueue = PriorityQueue<Int>()
        val rightQueue = PriorityQueue<Int>()

        val map = hashMapOf<Int, IntArray>()
        input.map { it.split(" ").mapNotNull { it.toIntOrNull() } }.forEach { (left, right) ->
            leftQueue.add(left)
            rightQueue.add(right)

            map.computeIfAbsent(left) { intArrayOf(0, 0) }.also {
                it[0]++
                part2Result += left * it[1]
            }
            map.computeIfAbsent(right) { intArrayOf(0, 0) }.also {
                it[1]++
                part2Result += right * it[0]
            }
        }

        while (leftQueue.isNotEmpty()) {
            part1Result += (leftQueue.poll() - rightQueue.poll()).absoluteValue
        }
    }
}