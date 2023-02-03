package y2019

import util.input

fun main() {
    val map = Array(input.size) { r ->
        CharArray(input.maxOf { it.trim().length }) { c ->
            input[r].getOrElse(c) { ' ' }
        }
    }
    val PORTAL_NAME = 'A'..'Z'

    val portals = hashMapOf<String, MutableList<Pair<Int, Int>>>()

    for (rowIndex in 1 until map.size - 1) {
        for (columnIndex in 1 until map[rowIndex].size - 1) {
            if (map[rowIndex][columnIndex] in PORTAL_NAME) {
                var rowOffset = 0
                var columnOffset = 0

                var portalName: String
                when {
                    map[rowIndex - 1][columnIndex] == '.' -> {
                        rowOffset -= 1

                        portalName = "${map[rowIndex][columnIndex]}${map[rowIndex + 1][columnIndex]}"
                    }

                    map[rowIndex + 1][columnIndex] == '.' -> {
                        rowOffset += 1

                        portalName = "${map[rowIndex - 1][columnIndex]}${map[rowIndex][columnIndex]}"
                    }

                    map[rowIndex][columnIndex - 1] == '.' -> {
                        columnOffset -= 1

                        portalName = "${map[rowIndex][columnIndex]}${map[rowIndex][columnIndex + 1]}"
                    }

                    map[rowIndex][columnIndex + 1] == '.' -> {
                        columnOffset += 1

                        portalName = "${map[rowIndex][columnIndex - 1]}${map[rowIndex][columnIndex]}"
                    }

                    else -> {
                        continue
                    }
                }

                portals.computeIfAbsent(portalName) { arrayListOf() } += (rowIndex + rowOffset) to (columnIndex + columnOffset)
            }
        }
    }

    val portalPositions = hashMapOf<Pair<Int, Int>, Pair<Int, Int>>()

    portals.values.filter { it.size == 2 }.forEach { (pos1, pos2) ->
        portalPositions[pos1] = pos2
        portalPositions[pos2] = pos1
    }

    val (startRowIndex, startColumnIndex) = portals["AA"]?.firstOrNull() ?: return
    val (endRowIndex, endColumnIndex) = portals["ZZ"]?.firstOrNull() ?: return

    data class Task(val pos: Pair<Int, Int>, val level: Int, val jump: Boolean)

    fun process(multiLevel: Boolean): Int {
        var done = false
        val walked = hashMapOf(0 to hashSetOf(startRowIndex to startColumnIndex))
        var distance = 0

        val tasks = arrayListOf(Task(startRowIndex to startColumnIndex, 0, false))

        fun addTask(level: Int, pos: Pair<Int, Int>, jump: Boolean) {
            if (walked.computeIfAbsent(level) { hashSetOf() }.add(pos)) {
                tasks += Task(pos, level, jump)
            }
        }

        loop@ while (tasks.isNotEmpty()) {
            val current = tasks.toList()
            tasks.clear()

            for ((pos, level, jump) in current) {
                val (rowIndex, columnIndex) = pos

                if (rowIndex == endRowIndex && columnIndex == endColumnIndex && level == 0) {
                    done = true
                    break@loop
                }

                val portal = portalPositions[pos]

                if (portal != null && !jump) {
                    val newLevel = if (multiLevel) {
                        if (portal.first == 2 || portal.second == 2 || portal.first == map.size - 3 || portal.second == map[0].size - 3) 1 else -1
                    } else {
                        0
                    }

                    if (level + newLevel >= 0) {
                        addTask(level + newLevel, portal, true)
                    }
                } else {
                    arrayOf(
                        rowIndex - 1 to columnIndex,
                        rowIndex + 1 to columnIndex,
                        rowIndex to columnIndex - 1,
                        rowIndex to columnIndex + 1
                    ).filter { (r, c) ->
                        map[r][c] == '.'
                    }.forEach {
                        addTask(level, it, false)
                    }
                }
            }

            distance++
        }
        return if (done) distance else -1
    }
    println(process(false))
    println(process(true))
}

