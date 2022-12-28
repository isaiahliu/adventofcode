package y2018

import util.input

fun main() {
    abstract class AbstractCharacter(goblin: Boolean, elf: Boolean) {
        private val camp = (if (goblin) 1 shl 0 else 0) + (if (elf) 1 shl 1 else 0)

        open val str = 3

        var hp = 200

        fun isEnemy(target: AbstractCharacter?): Boolean {
            return target != null && camp.and(target.camp) == 0
        }

        fun attack(target: AbstractCharacter) {
            target.hp -= target.hp.coerceAtMost(str)
        }

        val alive: Boolean get() = hp > 0
    }

    class Elf(override val str: Int) : AbstractCharacter(false, true)

    class Goblin : AbstractCharacter(true, false)

    val wall = object : AbstractCharacter(true, true) {
    }

    fun process(elfAttack: Int, allowElfDeath: Boolean): Int {

        val goblins = arrayListOf<Goblin>()
        val elves = arrayListOf<Elf>()

        val map = input.map {
            it.map {
                when (it) {
                    '#' -> wall
                    'G' -> {
                        Goblin().also { goblins += it }
                    }

                    'E' -> {
                        Elf(elfAttack).also { elves += it }
                    }

                    else -> null
                }
            }.toTypedArray()
        }.toTypedArray()

        fun printMap() {
            map.forEach {
                var hps = StringBuilder()
                val mapLine = it.joinToString("") {
                    when (it) {
                        wall -> "#"
                        is Goblin -> {
                            hps.append("G(${it.hp}) ")
                            "G"
                        }

                        is Elf -> {
                            hps.append("E(${it.hp}) ")
                            "E"
                        }

                        else -> "."
                    }
                }
                println("${mapLine}   ${hps}")
            }
        }

        fun drawMovementMap(r: Int, c: Int): Array<IntArray> {
            val movementMap = Array(map.size) {
                IntArray(map[it].size) { Int.MAX_VALUE }
            }

            fun walk(num: Int, rowIndex: Int, columnIndex: Int) {
                if (movementMap[rowIndex][columnIndex] <= num) {
                    return
                }

                movementMap[rowIndex][columnIndex] = num

                if (map[rowIndex - 1][columnIndex] == null) {
                    walk(num + 1, rowIndex - 1, columnIndex)
                }
                if (map[rowIndex][columnIndex - 1] == null) {
                    walk(num + 1, rowIndex, columnIndex - 1)
                }
                if (map[rowIndex][columnIndex + 1] == null) {
                    walk(num + 1, rowIndex, columnIndex + 1)
                }
                if (map[rowIndex + 1][columnIndex] == null) {
                    walk(num + 1, rowIndex + 1, columnIndex)
                }
            }

            val character = map[r][c] ?: return movementMap

            map.forEachIndexed { rowIndex, row ->
                row.forEachIndexed { columnIndex, c ->
                    if (character.isEnemy(c)) {
                        walk(0, rowIndex, columnIndex)
                    }
                }
            }

            return movementMap
        }

        fun attack(rowIndex: Int, columnIndex: Int): Boolean {
            val character = map[rowIndex][columnIndex] ?: return false

            val nearby = arrayOf(rowIndex - 1 to columnIndex, rowIndex to columnIndex - 1, rowIndex to columnIndex + 1, rowIndex + 1 to columnIndex)

            val enemies = nearby.filter { (r, c) ->
                character.isEnemy(map[r][c])
            }

            if (enemies.isEmpty()) {
                return false
            }

            val minHp = enemies.minOf { (r, c) -> map[r][c]?.hp ?: 0 }
            val enemyPos = enemies.first { (r, c) -> map[r][c]?.hp == minHp }

            map[enemyPos.first][enemyPos.second]?.also {
                character.attack(it)

                if (!it.alive) {
                    map[enemyPos.first][enemyPos.second] = null
                }
            }

            return true
        }

        var fullround = false
        var round = 0
        while (true) {
            round++

            val characters = map.mapIndexed { rowIndex, row ->
                row.mapIndexedNotNull { columnIndex, c ->
                    if (c != null && c != wall) {
                        rowIndex to columnIndex
                    } else {
                        null
                    }
                }
            }.flatten()

            var gameover = false
            characters.forEach { (rowIndex, columnIndex) ->
                if (map[rowIndex][columnIndex] == null) {
                    return@forEach
                }

                if (gameover) {
                    fullround = false
                } else {
                    val nearby = arrayOf(rowIndex - 1 to columnIndex, rowIndex to columnIndex - 1, rowIndex to columnIndex + 1, rowIndex + 1 to columnIndex)

                    if (!attack(rowIndex, columnIndex)) {
                        val movementMap = drawMovementMap(rowIndex, columnIndex)

                        nearby.filter { (r, c) ->
                            movementMap[r][c] < Int.MAX_VALUE
                        }.minByOrNull { (r, c) ->
                            movementMap[r][c]
                        }?.also { (r, c) ->
                            map[r][c] = map[rowIndex][columnIndex]
                            map[rowIndex][columnIndex] = null

                            attack(r, c)
                        }
                    }

                    if (elves.none { it.alive } || goblins.none { it.alive }) {
                        gameover = true
                        fullround = true
                    }
                }
            }

//            println(round)
//            printMap()
//            println()

            if (gameover) {
                break
            }
        }

        return if (allowElfDeath || elves.all { it.alive }) {
            (round - if (fullround) 0 else 1) * (elves.sumOf { it.hp } + goblins.sumOf { it.hp })
        } else {
            -1
        }
    }


    println(process(3, true))

    var str = 4
    while (true) {
        val result2 = process(str, false)

        if (result2 >= 0) {
            println(result2)
            break
        }

        str++
    }
}

