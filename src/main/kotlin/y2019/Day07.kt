package y2019

import util.input
import java.util.*
import kotlin.math.pow

fun main() {

    class Amp(var inputQueue: Queue<Int>) {
        val nums = input.first().let {
            it.split(",").map { it.toInt() }.toIntArray()
        }

        val outputQueue: Queue<Int> = LinkedList()

        var instructionIndex = 0

        val done get() = instructionIndex >= nums.size

        fun readParam(paramIndex: Int): Int {
            val paramMode = (nums[instructionIndex] / 10 / (10.0.pow(paramIndex)).toInt()) % 10 == 0

            return if (paramMode) {
                nums[nums[instructionIndex + paramIndex]]
            } else {
                nums[instructionIndex + paramIndex]
            }
        }

        fun process() {
            while (!done) {
                when (nums[instructionIndex] % 100) {
                    1 -> {
                        nums[nums[instructionIndex + 3]] = readParam(1) + readParam(2)
                        instructionIndex += 4
                    }

                    2 -> {
                        nums[nums[instructionIndex + 3]] = readParam(1) * readParam(2)
                        instructionIndex += 4
                    }

                    3 -> {
                        if (inputQueue.isEmpty()) {
                            return
                        }

                        nums[nums[instructionIndex + 1]] = inputQueue.poll()
                        instructionIndex += 2
                    }

                    4 -> {
                        outputQueue.add(nums[nums[instructionIndex + 1]])
                        instructionIndex += 2
                    }

                    5 -> {
                        if (readParam(1) != 0) {
                            instructionIndex = readParam(2)
                        } else {
                            instructionIndex += 3
                        }
                    }

                    6 -> {
                        if (readParam(1) == 0) {
                            instructionIndex = readParam(2)
                        } else {
                            instructionIndex += 3
                        }
                    }

                    7 -> {
                        nums[nums[instructionIndex + 3]] = if (readParam(1) < readParam(2)) {
                            1
                        } else {
                            0
                        }
                        instructionIndex += 4
                    }

                    8 -> {
                        nums[nums[instructionIndex + 3]] = if (readParam(1) == readParam(2)) {
                            1
                        } else {
                            0
                        }
                        instructionIndex += 4
                    }

                    99 -> {
                        instructionIndex = nums.size
                    }

                    else -> {
                        println("Error")
                    }
                }
            }
        }
    }

    fun generatePossibilities(nums: IntArray): ArrayList<IntArray> {
        val possibilities = arrayListOf<IntArray>()

        fun walk(list: List<Int>) {
            if (list.size == nums.size) {
                possibilities += list.toIntArray()
                return
            }

            nums.filter { it !in list }.forEach {
                walk(list + it)
            }
        }

        walk(emptyList())

        return possibilities
    }

    fun process(loopMode: Boolean, baseNum: Int) {
        val possibilities = generatePossibilities(IntArray(5) { it + baseNum })

        println(possibilities.maxOf { p ->
            var output: Queue<Int> = LinkedList<Int>().also {
                it += p[0]
                it += 0
            }
            val amps = (0..4).map { index ->
                Amp(output).also {
                    it.outputQueue += p[(index + 1) % 5]
                    output = it.outputQueue
                }
            }

            if (loopMode) {
                amps.last().outputQueue += 0
                amps.first().inputQueue = amps.last().outputQueue
            }

            while (!amps.last().done) {
                amps.forEach { it.process() }
            }

            amps.last().outputQueue.last()
        })
    }

    process(false, 0)
    process(true, 5)
}