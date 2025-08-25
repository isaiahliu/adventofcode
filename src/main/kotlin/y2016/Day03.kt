package y2016

import util.expect
import util.input
import java.util.*

fun main() {
    expect(0, 0) {
        val regex = "\\d+".toRegex()

        val columns = arrayOf(arrayListOf<Int>(), arrayListOf(), arrayListOf())
        input.forEach {
            regex.findAll(it).map { it.groupValues[0].toInt() }.toList().also { nums ->
                nums.forEachIndexed { index, num -> columns[index] += num }

                nums.sorted().takeIf { (a, b, c) -> a + b > c }?.also {
                    part1Result++
                }
            }
        }

        val temp = PriorityQueue<Int>()

        columns.forEach {
            it.forEach {
                temp.add(it)

                if (temp.size == 3) {
                    if (temp.poll() + temp.poll() > temp.poll()) {
                        part2Result++
                    }
                }
            }
        }
    }
}