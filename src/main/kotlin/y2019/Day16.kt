package y2019

import util.input
import kotlin.math.absoluteValue

fun main() {
    val nums = input.map { it.map { it - '0' }.toIntArray() }.first()

    fun process(init: IntArray, phase: Int, times: Int, offset: Int, outputLength: Int): String {
        val length = init.size * times
        val cache = hashMapOf<Int, MutableMap<Int, Byte>>()
        repeat(phase) {
            cache[it + 1] = hashMapOf()
        }

        fun getNum(level: Int, index: Int): Byte {
            if (index >= length) {
                throw RuntimeException("Error")
            }

            val levelCache = cache[level]!!

            return levelCache[index] ?: run {
                var sum = 0

                val count = index + 1

                var drop = count - 1

                var scanIndex = 0

                var mul = 1

                while (scanIndex < length) {
                    scanIndex += drop
                    drop = count

                    repeat(count.coerceAtMost(length - scanIndex)) {
                        sum += mul * if (level == 1) {
                            init[(scanIndex + it) % init.size]
                        } else {
                            getNum(level - 1, scanIndex + it).toInt()
                        }
                    }

                    mul *= -1

                    scanIndex += count
                }

                val value = (sum.absoluteValue % 10).toByte()
                levelCache[index] = value

                value
            }
        }

        return buildString {
            repeat(outputLength) {
                val num = getNum(phase, it + offset)

                append(num.toString())
            }
        }
    }

    val result1 = process(nums, 100, 1, 0, 8)
    println(result1)

    val offset = nums.take(7).reduce { acc, n -> acc * 10 + n }

    val result2 = process(nums, 100, 10000, offset, 8)
    println(result2)
}