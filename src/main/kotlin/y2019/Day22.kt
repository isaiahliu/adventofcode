package y2019

import util.input
import java.math.BigInteger

fun main() {
    var CARD_COUNT = 10007L.toBigInteger()

    var pos = 2019L.toBigInteger()

    input.map { it.split(" ") }.forEach {
        when {
            it[0] == "cut" -> {
                val cut = it[1].toBigInteger().mod(CARD_COUNT)

                pos = (pos - cut).mod(CARD_COUNT)
            }

            it[0] == "deal" && it[1] == "with" -> {
                val increment = it[3].toBigInteger()
                pos = (pos * increment) % CARD_COUNT
            }

            it[0] == "deal" && it[1] == "into" -> {
                pos = CARD_COUNT - pos - BigInteger.ONE
            }
        }
    }
    println(pos)

    CARD_COUNT = 119315717514047L.toBigInteger()
    pos = 2020L.toBigInteger()

    var offset = BigInteger.ZERO
    var increment = BigInteger.ONE

    input.map { it.split(" ") }.forEach {
        when {
            it[0] == "cut" -> {
                val cut = it[1].toBigInteger().mod(CARD_COUNT)

                offset -= cut
                offset = offset.mod(CARD_COUNT)
            }

            it[0] == "deal" && it[1] == "with" -> {
                val t = it[3].toBigInteger()

                increment *= t
                increment %= CARD_COUNT

                offset *= t
                offset %= CARD_COUNT
            }

            it[0] == "deal" && it[1] == "into" -> {
                increment = increment.negate()

                offset = offset.negate() - BigInteger.ONE
            }
        }
    }

    val times = 101741582076661L.toBigInteger()

    //For one time, y = ax + b
    //For n times, y = a^n*x + (b^n - 1) / (b - 1)
    val a = increment.modInverse(CARD_COUNT)
    val b = (a * offset.negate()).mod(CARD_COUNT)

    val an = a.modPow(times, CARD_COUNT)
    var result = (pos * an) % CARD_COUNT

    result += (a - BigInteger.ONE).modInverse(CARD_COUNT) * (an - BigInteger.ONE) * b

    result %= CARD_COUNT

    println(result)
}