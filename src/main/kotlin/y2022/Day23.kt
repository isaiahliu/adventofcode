package y2022

import util.input

fun main() {
    val NORTH = 0
    val SOUTH = 1
    val WEST = 2
    val EAST = 3

    val map = hashMapOf<Pair<Int, Int>, Boolean>()
    input.forEachIndexed { rowIndex, row ->
        row.toCharArray().forEachIndexed { columnIndex, c ->
            if (c == '#') {
                map[rowIndex to columnIndex] = true
            }
        }
    }

    fun printMap() {
        for (rowIndex in map.keys.minOf { it.first }..map.keys.maxOf { it.first }) {
            buildString {
                for (columnIndex in map.keys.minOf { it.second }..map.keys.maxOf { it.second }) {
                    append(if (map[rowIndex to columnIndex] == true) "#" else ".")
                }
            }.also { println(it) }
        }

        println()
    }

    fun move(turn: Int): Boolean {
        val tasks = hashMapOf<Pair<Int, Int>, MutableList<Pair<Int, Int>>>()

        map.keys.forEach { (rowIndex, columnIndex) ->
            val nw = map[rowIndex - 1 to columnIndex - 1] ?: false
            val n = map[rowIndex - 1 to columnIndex] ?: false
            val ne = map[rowIndex - 1 to columnIndex + 1] ?: false
            val w = map[rowIndex to columnIndex - 1] ?: false
            val e = map[rowIndex to columnIndex + 1] ?: false
            val sw = map[rowIndex + 1 to columnIndex - 1] ?: false
            val s = map[rowIndex + 1 to columnIndex] ?: false
            val se = map[rowIndex + 1 to columnIndex + 1] ?: false

            if (!nw && !n && !ne && !w && !e && !sw && !s && !se) {
                return@forEach
            }

            for (i in 0 until 4) {
                when ((i + turn) % 4) {
                    NORTH -> {
                        if (!nw && !n && !ne) {
//                                map[rowIndex][columnIndex] = NORTH
                            tasks.computeIfAbsent(rowIndex - 1 to columnIndex) { arrayListOf() } += rowIndex to columnIndex
                            break
                        }
                    }

                    SOUTH -> {
                        if (!sw && !s && !se) {
//                                map[rowIndex][columnIndex] = SOUTH
                            tasks.computeIfAbsent(rowIndex + 1 to columnIndex) { arrayListOf() } += rowIndex to columnIndex
                            break
                        }
                    }

                    WEST -> {
                        if (!nw && !w && !sw) {
//                                map[rowIndex][columnIndex] = WEST
                            tasks.computeIfAbsent(rowIndex to columnIndex - 1) { arrayListOf() } += rowIndex to columnIndex
                            break
                        }
                    }

                    EAST -> {
                        if (!ne && !e && !se) {
                            //                                map[rowIndex][columnIndex] = EAST
                            tasks.computeIfAbsent(rowIndex to columnIndex + 1) { arrayListOf() } += rowIndex to columnIndex
                            break
                        }
                    }
                }
            }
        }

        val moveTask = tasks.filter { it.value.size == 1 }

        moveTask.forEach { (target, elfs) ->
            val elf = elfs.single()

            map[target.first to target.second] = true
            map.remove(elf.first to elf.second)
        }

        return moveTask.isNotEmpty()
    }

    var part1Result = 0
    var part2Result = 0

    while (move(part2Result)) {
        part2Result++
        if (part2Result == 10) {
            part1Result = (map.keys.maxOf { it.first } - map.keys.minOf { it.first } + 1) * (map.keys.maxOf { it.second } - map.keys.minOf { it.second } + 1) - map.size
        }
    }

    println(part1Result)
    println(part2Result + 1)
}