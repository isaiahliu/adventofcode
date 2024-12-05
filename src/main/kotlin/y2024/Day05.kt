package y2024

import util.expect
import util.input

fun main() {
    expect(0, 0) {
        val parents = hashMapOf<Int, MutableSet<Int>>()
        val children = hashMapOf<Int, MutableSet<Int>>()

        var processing = false
        input.forEach next@{ line ->
            when {
                line.isEmpty() -> {
                    processing = true
                }

                processing -> {
                    val nums = line.split(",").let { it.map { it.toInt() } }
                    val forbiddenNums = hashSetOf<Int>()

                    var valid = true
                    for (num in nums) {
                        if (num in forbiddenNums) {
                            valid = false
                            break
                        }

                        forbiddenNums += parents[num].orEmpty()
                    }

                    if (valid) {
                        part1Result += nums[nums.size / 2]
                    } else {
                        val degrees = nums.associateWith { 0 }.toMutableMap()
                        nums.forEach {
                            children[it]?.forEach { child ->
                                degrees[child]?.also { degree ->
                                    degrees[child] = degree + 1
                                }
                            }
                        }

                        val correct = arrayListOf<Int>()

                        while (degrees.isNotEmpty()) {
                            degrees.filter { it.value == 0 }.keys.forEach { num ->
                                children[num]?.forEach { child ->
                                    degrees[child]?.also { degree ->
                                        degrees[child] = degree - 1
                                    }
                                }

                                degrees -= num
                                correct += num
                            }
                        }

                        part2Result += correct[correct.size / 2]
                    }
                }

                else -> {
                    line.split("|").let { it.map { it.toInt() } }.also { (x, y) ->
                        parents.computeIfAbsent(y) { hashSetOf() } += x
                        children.computeIfAbsent(x) { hashSetOf() } += y
                    }
                }
            }
        }
    }
}