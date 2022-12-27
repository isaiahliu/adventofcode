package y2022

import util.input

fun main() {
    val maxDigits = input.maxOf { it.length }

    val baseNum = CharArray(maxDigits) { '2' }.concatToString().toLong(5)

    val digits1 = "01234"
    val digits2 = "=-012"

    fun convertTo(num: Long): String {
        return (num + baseNum).toString(5).map {
            digits2[digits1.indexOf(it)]
        }.toCharArray().concatToString().trimStart('0').takeIf { it.isNotEmpty() } ?: "0"
    }

    fun convertFrom(num: String): Long {
        return num.padStart(maxDigits, '0').map {
            digits1[digits2.indexOf(it)]
        }.toCharArray().concatToString().toLong(5) - baseNum
    }

    println(convertTo(input.sumOf { convertFrom(it) }))
}