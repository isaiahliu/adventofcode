package y2024

import util.expect
import util.input
import kotlin.math.sign

fun main() {
    expect(0, 0) {
        val antennas = hashMapOf<Char, MutableList<Pair<Int, Int>>>()

        input.forEachIndexed { r, row ->
            row.forEachIndexed { c, ch ->
                if (ch != '.') {
                    antennas.computeIfAbsent(ch) { arrayListOf() } += r to c
                }
            }
        }

        val antinodes = Array(2) { hashSetOf<Pair<Int, Int>>() }
        antennas.values.forEach {
            if (it.size > 1) {
                antinodes[1] += it
            }

            for (i in it.indices) {
                val (r1, c1) = it[i]
                for (j in i + 1 until it.size) {
                    val (r2, c2) = it[j]

                    val deltaR = r2 - r1
                    val deltaC = c2 - c1

                    var step = 0
                    var r = r2
                    var c = c2
                    while (true) {
                        r += deltaR
                        c += deltaC

                        input.getOrNull(r)?.getOrNull(c)?.also {
                            antinodes[(step++).sign] += r to c
                        } ?: break
                    }

                    step = 0
                    r = r1
                    c = c1

                    while (true) {
                        r -= deltaR
                        c -= deltaC

                        input.getOrNull(r)?.getOrNull(c)?.also {
                            antinodes[(step++).sign] += r to c
                        } ?: break
                    }
                }
            }
        }
        part1Result = antinodes[0].size
        part2Result = antinodes[0].union(antinodes[1]).size
    }
}
