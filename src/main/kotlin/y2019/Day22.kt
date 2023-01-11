package y2019

import util.input
import java.math.BigInteger

fun main() {
    val CARD_COUNT1 = 10007L.toBigInteger()
    val CARD_COUNT2 = 119315717514047L.toBigInteger()

    var pos = 2019L.toBigInteger()

    var offset = BigInteger.ZERO
    var increment = BigInteger.ONE
    input.map { it.split(" ") }.forEach {
        when {
            it[0] == "cut" -> {
                val cut = it[1].toBigInteger()

                pos = (pos - cut).mod(CARD_COUNT1)

                offset -= cut
                offset = offset.mod(CARD_COUNT2)
            }

            it[0] == "deal" && it[1] == "with" -> {
                val inc = it[3].toBigInteger()
                pos = (pos * inc) % CARD_COUNT1

                increment *= inc
                increment %= CARD_COUNT2

                offset *= inc
                offset %= CARD_COUNT2
            }

            it[0] == "deal" && it[1] == "into" -> {
                pos = CARD_COUNT1 - pos - BigInteger.ONE

                increment = increment.negate()
                offset = offset.negate() - BigInteger.ONE
            }
        }
    }
    println(pos)


    pos = 2020L.toBigInteger()

    val times = 101741582076661L.toBigInteger()

    //For one time, y = ax + b
    //For n times, y = a^n*x + b*(a^n-1)/(a-1)
    val a = increment.modInverse(CARD_COUNT2)
    val b = (a * offset.negate()).mod(CARD_COUNT2)

    val an = a.modPow(times, CARD_COUNT2)
    pos = (pos * an) % CARD_COUNT2

    pos += (a - BigInteger.ONE).modInverse(CARD_COUNT2) * (an - BigInteger.ONE) * b

    pos %= CARD_COUNT2

    println(pos)
}