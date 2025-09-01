package y2016

import util.expect
import util.input
import java.math.BigInteger

fun main() {
    expect(BigInteger.ZERO, BigInteger.ZERO) {
        val regex = "Disc #\\d+ has (\\d+) positions; at time=(\\d+), it is at position (\\d+).".toRegex()

        part2Result = (input.mapIndexed { index, line ->
            regex.matchEntire(line)?.groupValues?.drop(1)?.map { it.toBigInteger() }?.let { (position, time, current) ->
                position to (time - current - (index + 1).toBigInteger())
            } ?: return@expect
        } + (11.toBigInteger() to (-input.size - 1).toBigInteger()))
            .reduce { (a, ma), (b, mb) ->
                part1Result = ma

                a * b to (a * (mb - ma) * a.modInverse(b) + ma).mod(a * b)
            }.second
    }
}
