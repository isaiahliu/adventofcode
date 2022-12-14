package y2015

import input
import md5

fun main() {
    var num = 1

    var fiveFound = false
    var sixFound = false

    var fiveResult = 0
    var sixResult = 0
    while (!fiveFound || !sixFound) {
        val md5 = "$input$num".md5

        if (!fiveFound && md5.startsWith("00000")) {
            fiveFound = true

            fiveResult = num
        }

        if (!sixFound && md5.startsWith("000000")) {
            sixFound = true

            sixResult = num
        }

        num++
    }

    println(fiveResult)
    println(sixResult)
}
