package y2018

import util.input

fun main() {
    val TREE = 1
    val OPEN = 0
    val LUMBERYARD = 2
    val map = input.map {
        it.map {
            when (it) {
                '#' -> LUMBERYARD
                '|' -> TREE
                else -> OPEN
            }
        }.toIntArray()
    }.toTypedArray()

    fun Array<IntArray>.count(rowIndex: Int, columnIndex: Int): Pair<Int, Int> {
        val group = arrayOf(rowIndex - 1 to columnIndex - 1, rowIndex - 1 to columnIndex, rowIndex - 1 to columnIndex + 1, rowIndex to columnIndex - 1, rowIndex to columnIndex + 1, rowIndex + 1 to columnIndex - 1, rowIndex + 1 to columnIndex, rowIndex + 1 to columnIndex + 1).mapNotNull { (r, c) ->
            getOrNull(r)?.getOrNull(c)
        }.groupingBy { it }.eachCount()

        return (group[TREE] ?: 0) to (group[LUMBERYARD] ?: 0)
    }

    fun Array<IntArray>.printMap() {
        forEach { row ->
            println(row.joinToString("") {
                when (it) {
                    TREE -> "|"
                    LUMBERYARD -> "#"
                    else -> "."
                }
            })
        }
        println()
    }

    fun Array<IntArray>.serializeMap(): String {
        return joinToString("") { row ->
            row.joinToString("") {
                when (it) {
                    TREE -> "|"
                    LUMBERYARD -> "#"
                    else -> "."
                }
            }
        }
    }

    fun process(times: Int): Int {
        var t = map

        val cache = arrayListOf(t.serializeMap())

        var remainingTimes = times

        var cachedResult = 0
        while (remainingTimes > 0) {
            remainingTimes--

            t = Array(t.size) { r ->
                IntArray(t[r].size) { c ->
                    val (tree, lumberyard) = t.count(r, c)

                    when (val current = t[r][c]) {
                        TREE -> {
                            if (lumberyard >= 3) {
                                LUMBERYARD
                            } else {
                                current
                            }
                        }

                        LUMBERYARD -> {
                            if (tree >= 1 && lumberyard >= 1) {
                                current
                            } else {
                                OPEN
                            }
                        }

                        else -> {
                            if (tree >= 3) {
                                TREE
                            } else {
                                current
                            }
                        }
                    }
                }
            }

            val serialization = t.serializeMap()

            val cacheIndex = cache.indexOf(serialization)
            if (cacheIndex == -1) {
                cache += serialization
            } else {
                val loop = cache.drop(cacheIndex)

                val cacheString = loop[remainingTimes % loop.size]

                cachedResult = cacheString.count { it == '#' } * cacheString.count { it == '|' }

                break
            }
        }

        return cachedResult.takeIf { it > 0 } ?: run {
            val treeCount = t.sumOf { it.count { it == TREE } }
            val lumberyardCount = t.sumOf { it.count { it == LUMBERYARD } }

            treeCount * lumberyardCount
        }
    }

    println(process(10))
    println(process(1000000000))
}