package y2015

import util.expect
import util.input

fun main() {
    expect(0, 0) {
        val target = input.first().toInt()

        fun calculate(times: Int, maxHouse: Int): Int {
            val dp = hashMapOf<Int, MutableMap<Int, Int>>()

            var num = 2
            while (true) {
                var sum = (num + 1) * times
                dp.remove(num)?.forEach { (key, house) ->
                    sum += key * times
                    if (house > 1) {
                        dp.computeIfAbsent(num + key) { hashMapOf() }[key] = house - 1
                    }
                } ?: run {
                    if (num > 20) {
                        num++
                        continue
                    }
                }

                dp.computeIfAbsent(num + num) { hashMapOf() }[num] = maxHouse
                if (sum >= target) {
                    return num
                }

                num++
            }
        }

        part1Result = calculate(10, Int.MAX_VALUE)
        part2Result = calculate(11, 50)
    }
}