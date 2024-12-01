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

            map.computeIfAbsent(left) { intArrayOf(0, 0) }[0]++
            map.computeIfAbsent(right) { intArrayOf(0, 0) }[1]++
        }

        while (leftQueue.isNotEmpty()) {
            part1Result += (leftQueue.poll() - rightQueue.poll()).absoluteValue
        }

        part2Result = map.entries.sumOf { (key, value) -> key * value[0] * value[1] }
    }
}