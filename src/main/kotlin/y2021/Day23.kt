package y2021

import util.input
import kotlin.math.absoluteValue

fun main() {
    val energies = intArrayOf(1, 10, 100, 1000)
    fun Char.energy(): Int {
        return energies[this - 'A']
    }

    fun Char.targetRoom(): Int {
        return (this - 'A') * 2 + 2
    }

    fun Int.targetChar(): Char {
        return 'A' + (this / 2 - 1)
    }

    val roomIndices = arrayOf(2, 4, 6, 8)
    val hallwayIndices = arrayOf(1, 3, 5, 7, 9)
    val specialIndices = arrayOf(0, 10)

    fun Array<String>.done(roomSize: Int): Boolean {
        return roomIndices.all {
            val targetChar = it.targetChar()

            this[it].count { it == targetChar } == roomSize
        }
    }

    fun Array<String>.canMove(fromIndex: Int, toIndex: Int): Boolean {
        if (fromIndex == toIndex) {
            return false
        }

        val top = this[fromIndex].firstOrNull() ?: return false

        return when {
            toIndex in specialIndices || toIndex in hallwayIndices && fromIndex in specialIndices -> {
                this[toIndex].isEmpty() && (fromIndex - toIndex).absoluteValue == 1
            }

            toIndex in roomIndices -> {
                val targetChar = toIndex.targetChar()

                (fromIndex.coerceAtMost(toIndex) + 1 until fromIndex.coerceAtLeast(toIndex)).all {
                    it in roomIndices || this[it].isEmpty()
                } && top == targetChar && this[toIndex].all { it == targetChar }
            }

            else -> {
                this[toIndex].isEmpty() && fromIndex in roomIndices && (fromIndex.coerceAtMost(toIndex) + 1 until fromIndex.coerceAtLeast(
                    toIndex
                )).all {
                    it in roomIndices || this[it].isEmpty()
                }
            }
        }
    }

    fun Array<String>.moveStack(fromIndex: Int, toIndex: Int, roomSize: Int): Int {
        var distance = (fromIndex - toIndex).absoluteValue

        if (fromIndex in roomIndices) {
            distance += roomSize - this[fromIndex].length + 1
        }

        if (toIndex in roomIndices) {
            distance += roomSize - this[toIndex].length
        }

        val from = this[fromIndex].first()

        this[fromIndex] = this[fromIndex].substring(1)
        this[toIndex] = from + this[toIndex]

        return distance * from.energy()
    }

    fun Array<String>.serialize(): String {
        return this.joinToString(",")
    }

    fun process(v2: Boolean): Int {
        val map: Array<String> = Array(11) { "" }

        val roomSize = if (v2) 4 else 2
        input.drop(2).take(2).forEachIndexed { row, line ->
            if (v2 && row > 0) {
                map[2] += "D"
                map[4] += "C"
                map[6] += "B"
                map[8] += "A"
                map[2] += "D"
                map[4] += "B"
                map[6] += "A"
                map[8] += "C"
            }
            map[2] += line[3].toString()
            map[4] += line[5].toString()
            map[6] += line[7].toString()
            map[8] += line[9].toString()
        }

        var sum = Int.MAX_VALUE

        val visited = hashMapOf(map.serialize() to 0)
        val tasks = hashMapOf(map.serialize() to 0)

        while (tasks.isNotEmpty()) {
            val current = tasks.toMap()
            tasks.clear()

            for ((task, energy) in current) {
                val temp = task.split(",").toTypedArray()

                if (temp.done(roomSize)) {
                    sum = sum.coerceAtMost(energy)
                    continue
                }

                val availables = arrayListOf<Pair<Int, Int>>()

                roomIndices.forEach { from ->
                    hallwayIndices.forEach { to ->
                        if (temp.canMove(from, to)) {
                            availables += from to to
                        }
                    }

                    roomIndices.forEach { to ->
                        if (temp.canMove(from, to)) {
                            availables += from to to
                        }
                    }
                }

                hallwayIndices.forEach { from ->
                    roomIndices.forEach { to ->
                        if (temp.canMove(from, to)) {
                            availables += from to to
                        }
                    }

                    specialIndices.forEach { to ->
                        if (temp.canMove(from, to)) {
                            availables += from to to
                        }
                    }
                }

                specialIndices.forEach { from ->
                    roomIndices.forEach { to ->
                        if (temp.canMove(from, to)) {
                            availables += from to to
                        }
                    }
                }

                availables.forEach { (from, to) ->
                    val newTask = task.split(",").toTypedArray()
                    val newEnergy = energy + newTask.moveStack(from, to, roomSize)

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