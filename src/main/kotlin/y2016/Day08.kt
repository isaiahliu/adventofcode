package y2016

import util.input

fun main() {
    val width = 50
    val height = 6

    val screen = Array(height) { Array(width) { " " } }

    fun rotateRow(rowIndex: Int) {
        val t = screen[rowIndex][width - 1]

        repeat(width - 1) {
            screen[rowIndex][width - it - 1] = screen[rowIndex][width - it - 2]
        }

        screen[rowIndex][0] = t
    }

    fun rotateColumn(columnIndex: Int) {
        val t = screen[height - 1][columnIndex]

        repeat(height - 1) {
            screen[height - it - 1][columnIndex] = screen[height - it - 2][columnIndex]
        }

        screen[0][columnIndex] = t
    }

    input.map { it.split(" ") }.forEach { nodes ->
        when (nodes[0]) {
            "rect" -> {
                val (x, y) = nodes[1].split("x")
                repeat(x.toInt()) { column ->
                    repeat(y.toInt()) { row ->
                        screen[row][column] = "#"
                    }
                }
            }

            else -> {
                val index = nodes[2].split("=")[1].toInt()
                val times = nodes[4].toInt()

                repeat(times) {
                    when (nodes[1]) {
                        "row" -> {
                            rotateRow(index)
                        }

                        "column" -> {
                            rotateColumn(index)
                        }
                    }
                }
            }
        }
    }
    println(screen.sumOf { it.count { it.isNotBlank() } })

    screen.forEach {
        println(it.joinToString(""))
    }
}