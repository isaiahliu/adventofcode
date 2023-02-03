package y2016

import util.input

fun main() {
    val initSpace = IntArray(10)

    input.forEach {
        val floor = when {
            it.contains("first floor") -> 0
            it.contains("second floor") -> 1
            it.contains("third floor") -> 2
            else -> 3
        }

        if (it.contains("promethium-compatible microchip")) initSpace[0] = floor
        if (it.contains("cobalt-compatible microchip")) initSpace[1] = floor
        if (it.contains("curium-compatible microchip")) initSpace[2] = floor
        if (it.contains("ruthenium-compatible microchip")) initSpace[3] = floor
        if (it.contains("plutonium-compatible microchip")) initSpace[4] = floor
        if (it.contains("promethium generator")) initSpace[0 + 5] = floor
        if (it.contains("cobalt generator")) initSpace[1 + 5] = floor
        if (it.contains("curium generator")) initSpace[2 + 5] = floor
        if (it.contains("ruthenium generator")) initSpace[3 + 5] = floor
        if (it.contains("plutonium generator")) initSpace[4 + 5] = floor
    }

    fun process(inputSpace: IntArray): Int {
        val walked = hashMapOf<Int, MutableSet<String>>(
            0 to hashSetOf(),
            1 to hashSetOf(),
            2 to hashSetOf(),
            3 to hashSetOf()
        )

        fun IntArray.walked(elevator: Int): Boolean {
            return !walked[elevator]!!.add(joinToString(""))
        }

        fun IntArray.explode(): Boolean {
            for (index in 0 until size / 2) {
                if (this[index] != this[index + size / 2]) {
                    if (this.takeLast(size / 2).any {
                            it == this[index]
                        }) {
                        return true
                    }
                }
            }
            return false
        }

        var step = 0

        val currentStats = arrayListOf(0 to inputSpace)

        while (true) {
            val stat = currentStats.toList()
            currentStats.clear()

            for ((elevator, space) in stat) {
                if (space.walked(elevator)) {
                    continue
                }

                if (space.all { it == 3 }) {
                    return step
                }

                if (space.explode()) {
                    continue
                }

                val current = space.mapIndexed { index, floor ->
                    if (elevator == floor) {
                        index
                    } else {
                        null
                    }
                }.filterNotNull().sortedDescending()

                current.forEach { item1 ->
                    //Move up
                    if (elevator < 3) {
                        currentStats += (elevator + 1) to IntArray(space.size) {
                            space[it]
                        }.also {
                            it[item1]++
                        }

                        current.forEach { item2 ->
                            if (item1 < item2) {
                                currentStats += (elevator + 1) to IntArray(space.size) {
                                    space[it]
                                }.also {
                                    it[item1]++
                                    it[item2]++
                                }
                            }
                        }
                    }

                    if (elevator > 0) {
                        currentStats += (elevator - 1) to IntArray(space.size) {
                            space[it]
                        }.also {
                            it[item1]--
                        }

                        current.forEach { item2 ->
                            if (item1 < item2) {
                                currentStats += (elevator - 1) to IntArray(space.size) {
                                    space[it]
                                }.also {
                                    it[item1]--
                                    it[item2]--
                                }
                            }
                        }
                    }
                }
            }
            step++
        }
    }

    println(process(initSpace))

    val step2Input = buildList {
        add(0)
        add(0)
        addAll(initSpace.take(initSpace.size / 2))
        add(0)
        add(0)
        addAll(initSpace.takeLast(initSpace.size / 2))
    }.toIntArray()

    println(process(step2Input))
}

