package y2021

import util.input
import kotlin.math.absoluteValue

fun main() {
    val EMPTY = '.'

    val targetChar = arrayOf(EMPTY, 'A', EMPTY, 'B', EMPTY, 'C', EMPTY, 'D', EMPTY)

    fun Array<String>.printMap() {
        val charStacks = this

        println("#############")
        buildString {
            append("#")

            append(charStacks.mapIndexed { index, s ->
                when {
                    index == 0 -> s.reversed()
                    targetChar[index] == EMPTY -> {
                        s
                    }

                    else -> {
                        s[0]
                    }
                }
            }.joinToString(""))

            append("#")
        }.also { println(it) }

        repeat(charStacks.maxOf { it.length - 1 }) { row ->
            buildString {
                append(charStacks.mapIndexed { index, s ->
                    when {
                        index == 0 -> {
                            when (row) {
                                0 -> "###"
                                else -> "  #"
                            }
                        }

                        index == 8 -> {
                            when (row) {
                                0 -> "###"
                                else -> "#  "
                            }
                        }

                        targetChar[index] == EMPTY -> {
                            "#"
                        }

                        else -> {
                            s[row + 1]
                        }
                    }
                }.joinToString(""))
            }.also { println(it) }
        }

        println("  #########   ")
        println()
    }

    val energies = intArrayOf(1, 10, 100, 1000)
    fun Char.energy(): Int {
        return energies[this - 'A']
    }

    fun Array<String>.minEnergy(): Int {
        val indexes = arrayOf(1, 3, 5, 7)

        var result = 0

        indexes.forEach { i ->
            val target = targetChar[i]
            var t = this[i].mapIndexedNotNull { index, c ->
                index.takeIf { c != target && c != EMPTY }
            }.sum()

            indexes.forEach { j ->
                if (i != j) {
                    val line = this[j]
                    t += line.mapIndexedNotNull { index, c ->
                        (index + (i - j).absoluteValue).takeIf { c == target }
                    }.sum()
                }
            }

            result += t * target.energy()
        }

        return result
    }

    fun Array<String>.halfDone(index: Int): Boolean {
        return targetChar[index] != EMPTY && this[index].all { it == targetChar[index] || it == EMPTY }
    }

    fun Array<String>.done(index: Int? = null): Boolean {
        return if (index != null) {
            indices.all { done(it) }
        } else {
            this.mapIndexed { i, s ->
                if (targetChar[i] != EMPTY) {
                    s.drop(1)
                } else {
                    s
                }.all { it == targetChar[i] }
            }.all { it }
        }
    }

    fun Array<String>.canMove(fromIndex: Int, toIndex: Int): Boolean {
        if (this[fromIndex].all { it == EMPTY }) {
            return false
        }

        if (this[toIndex].none { it == EMPTY }) {
            return false
        }

        if (targetChar[toIndex] != EMPTY && this[toIndex][0] != EMPTY) {
            return false
        }

        for (index in fromIndex.coerceAtMost(toIndex) + 1 until fromIndex.coerceAtLeast(toIndex)) {
            if (targetChar[index] == EMPTY && this[index].any { it != EMPTY }) {
                return false
            }
        }

        return true
    }

    fun Array<String>.moveStack(
        fromIndex: Int, toIndex: Int, costOnly: Boolean = false
    ): Int {
        val from = this[fromIndex]
        val to = this[toIndex]

        val char = from.first { it != EMPTY }

        val toTheBottom = char == targetChar[toIndex] && halfDone(toIndex)

        var additionalCost = 0
        var distance = (fromIndex - toIndex).absoluteValue

        distance += from.indexOfFirst { it == char }

        if (toTheBottom) {
            distance += to.length - to.trimStart(EMPTY).length - 1
        } else {
            val firstEmpty = to.indexOfFirst { it == EMPTY }

            additionalCost += to.substring(0, firstEmpty).sumOf { it.energy() }
        }

        if (!costOnly) {
            this[fromIndex] = from.trimStart(EMPTY).substring(1).padStart(from.length, EMPTY)

            this[toIndex] = if (toTheBottom) {
                (char + to.trimStart(EMPTY)).padStart(to.length, EMPTY)
            } else {
                val firstEmpty = to.indexOfFirst { it == EMPTY }

                char + to.substring(0, firstEmpty) + to.substring(firstEmpty + 1)
            }
        }

        return char.energy() * distance + additionalCost
    }

    fun Array<String>.top(index: Int): Char? {
        return this[index].firstOrNull { it != EMPTY }
    }

    fun Array<String>.uselessChars(index: Int): Int {
        return this[index].trimEnd(targetChar[index]).count { it != EMPTY }
    }

    fun Array<String>.serialize(): String {
        return this.joinToString(",")
    }

    fun process(v2: Boolean): Int {
        val map: Array<String> = arrayOf(
            EMPTY.toString().repeat(2),
            EMPTY.toString(),
            EMPTY.toString(),
            EMPTY.toString(),
            EMPTY.toString(),
            EMPTY.toString(),
            EMPTY.toString(),
            EMPTY.toString(),
            EMPTY.toString().repeat(2),
        )

        input.drop(2).take(2).forEachIndexed { row, line ->
            if (v2 && row > 0) {
                map[1] += "D"
                map[3] += "C"
                map[5] += "B"
                map[7] += "A"
                map[1] += "D"
                map[3] += "B"
                map[5] += "A"
                map[7] += "C"
            }
            map[1] += line[3].toString()
            map[3] += line[5].toString()
            map[5] += line[7].toString()
            map[7] += line[9].toString()
        }

        var sum = Int.MAX_VALUE

        val visited = hashMapOf(map.serialize() to 0)
        val tasks = hashMapOf(map.serialize() to 0)

        val hallwayIndicies = map.indices.filter { it % 2 == 0 }
        val roomIndicies = map.indices.filter { it % 2 == 1 }

        while (tasks.isNotEmpty()) {
            val current = tasks.toMap()
            tasks.clear()

            for ((task, energy) in current) {
                val temp = task.split(",").toTypedArray()

                if (temp.done()) {
                    sum = sum.coerceAtMost(energy)
                    continue
                }

                val availables = arrayListOf<Pair<Int, Int>>()
                hallwayIndicies.forEach { i ->
                    val top = temp[i].firstOrNull { it != EMPTY } ?: return@forEach

                    val targetRoom = (top - 'A') * 2 + 1
                    if (temp.halfDone(targetRoom) && temp.canMove(i, targetRoom)) {
                        availables += i to targetRoom
                    }
                }

                roomIndicies.forEach { i ->
                    hallwayIndicies.filter { temp.canMove(i, it) }.forEach {
                        availables += i to it
                    }
                }

                availables.forEach { (from, to) ->
                    val newTask = task.split(",").toTypedArray()
                    val newEnergy = energy + newTask.moveStack(from, to)

                    val newTaskStr = newTask.serialize()
                    if ((visited[newTaskStr] ?: Int.MAX_VALUE) > newEnergy) {
                        visited[newTaskStr] = newEnergy
                        tasks[newTaskStr] = newEnergy
                    }
                }
            }
        }

        return sum
    }
    println(process(false))
    println(process(true))
}