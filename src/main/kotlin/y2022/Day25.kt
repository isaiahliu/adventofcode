package y2022

import util.input

fun main() {
    val maxDigits = input.maxOf { it.length }

    val baseNum = String(CharArray(maxDigits) { '2' }).toLong(5)

    val digits1 = "01234"
    val digits2 = "=-012"

    fun convertTo(num: Long): String {
        return String((num + baseNum).toString(5).map {
            digits2[digits1.indexOf(it)]
        }.toCharArray()).trimStart('0')
    }

    fun convertFrom(num: String): Long {
        return String(num.padStart(maxDigits, '0').map {
            digits1[digits2.indexOf(it)]
        }.toCharArray()).toLong(5) - baseNum
    }

    println(convertTo(input.sumOf { convertFrom(it) }))
}