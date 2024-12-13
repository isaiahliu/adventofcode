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

                    (bx to by).also { (mulx, muly) ->
                        ax *= muly
                        bx *= muly

                        ay *= mulx
                        by *= mulx

                        val diff = ax - ay

                        if (diff != BigInteger.ZERO) {
                            px *= muly
                            py *= mulx

                            if ((px - py) % diff == BigInteger.ZERO) {
                                val a = (px - py) / diff

                                if (bx != BigInteger.ZERO && (px - ax * a) % bx == BigInteger.ZERO) {
                                    val b = (px - ax * a) / bx

                                    if (b >= BigInteger.ZERO) {
                                        part1Result += (a * 3.toBigInteger() + b).toLong()
                                    }
                                }
                            }

                            px += 10000000000000.toBigInteger() * muly
                            py += 10000000000000.toBigInteger() * mulx

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