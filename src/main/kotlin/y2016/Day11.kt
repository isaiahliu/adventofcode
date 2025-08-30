package y2016

import util.expect
import util.input
import java.util.*

fun main() {
    expect(0, 0) {
        fun process(state: Long, size: Int): Int {
            fun Long.toSortedState(): Long {
                return (0 until size).map {
                    (this shr (it * 4)) and 0b1111
                }.sorted().mapIndexed { index, state -> state shl (index * 4) }.sum()
            }

            fun checkState(state: Long): Boolean {
                return (0 until 4).all { floor ->
                    val microChips = (0 until size).filter { ((state shr (it * 4)) and 0b11).toInt() == floor }
                    val generators = (0 until size).filter { ((state shr (it * 4 + 2)) and 0b11).toInt() == floor }

                    generators.isEmpty() || microChips.all { it in generators }
                }
            }

            val visited = hashSetOf<Pair<Int, Long>>()
            val tasks = LinkedList<Pair<Int, Long>>()

            fun addTask(floor: Int, state: Long) {
                if (!checkState(state)) {
                    return
                }

                val newState = state.toSortedState()

                if (visited.add(floor to newState)) {
                    tasks.add(floor to newState)
                }
            }

            addTask(0b11, state)

            var result = 0
            loop@ while (tasks.isNotEmpty()) {
                repeat(tasks.size) {
                    val (elevator, state) = tasks.poll()

                    if (state == 0L) {
                        break@loop
                    }

                    val items = (0 until size * 2).filter {
                        ((state shr it * 2) and 0b11).toInt() == elevator
                    }

                    arrayOf(elevator + 1, elevator - 1).filter { it in 0 until 4 }.forEach { targetFloor ->
                        for (index1 in items.indices) {
                            val item1 = items[index1]

                            val newState = state + ((targetFloor - elevator) shl (item1 * 2))
                            addTask(targetFloor, newState)

                            for (index2 in index1 + 1 until items.size) {
                                val item2 = items[index2]

                                addTask(targetFloor, newState + ((targetFloor - elevator) shl (item2 * 2)))
                            }
                        }
                    }
                }
                result++
            }

            return result
        }

        var init = 0L
        input.forEach {
            val floor = when {
                it.contains("first floor") -> 0b11L
                it.contains("second floor") -> 0b10L
                it.contains("third floor") -> 0b01L
                else -> 0b00L
            }

            if (it.contains("promethium-compatible microchip")) init += floor shl 0
            if (it.contains("promethium generator")) init += floor shl 2
            if (it.contains("cobalt-compatible microchip")) init += floor shl 4
            if (it.contains("cobalt generator")) init += floor shl 6
            if (it.contains("curium-compatible microchip")) init += floor shl 8
            if (it.contains("curium generator")) init += floor shl 10
            if (it.contains("ruthenium-compatible microchip")) init += floor shl 12
            if (it.contains("ruthenium generator")) init += floor shl 14
            if (it.contains("plutonium-compatible microchip")) init += floor shl 16
            if (it.contains("plutonium generator")) init += floor shl 18
        }

        part1Result = process(init, 5)
        part2Result = process((init shl 8) + 0b11111111, 7)
    }
}