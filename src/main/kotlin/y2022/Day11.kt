package y2022

import util.input

fun main() {
    fun process(maxRound: Int, initProcessor: (Long, Long) -> Long): Long {
        val monkeyMap = hashMapOf<Int, Monkey>()

        val lines = input.map { it.trim().split(" ") }
        var lineIndex = 0
        while (lineIndex < lines.size) {
            val line = lines[lineIndex]

            when (line[0]) {
                "Monkey" -> {
                    val monkey = Monkey(line[1].trimEnd(':').toInt())

                    lineIndex++
                    val startItemsLine = lines[lineIndex++]

                    monkey.items += startItemsLine.drop(2).map { it.trimEnd(',').toLong() }

                    val operationLine = lines[lineIndex++]

                    when (operationLine.takeLast(2)[0]) {
                        "+" -> {
                            monkey.operate = { it + (operationLine.last().toLongOrNull() ?: it) }
                        }

                        "*" -> {
                            monkey.operate = { it * (operationLine.last().toLongOrNull() ?: it) }
                        }
                    }

                    val testLine = lines[lineIndex++]
                    val trueLine = lines[lineIndex++]
                    val falseLine = lines[lineIndex++]

                    monkey.testDivisible = testLine.last().toLong()
                    monkey.monkeyTrue = trueLine.last().toInt()
                    monkey.monkeyFalse = falseLine.last().toInt()

                    monkeyMap[monkey.index] = monkey
                }

                else -> lineIndex++
            }
        }

        val monkeys = monkeyMap.values.sortedBy { it.index }

        val totalDivisible = monkeys.map { it.testDivisible }.reduce { a, b -> a * b }

        var round = 0
        while (round < maxRound) {
            monkeys.forEach { monkey ->
                monkey.items.forEach {
                    val newItem = initProcessor(monkey.operate(it), totalDivisible)

                    if (newItem % monkey.testDivisible == 0L) {
                        monkeyMap[monkey.monkeyTrue]
                    } else {
                        monkeyMap[monkey.monkeyFalse]
                    }?.items?.add(newItem)
                }

                monkey.inspectedItems += monkey.items.size
                monkey.items.clear()
            }

            round++
        }

        return monkeys.map { it.inspectedItems }.sortedDescending().take(2).reduce { a, b -> a * b }
    }
    println(process(20) { num, _ -> num / 3 })
    println(process(10000) { num, division -> num % division })
}

private class Monkey(val index: Int) {
    val items = arrayListOf<Long>()

    var inspectedItems = 0L

    var operate: (Long) -> Long = { it }

    var testDivisible = 1L

    var monkeyTrue = 0

    var monkeyFalse = 0
}