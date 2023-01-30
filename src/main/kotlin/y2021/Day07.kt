package y2021

import util.input
import kotlin.math.absoluteValue
import kotlin.math.roundToInt

fun main() {
    val nums = input.first().split(",").map { it.toInt() }.sorted()

    val m1 = nums[nums.size / 2]

    println(nums.sumOf { (it - m1).absoluteValue })

    val m2d = 1.0 * nums.sum() / nums.size

    println(setOf(m2d.toInt(), m2d.roundToInt()).minOf { m2 ->
        nums.sumOf { (it - m2).absoluteValue.let { (1 + it) * it / 2 } }
    })
}