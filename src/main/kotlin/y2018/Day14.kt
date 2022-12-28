package y2018

import util.input

fun main() {
    val target = input.first()
    val targetCount = target.toInt()

    val recipes = StringBuilder("37")
    var result1 = ""
    var result2 = -1

    var matchIndex = 0
    fun append(digit: Int) {
        recipes.append(digit.toString())

        if (result2 < 0 && target[matchIndex] - '0' == digit) {
            matchIndex++

            if (matchIndex == target.length) {
                result2 = recipes.length - target.length
            }
        } else {
            matchIndex = 0
        }
    }

    var first = 0
    var second = 1

    while (result1.isEmpty() || result2 < 0) {
        val firstNum = recipes[first] - '0'
        val secondNum = recipes[second] - '0'

        (firstNum + secondNum).toString().map { it - '0' }.forEach { append(it) }

        first = (first + firstNum + 1) % recipes.length
        second = (second + secondNum + 1) % recipes.length

        if (result1.isEmpty() && recipes.length >= targetCount + 10) {
            result1 = recipes.drop(targetCount).take(10).toString()
        }
    }

    println(result1)
    println(result2)
}

