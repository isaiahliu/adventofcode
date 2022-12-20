package y2017

import util.input

fun main() {
    val states = hashMapOf<String, Array<Day25Processor>>()

    var stateCode = "A"
    var steps = 0

    lateinit var currentState: Array<Day25Processor>
    lateinit var currentProcessor: Day25Processor

    input.map { it.trim().split(" ") }.forEach {
        when (it.getOrNull(0)) {
            "Begin" -> {
                stateCode = it.last().trimEnd('.')
            }

            "Perform" -> {
                steps = it.dropLast(1).last().toInt()
            }

            "In" -> {
                currentState = Array(2) {
                    Day25Processor()
                }

                states[it.last().trimEnd(':')] = currentState
            }

            "If" -> {
                currentProcessor = currentState[it.last().trimEnd(':').toInt()]
            }

            "-" -> {
                when (it[1]) {
                    "Write" -> {
                        currentProcessor.value = it.last().trimEnd('.').toInt()
                    }

                    "Move" -> {
                        currentProcessor.moveRight = it.last().trimEnd('.') == "right"
                    }

                    "Continue" -> {
                        currentProcessor.next = it.last().trimEnd('.')
                    }
                }
            }
        }
    }

    val values = hashMapOf<Int, Int>()
    var currentPos = 0

    repeat(steps) {
        val status = states[stateCode]!!

        val value = values[currentPos] ?: 0

        val processor = status[value]

        values[currentPos] = processor.value

        if (processor.moveRight) {
            currentPos++
        } else {
            currentPos--
        }

        stateCode = processor.next
    }

    println(values.values.sum())
}

private class Day25Processor {
    var value = 0

    var moveRight = false

    var next: String = ""
}