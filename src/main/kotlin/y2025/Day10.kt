package y2025

import util.expect
import util.input
import java.util.*

fun main() {
    val factorCache = arrayOfNulls<Set<Int>>(100)
    fun findFactor(num: Int): Set<Int>? {
        if (num == 0) {
            return null
        }

        factorCache[num]?.also {
            return it
        }

        return buildSet {
            var f = num

            while (--f > 0) {
                if (num % f == 0) {
                    add(f)
                }
            }
            add(1)
        }.also { factorCache[num] = it }
    }

    fun findCommonFactor(route: IntArray): Int {
        return route.map {
            findFactor(it)
        }.filterNotNull().reduce { a, b -> a.intersect(b) }.max()
    }
    expect(0, 0) {
        input.forEachIndexed { index, line ->
            val nodes = line.split(" ")

            var target1 = 0L
            nodes[0].also {
                it.drop(1).dropLast(1).forEachIndexed { index, ch ->
                    if (ch == '#') {
                        target1 += 1L shl index
                    }
                }
            }

            val buttons = nodes.drop(1).dropLast(1).map {
                it.drop(1).dropLast(1).split(",").map { it.toInt() }.toSet()
            }.sortedByDescending { it.size }.toTypedArray()

            val visited1 = hashSetOf(0L)
            val tasks1 = LinkedList<Long>()
            tasks1 += 0L

            while (target1 !in visited1) {
                part1Result++

                repeat(tasks1.size) {
                    val number = tasks1.poll()
                    buttons.forEach { button ->
                        button.fold(number) { a, b ->
                            a xor (1L shl b)
                        }.also {
                            if (visited1.add(it)) {
                                tasks1.add(it)
                            }
                        }
                    }
                }
            }

            val route = nodes.last().drop(1).dropLast(1).split(",").map { it.toInt() }.toIntArray()

            val clicks = IntArray(buttons.size)
        }
    }
}
