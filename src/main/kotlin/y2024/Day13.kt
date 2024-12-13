package y2024

import util.expect
import util.input

fun main() {
    expect(0L, 0L) {
        val regexA = "^Button A: X\\+(\\d+), Y\\+(\\d+)$".toRegex()
        val regexB = "^Button B: X\\+(\\d+), Y\\+(\\d+)$".toRegex()
        val regexPrize = "^Prize: X=(\\d+), Y=(\\d+)$".toRegex()

        var ax = 0L
        var ay = 0L

        var bx = 0L
        var by = 0L

        input.forEach {
            regexA.matchEntire(it)?.groupValues?.drop(1)?.also { (x, y) ->
                ax = x.toLong()
                ay = y.toLong()
            } ?: regexB.matchEntire(it)?.groupValues?.drop(1)?.also { (x, y) ->
                bx = x.toLong()
                by = y.toLong()
            } ?: regexPrize.matchEntire(it)?.groupValues?.drop(1)?.also { (x, y) ->
                var px = x.toLong()
                var py = y.toLong()

                (bx to by).also { (mulx, muly) ->
                    ax *= muly
                    bx *= muly

                    ay *= mulx
                    by *= mulx

                    val diff = ax - ay

                    if (diff != 0L) {
                        px *= muly
                        py *= mulx

                        if ((px - py) % diff == 0L) {
                            val a = (px - py) / diff

                            if (bx != 0L && (px - ax * a) % bx == 0L) {
                                val b = (px - ax * a) / bx

                                if (b >= 0L) {
                                    part1Result += (a * 3 + b)
                                }
                            }
                        }

                        px += 10000000000000 * muly
                        py += 10000000000000 * mulx

                        if ((px - py) % diff == 0L) {
                            val a = (px - py) / diff

                            if (bx != 0L && (px - ax * a) % bx == 0L) {
                                val b = (px - ax * a) / bx

                                if (b >= 0L) {
                                    part2Result += (a * 3 + b)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}