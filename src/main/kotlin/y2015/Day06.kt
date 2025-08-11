package y2015

import util.expect
import util.input

fun main() {
    val TURN_OFF = 1
    val TURN_ON = 2
    val TOGGLE = 3
    expect(0, 0) {
        val regex = "(turn on|turn off|toggle) (\\d+),(\\d+) through (\\d+),(\\d+)".toRegex()

        val diffMap = hashMapOf<Int, MutableMap<Int, MutableList<Int>>>()

        val actions = IntArray(input.size)

        input.mapNotNull { regex.matchEntire(it) }.forEachIndexed { index, matchResult ->
            actions[index] = when (matchResult.groupValues[1]) {
                "turn off" -> TURN_OFF
                "turn on" -> TURN_ON
                else -> TOGGLE
            }

            matchResult.groupValues.takeLast(4).map { it.toInt() }.also { (fromR, fromC, toR, toC) ->
                arrayOf(fromR to fromC, fromR to toC + 1, toR + 1 to fromC, toR + 1 to toC + 1).forEach { (r, c) ->
                    diffMap.computeIfAbsent(r) { hashMapOf() }.computeIfAbsent(c) { arrayListOf() } += index
                }
            }
        }

        fun merge(list1: List<Int>?, list2: List<Int>?): List<Int>? {
            return when {
                list1 == null && list2 == null -> null
                list1 == null -> list2
                list2 == null -> list1
                else -> {
                    buildList {
                        var index2 = 0
                        list1.forEach {
                            while (index2 < list2.size) {
                                if (list2[index2] < it) {
                                    add(list2[index2++])
                                } else {
                                    break
                                }
                            }

                            if (it == list2.getOrNull(index2)) {
                                index2++
                            } else {
                                add(it)
                            }
                        }

                        while (index2 < list2.size) {
                            add(list2[index2++])
                        }
                    }
                }
            }
        }

        var lastRow = arrayOfNulls<List<Int>>(1000)

        for (r in 0 until 1000) {
            var diffSum: List<Int>? = null

            lastRow = Array(1000) { c ->
                diffSum = merge(diffSum, diffMap[r]?.get(c))

                merge(diffSum, lastRow[c])?.also {
                    var r1 = 0
                    var r2 = 0
                    it.forEach {
                        when (actions[it]) {
                            TURN_OFF -> {
                                r1 = 0
                                r2 = maxOf(r2 - 1, 0)
                            }

                            TURN_ON -> {
                                r1 = 1
                                r2++
                            }

                            TOGGLE -> {
                                r1 = 1 - r1
                                r2 += 2
                            }
                        }
                    }

                    part1Result += r1
                    part2Result += r2
                }
            }
        }

        println(543903)
        println(14687245)
    }
}
