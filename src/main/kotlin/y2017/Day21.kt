package y2017

import util.input

fun main() {
    val mapping = input.map {
        it.split(" => ").let { it[0].split("/").map { it.toCharArray() } to it[1].split("/").map { it.toCharArray() } }
    }

    var array = arrayOf(
        charArrayOf('.', '#', '.'),
        charArrayOf('.', '.', '#'),
        charArrayOf('#', '#', '#')
    )

    fun Array<CharArray>.transform(): Array<CharArray> {
        val subSize = if (this.size % 2 == 0) {
            2
        } else {
            3
        }

        val result = Array(this.size / subSize * (subSize + 1)) {
            CharArray(this.size / subSize * (subSize + 1)) { '.' }
        }

        repeat(this.size / subSize) { gridRowIndex ->
            repeat(this.size / subSize) { gridColumnIndex ->
                val mappingResult = mapping.filter { it.first.size == subSize }.first { (art, _) ->
                    (0 until 8).any {
                        val matcher: (row: Int, column: Int) -> Char = when (it) {
                            0 -> { row, column -> art[row][column] }
                            1 -> { row, column -> art[row][subSize - 1 - column] }
                            2 -> { row, column -> art[subSize - 1 - row][column] }
                            3 -> { row, column -> art[subSize - 1 - row][subSize - 1 - column] }
                            4 -> { row, column -> art[column][row] }
                            5 -> { row, column -> art[subSize - 1 - column][row] }
                            6 -> { row, column -> art[column][subSize - 1 - row] }
                            else -> { row, column -> art[subSize - 1 - column][subSize - 1 - row] }
                        }

                        (0 until subSize).all { row ->
                            (0 until subSize).all { column ->
                                this[gridRowIndex * subSize + row][gridColumnIndex * subSize + column] == matcher(
                                    row,
                                    column
                                )
                            }
                        }
                    }
                }.second

                repeat(subSize + 1) { row ->
                    repeat(subSize + 1) { column ->
                        result[gridRowIndex * (subSize + 1) + row][gridColumnIndex * (subSize + 1) + column] =
                            mappingResult[row][column]
                    }
                }
            }
        }

        return result
    }

    repeat(5) {
        array = array.transform()
    }

    println(array.sumOf { it.count { it == '#' } })

    repeat(18 - 5) {
        array = array.transform()
    }

    println(array.sumOf { it.count { it == '#' } })
}