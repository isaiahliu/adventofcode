package y2016

import input

fun main() {
    val values = arrayListOf<Pair<Int, Int>>()

    val robots = hashMapOf<Int, Robot>()
    val outputs = hashMapOf<Int, Output>()

    input.map { it.split(" ") }.forEach {
        when (it[0]) {
            "value" -> {
                values += it[5].toInt() to it[1].toInt()
            }

            "bot" -> {
                val robot = robots.computeIfAbsent(it[1].toInt()) {
                    Robot()
                }

                robot.lower = when (it[5]) {
                    "bot" -> {
                        robots.computeIfAbsent(it[6].toInt()) { Robot() }
                    }

                    else -> {
                        outputs.computeIfAbsent(it[6].toInt()) { Output() }
                    }
                }

                robot.higher = when (it[10]) {
                    "bot" -> {
                        robots.computeIfAbsent(it[11].toInt()) { Robot() }
                    }

                    else -> {
                        outputs.computeIfAbsent(it[11].toInt()) { Output() }
                    }
                }
            }

            else -> {
                throw Exception("else")
            }
        }
    }

    values.forEach {
        robots[it.first]?.receive(it.second)
    }

    val result1 = robots.entries.first {
        it.value.history.any {
            it.first == 17 && it.second == 61
        }
    }.key

    println(result1)

    val output012 = outputs[0]?.history.orEmpty().union(outputs[1]?.history.orEmpty()).union(outputs[2]?.history.orEmpty())

    println(output012.fold(1) { a, b -> a * b })
}

private interface IReceivable {
    fun receive(value: Int)
}

private class Robot : IReceivable {
    val values = arrayListOf<Int>()

    lateinit var lower: IReceivable

    lateinit var higher: IReceivable

    val history = arrayListOf<Pair<Int, Int>>()

    override fun receive(value: Int) {
        values += value

        if (values.size == 2) {
            val (low, high) = values.sorted()

            values.clear()

            lower.receive(low)
            higher.receive(high)

            history += low to high
        }
    }
}

private class Output : IReceivable {
    val history = arrayListOf<Int>()

    override fun receive(value: Int) {
        history += value
    }
}