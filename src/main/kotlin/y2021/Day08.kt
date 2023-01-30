package y2021

import util.input
import kotlin.math.pow

fun main() {
    val nums = arrayOf(
        0b1110111,
        0b0010010,
        0b1011101,
        0b1011011,
        0b0111010,
        0b1101011,
        0b1101111,
        0b1010010,
        0b1111111,
        0b1111011,
    )

    var result1 = 0
    val targets = arrayOf(1, 4, 7, 8)

    var result2 = 0
    input.map { it.split(" | ").map { it.split(" ") } }.forEach { (left, right) ->
        val result = CharArray(7)

        val inst = left.map { it.toCharArray().toMutableSet() }.groupBy { it.size }

        val num1 = inst[2]!!.first()

        result[0] = (inst[3]!!.first() - num1).first()
        inst.values.forEach { it.forEach { it.remove(result[0]) } }

        result[5] = (num1 - inst[6]!!.flatten().groupingBy { it }.eachCount().filter { it.value == 2 }.map { it.key }
            .toSet()).first()
        inst.values.forEach { it.forEach { it.remove(result[5]) } }

        result[2] = num1.first()
        inst.values.forEach { it.forEach { it.remove(result[2]) } }

        val num4 = inst[4]!!.first()
        result[3] = (num4 - inst[5]!!.flatten().groupingBy { it }.eachCount().filter { it.value == 1 }.map { it.key }
            .toSet()).first()
        inst.values.forEach { it.forEach { it.remove(result[3]) } }

        result[1] = num4.first()
        inst.values.forEach { it.forEach { it.remove(result[1]) } }

        result[6] = inst[5]!!.first { it.size == 1 }.first()
        inst.values.forEach { it.forEach { it.remove(result[6]) } }

        result[4] = inst[5]!!.first { it.size == 1 }.first()

        result.reverse()

        right.forEachIndexed { digit, it ->
            val num = result.foldIndexed(0) { index, acc, c ->
                if (c in it) {
                    acc + (1 shl index)
                } else {
                    acc
                }
            }

            val value = nums.indexOf(num)
            if (value in targets) {
                result1++
            }

            result2 += value * 10.0.pow(3 - digit).toInt()
        }
    }

    println(result1)

    println(result2)
}