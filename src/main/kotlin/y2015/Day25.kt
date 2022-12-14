package y2015

import util.input

fun main() {
    val regex = "To continue, please consult the code grid in the manual.  Enter the code at row (\\d+), column (\\d+).".toRegex()

    val match = regex.matchEntire(input.first()) ?: return

    val row = match.groupValues[1].toInt()
    val column = match.groupValues[2].toInt()

    var current = 20151125L

    var currentRow = 1
    var currentColumn = 1

    fun next() {
        current = (current * 252533L) % 33554393
    }

    while (true) {
        if (currentRow == 1) {
            currentRow = currentColumn + 1
            currentColumn = 1
        } else {
            currentColumn++
            currentRow--
        }

        next()

        if (currentRow == row && currentColumn == column) {
            break
        }
    }

    println(current)
}