package y2020

import util.input

fun main() {
    val TOP = 0
    val RIGHT = 1
    val BOTTOM = 2
    val LEFT = 3

    val NO_FLIP = 0
    val VERTICAL_FLIP = 1
    val HORIZONTAL_FLIP = 2

    class Fragment(val id: Int, val data: Array<BooleanArray>) {
        var rotation: Int = 0
        var flipped: Int = NO_FLIP

        private val generates: HashMap<Pair<Int, Int>, Array<BooleanArray>> = hashMapOf()
        private val cuteSidesGenerates: HashMap<Pair<Int, Int>, Array<BooleanArray>> = hashMapOf()

        fun generate(cutSides: Boolean): Array<BooleanArray> {
            return if (cutSides) {
                cuteSidesGenerates.computeIfAbsent(rotation to flipped) {
                    generate(false).drop(1).dropLast(1).map {
                        it.drop(1).dropLast(1).toBooleanArray()
                    }.toTypedArray()
                }
            } else {
                generates.computeIfAbsent(rotation to flipped) {
                    val size = data.size
                    fun Int.reverse(): Int {
                        return size - this - 1
                    }

                    val image = Array(size) { rowIndex ->
                        BooleanArray(size) { columnIndex ->
                            when (rotation) {
                                0 -> {
                                    when (flipped) {
                                        NO_FLIP -> {
                                            data[rowIndex][columnIndex]
                                        }

                                        VERTICAL_FLIP -> {
                                            data[rowIndex.reverse()][columnIndex]
                                        }

                                        HORIZONTAL_FLIP -> {
                                            data[rowIndex][columnIndex.reverse()]
                                        }

                                        else -> throw RuntimeException("Error")
                                    }
                                }

                                1 -> {
                                    when (flipped) {
                                        NO_FLIP -> {
                                            data[columnIndex][rowIndex.reverse()]
                                        }

                                        VERTICAL_FLIP -> {
                                            data[columnIndex][rowIndex]
                                        }

                                        HORIZONTAL_FLIP -> {
                                            data[columnIndex.reverse()][rowIndex.reverse()]
                                        }

                                        else -> throw RuntimeException("Error")
                                    }
                                }

                                2 -> {
                                    when (flipped) {
                                        NO_FLIP -> {
                                            data[rowIndex.reverse()][columnIndex.reverse()]
                                        }

                                        VERTICAL_FLIP -> {
                                            data[rowIndex][columnIndex.reverse()]
                                        }

                                        HORIZONTAL_FLIP -> {
                                            data[rowIndex.reverse()][columnIndex]
                                        }

                                        else -> throw RuntimeException("Error")
                                    }
                                }

                                3 -> {
                                    when (flipped) {
                                        NO_FLIP -> {
                                            data[columnIndex.reverse()][rowIndex]
                                        }

                                        VERTICAL_FLIP -> {
                                            data[columnIndex.reverse()][rowIndex.reverse()]
                                        }

                                        HORIZONTAL_FLIP -> {
                                            data[columnIndex][rowIndex]
                                        }

                                        else -> throw RuntimeException("Error")
                                    }
                                }

                                else -> throw RuntimeException("Error")
                            }
                        }
                    }

                    image
                }
            }
        }

        fun side(
            direction: Int,
        ): BooleanArray {
            val generated = generate(false)

            val result = when (direction % 4) {
                TOP -> {
                    generated.first().toList().toBooleanArray()
                }

                RIGHT -> {
                    generated.map { it.last() }.toBooleanArray()
                }

                BOTTOM -> {
                    generated.last().reversed().toBooleanArray()
                }

                LEFT -> {
                    generated.map { it.first() }.reversed().toBooleanArray()
                }

                else -> throw RuntimeException("Error")
            }

            return result
        }
    }

    val fragments = arrayListOf<Fragment>()

    var tempId = 0
    val tempFrag = arrayListOf<BooleanArray>()

    (input + "").forEach {
        when {
            it.isBlank() -> {
                fragments += Fragment(tempId, tempFrag.toTypedArray())
                tempFrag.clear()
            }

            it.startsWith("Tile") -> {
                tempId = it.split(" ").last().dropLast(1).toInt()
            }

            else -> {
                tempFrag += it.map { it == '#' }.toBooleanArray()
            }
        }
    }

    fun matchSide(left: BooleanArray, right: BooleanArray): Boolean {
        return left.withIndex().all { (index, value) ->
            value == right[right.size - index - 1]
        }
    }

    val first = fragments.first()
    val image: Map<Pair<Int, Int>, Fragment> = mapOf(0 to 0 to first)
    val ids = fragments.toSet() - first

    fun walk(frags: Set<Fragment>, image: Map<Pair<Int, Int>, Fragment>): Map<Pair<Int, Int>, Fragment> {
        if (frags.isEmpty()) {
            return image
        }

        for ((pos, f) in image) {
            for (targetDirection in arrayOf(TOP, RIGHT, BOTTOM, LEFT)) {
                var targetPos: Pair<Int, Int>
                when (targetDirection) {
                    TOP -> {
                        targetPos = pos.first - 1 to pos.second
                    }

                    RIGHT -> {
                        targetPos = pos.first to pos.second + 1
                    }

                    BOTTOM -> {
                        targetPos = pos.first + 1 to pos.second
                    }

                    LEFT -> {
                        targetPos = pos.first to pos.second - 1
                    }

                    else -> throw RuntimeException("Error")
                }

                if (targetPos in image) {
                    continue
                }
//                if ((maxRowIndex.coerceAtLeast(pos.first) - minRowIndex.coerceAtMost(pos.first) + 1) * (maxColumnIndex.coerceAtLeast(
//                        pos.second
//                    ) - minColumnIndex.coerceAtMost(pos.second) + 1) > fragments.size
//                ) {
//                    continue
//                }

                val targetSide = f.side(targetDirection)

                for (fragment in frags) {
                    for (rotation in arrayOf(0, 1, 2, 3)) {
                        for (flipped in arrayOf(NO_FLIP, VERTICAL_FLIP, HORIZONTAL_FLIP)) {
                            fragment.rotation = rotation
                            fragment.flipped = flipped
                            if (matchSide(fragment.side(targetDirection + 2), targetSide)) {
                                val result = walk(frags - fragment, image + (targetPos to fragment))
                                if (result.isNotEmpty()) {
                                    return result
                                }
                            }
                        }
                    }
                }
            }
        }

        return emptyMap()
    }

    val result = walk(ids, image)

    val minRowIndex = result.keys.minOf { it.first }
    val maxRowIndex = result.keys.maxOf { it.first }
    val minColumnIndex = result.keys.minOf { it.second }
    val maxColumnIndex = result.keys.maxOf { it.second }

    println(1L * result[minRowIndex to minColumnIndex]!!.id * result[minRowIndex to maxColumnIndex]!!.id * result[maxRowIndex to minColumnIndex]!!.id * result[maxRowIndex to maxColumnIndex]!!.id)

    val size = fragments.first().data.size - 2

    var map = Array((maxRowIndex - minRowIndex + 1) * size) { rowIndex ->
        BooleanArray((maxColumnIndex - minColumnIndex + 1) * size) { columnIndex ->
            result[(minRowIndex + rowIndex / size) to (minColumnIndex + columnIndex / size)]!!.generate(true)[rowIndex % size][columnIndex % size]
        }
    }

    val sharpCount = map.sumOf { it.count { it } }

    fun Array<BooleanArray>.rotate(): Array<BooleanArray> {
        return Array(this.size) { r ->
            BooleanArray(this.size) { c ->
                this[c][this.size - r - 1]
            }
        }
    }

    fun Array<BooleanArray>.flip(): Array<BooleanArray> {
        return Array(this.size) { r ->
            BooleanArray(this.size) { c ->
                this[r][this.size - c - 1]
            }
        }
    }

    val maps = arrayListOf(map)
    repeat(3) {
        map = map.rotate()
        maps += map
    }

    map = map.flip()
    maps += map

    repeat(3) {
        map = map.rotate()
        maps += map
    }

    val monster = arrayOf(
        "                  # ", "#    ##    ##    ###", " #  #  #  #  #  #   "
    ).map { it.map { it == '#' }.toBooleanArray() }.toTypedArray()
    val monsterSharpCount = monster.sumOf { it.count { it } }

    val monsterCount = maps.maxOf { map ->
        var monsterCount = 0

        for (startRowIndex in 0 until map.size - monster.size) {
            for (startColumnIndex in 0 until map.size - monster[0].size) {
                var match = true

                loop@ for ((r, row) in monster.withIndex()) {
                    for ((c, node) in row.withIndex()) {
                        if (node && !map[startRowIndex + r][startColumnIndex + c]) {
                            match = false
                            break@loop
                        }
                    }
                }

                if (match) {
                    for ((r, row) in monster.withIndex()) {
                        for ((c, node) in row.withIndex()) {
                            if (node) {
                                map[startRowIndex + r][startColumnIndex + c] = false
                            }
                        }
                    }
                    monsterCount++
                }
            }
        }

        monsterCount
    }

    println(sharpCount - monsterCount * monsterSharpCount)
}