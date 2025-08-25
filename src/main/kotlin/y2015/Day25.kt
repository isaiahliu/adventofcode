package y2015

import util.expect
import util.input

fun main() {
    expect(0, "Merry Christmas!") {
        val (row, column) = "\\d+".toRegex().findAll(input.first()).map { it.groupValues[0].toInt() }.toList()

        val position = ((row + column - 2) * (row + column - 1) / 2 + column - 1).toBigInteger()

        val base = 20151125.toBigInteger()
        val factor = 252533.toBigInteger()
        val mod = 33554393.toBigInteger()

        part1Result = (base * factor.modPow(position, mod)).mod(mod).toInt()
    }
}