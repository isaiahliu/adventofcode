package y2022

import util.input

fun main() {
    val line = input.firstOrNull() ?: return

    val room = Array(1000000) { BooleanArray(7) }

    var top = -1

    var shapeIndex = 0
    val shapeInitializer = arrayOf(::Shape1, ::Shape2, ::Shape3, ::Shape4, ::Shape5)

    var currentShape = shapeInitializer[(shapeIndex++) % 5](room)
    currentShape.initialize(top)

    var movementIndex = 0

    val cache1 = StringBuilder()
    val cache2 = StringBuilder()

    var result1 = -1
    var result2 = -1L
    var cacheStartTop = 0L
    val targetRocks = 1000000000000
    while (result1 < 0 || result2 < 0) {
        when (line[(movementIndex++) % line.length]) {
            '<' -> {
                currentShape.moveLeft()
            }

            '>' -> {
                currentShape.moveRight()
            }
        }

        if (!currentShape.moveDown()) {
            val topIncrement = (currentShape.top - top).coerceAtLeast(0)

            //Begin caching
            if (shapeIndex > 500) {
                val cacheKey = "${shapeIndex % 5}-${movementIndex % line.length}-${topIncrement},"

                if (cache1.isEmpty()) {
                    cacheStartTop = top.toLong()
                    cache1.append(cacheKey)
                } else {
                    if (!cache1.startsWith(cache2.toString() + cacheKey)) {
                        cache1.append(cache2)
                        cache2.clear()
                    }
                    cache2.append(cacheKey)

                    if (cache1.contentEquals(cache2)) {
                        val cacheNodes = cache1.split(",").mapNotNull { it.split("-").getOrNull(2)?.toIntOrNull() }
                        result2 =
                            cacheStartTop + (targetRocks - 500) / cacheNodes.size * cacheNodes.sum() + cacheNodes.take(((targetRocks - 500) % cacheNodes.size).toInt())
                                .sum() + 1
                    }
                }
            }

            top = top.coerceAtLeast(currentShape.top)

            currentShape = shapeInitializer[(shapeIndex++) % 5](room)
            currentShape.initialize(top)

            if (shapeIndex == 2023) {
                result1 = top + 1
//                result2 = top + 1
            }
        }
    }

    println(result1)
    println(result2)
}

private sealed class AbstractShape(protected val room: Array<BooleanArray>) {
    val nodes: MutableList<Array<Int>> = arrayListOf()

    val top: Int get() = nodes.maxOf { it[0] }

    fun move(rowMove: (Int) -> Int, columnMove: (Int) -> Int): Boolean {
        return if (nodes.all { (row, column) ->
                (room.getOrNull(rowMove(row))?.getOrNull(columnMove(column)) == false)
            }) {
            nodes.forEach {
                it[0] = rowMove(it[0])
                it[1] = columnMove(it[1])
            }

            true
        } else {
            false
        }
    }

    fun moveLeft(): Boolean {
        return move({ it }, { it - 1 })
    }

    fun moveRight(): Boolean {
        return move({ it }, { it + 1 })
    }

    fun moveDown(): Boolean {
        val canMove = move({ it - 1 }, { it })

        if (!canMove) {
            nodes.forEach { (row, column) ->
                room[row][column] = true
            }
        }
        return canMove
    }

    abstract fun initialize(top: Int)
}

private class Shape1(room: Array<BooleanArray>) : AbstractShape(room) {
    override fun initialize(top: Int) {
        nodes += arrayOf((top + 4), 2)
        nodes += arrayOf((top + 4), 3)
        nodes += arrayOf((top + 4), 4)
        nodes += arrayOf((top + 4), 5)
    }
}

private class Shape2(room: Array<BooleanArray>) : AbstractShape(room) {
    override fun initialize(top: Int) {
        nodes += arrayOf((top + 4), 3)
        nodes += arrayOf((top + 5), 2)
        nodes += arrayOf((top + 5), 3)
        nodes += arrayOf((top + 5), 4)
        nodes += arrayOf((top + 6), 3)
    }
}

private class Shape3(room: Array<BooleanArray>) : AbstractShape(room) {
    override fun initialize(top: Int) {
        nodes += arrayOf((top + 4), 2)
        nodes += arrayOf((top + 4), 3)
        nodes += arrayOf((top + 4), 4)
        nodes += arrayOf((top + 5), 4)
        nodes += arrayOf((top + 6), 4)
    }
}

private class Shape4(room: Array<BooleanArray>) : AbstractShape(room) {
    override fun initialize(top: Int) {
        nodes += arrayOf((top + 4), 2)
        nodes += arrayOf((top + 5), 2)
        nodes += arrayOf((top + 6), 2)
        nodes += arrayOf((top + 7), 2)
    }
}

private class Shape5(room: Array<BooleanArray>) : AbstractShape(room) {
    override fun initialize(top: Int) {
        nodes += arrayOf((top + 4), 2)
        nodes += arrayOf((top + 4), 3)
        nodes += arrayOf((top + 5), 2)
        nodes += arrayOf((top + 5), 3)
    }
}
