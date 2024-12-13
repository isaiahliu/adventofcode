package y2024

import util.expect
import util.input
import java.math.BigInteger

fun main() {
    expect(0L, 0L) {
        val regexA = "^Button A: X\\+(\\d+), Y\\+(\\d+)$".toRegex()
        val regexB = "^Button B: X\\+(\\d+), Y\\+(\\d+)$".toRegex()
        val regexPrize = "^Prize: X=(\\d+), Y=(\\d+)$".toRegex()

        var ax = BigInteger.ZERO
        var ay = BigInteger.ZERO

        var bx = BigInteger.ZERO
        var by = BigInteger.ZERO

        var px = BigInteger.ZERO
        var py = BigInteger.ZERO

        input.forEach {
            when {
                regexA.matches(it) -> {
                    regexA.matchEntire(it)?.groupValues?.drop(1)?.also { (x, y) ->
                        ax = x.toBigInteger()
                        ay = y.toBigInteger()
                    }
                }

                regexB.matches(it) -> {
                    regexB.matchEntire(it)?.groupValues?.drop(1)?.also { (x, y) ->
                        bx = x.toBigInteger()
                        by = y.toBigInteger()
                    }
                }

                regexPrize.matches(it) -> {
                    regexPrize.matchEntire(it)?.groupValues?.drop(1)?.also { (x, y) ->
                        px = x.toBigInteger()
                        py = y.toBigInteger()
                    }

                    //94a + 22b = 8400
                    //34a + 67b = 5400
//ax * a + bx * b = px
//ay * a + by + b = py
                    arrayOf(bx, by).also {
                        ax *= it[1]
                        bx *= it[1]

                        ay *= it[0]
                        by *= it[0]

                        val diff = ax - ay

                        if (diff != BigInteger.ZERO) {
                            px *= it[1]
                            py *= it[0]

                            if ((px - py) % diff == BigInteger.ZERO) {
                                val a = (px - py) / diff

                                if (bx != BigInteger.ZERO && (px - ax * a) % bx == BigInteger.ZERO) {
                                    val b = (px - ax * a) / bx

                                    if (b >= BigInteger.ZERO) {
                                        part1Result += (a * 3.toBigInteger() + b).toLong()
                                    }
                                }
                            }

                            px += 10000000000000.toBigInteger() * it[1]
                            py += 10000000000000.toBigInteger() * it[0]

                            if ((px - py) % diff == BigInteger.ZERO) {
                                val a = (px - py) / diff

                                if (bx != BigInteger.ZERO && (px - ax * a) % bx == BigInteger.ZERO) {
                                    val b = (px - ax * a) / bx

                                    if (b >= BigInteger.ZERO) {
                                        part2Result += (a * 3.toBigInteger() + b).toLong()
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}