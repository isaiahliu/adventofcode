package y2020

import util.input
import java.math.BigInteger

fun main() {
    val time = input.first().toInt()

    val waitingTimes = input.drop(1).first().split(",").mapNotNull { it.toIntOrNull() }.map {
        it to (it - time % it) % it
    }

    println(waitingTimes.minBy { it.second }.let { it.first * it.second })

    val nums = input.drop(1).first().split(",").mapIndexedNotNull { index, num ->
        num.toBigIntegerOrNull()?.let { it to index.toBigInteger().negate() }
    }

    var diff = BigInteger.ZERO
    var d = nums.first().first
    var base = BigInteger.ZERO
    nums.drop(1).forEach { (b, m) ->
        base = (d.modInverse(b) * (m - diff)).mod(d * b)

        diff += d * base

        d *= b
    }

    println((base * d + diff) % d)
}