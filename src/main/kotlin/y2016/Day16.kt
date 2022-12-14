package y2016

import input

fun main() {
    var line = input.first()
//    var line = "10000"

    fun process(fitLength: Int): String {
        while (line.length < fitLength) {
            line = "${line}0${line.reversed().map { '1' - it }.joinToString("")}"
        }

        var checkSum = line.substring(0, fitLength)

        while (checkSum.length % 2 == 0) {
            checkSum = buildString {
                for (index in checkSum.indices step 2) {
                    append(1 - (checkSum[index] - '0').xor(checkSum[index + 1] - '0'))
                }
            }
        }

        return checkSum
    }
    println(process(272))
    println(process(35651584))
}

