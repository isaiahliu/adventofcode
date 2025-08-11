package y2015

import util.expect
import util.input
import java.util.*

fun main() {
    expect(0, 0) {
        fun LinkedList<IntArray>.append(num: Int) {
            if (num > 9) {
                num.toString().forEach {
                    append(it - '0')
                }
            } else {
                if (peekLast()?.get(1) != num) {
                    add(intArrayOf(0, num))
                }
                
                peekLast()[0]++
            }
        }

        fun LinkedList<IntArray>.process(times: Int): LinkedList<IntArray> {
            var result = this
            repeat(times) {
                val newLine = LinkedList<IntArray>()

                result.forEach { (count, num) ->
                    newLine.append(count)
                    newLine.append(num)
                }

                result = newLine
            }
            return result
        }

        var line = LinkedList<IntArray>()
        input.first().map {
            line.append(it - '0')
        }

        line = line.process(40)
        part1Result = line.sumOf { it[0] }

        line = line.process(10)
        part2Result = line.sumOf { it[0] }
    }
}

