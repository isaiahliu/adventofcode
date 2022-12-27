package y2022

import util.input

fun main() {
    val maxDigits = input.maxOf { it.length }

    val baseNum = "".padStart(maxDigits, '2').toLong(5)

    fun convertTo(num: Long): String {
        return String((num + baseNum).toString(5).map { c ->
            when (c) {
                '0' -> '='
                '1' -> '-'
                '2' -> '0'
                '3' -> '1'
                '4' -> '2'
                else -> c
            }
        }.toCharArray()).trimStart('0')
    }

    fun convertFrom(num: String): Long {
        return String(num.padStart(maxDigits, '0').map { c ->
            when (c) {
                '=' -> '0'
                '-' -> '1'
                '0' -> '2'
                '1' -> '3'
                '2' -> '4'
                else -> c
            }
        }.toCharArray()).toLong(5) - baseNum
    }

    println(convertTo(input.sumOf { convertFrom(it) }))
}