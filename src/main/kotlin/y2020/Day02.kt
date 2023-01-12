package y2020

import util.input

fun main() {
    val regex = "(\\d+)-(\\d+) (\\w): (\\w+)".toRegex()

    var result1 = 0
    var result2 = 0
    input.mapNotNull { regex.matchEntire(it)?.groupValues?.drop(1) }.forEach {
        val (min, max) = it.take(2).map { it.toInt() }
        val char = it[2][0]
        val password = it[3]

        if (password.count { it == char } in min..max) {
            result1++
        }


        if (arrayOf(password[min - 1], password[max - 1]).count { it == char } == 1) {
            result2++
        }
    }

    println(result1)
    println(result2)
}