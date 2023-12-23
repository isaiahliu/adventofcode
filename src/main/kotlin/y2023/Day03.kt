package y2023

import util.expect
import util.input

fun main() {
    expect(0) {
        val symbolNums = hashMapOf<Pair<Int, Int>, MutableList<Int>>()

        input.forEachIndexed { r, s ->
            var num = 0
            var length = 0
            ("$s.").forEachIndexed { c, ch ->
                when {
                    ch in '0'..'9' -> {
                        num *= 10
                        num += ch - '0'
                        length++
                    }

                    length > 0 -> {
                        ((c - length - 1..c).map {
                            listOf(r - 1 to it, r + 1 to it)
                        }.flatten() + setOf(r to c - length - 1, r to c)).firstOrNull { (r, c) ->
                            input.getOrNull(r)?.getOrNull(c)?.takeIf { it !in '0'..'9' && it != '.' } != null
                        }?.also {
                            symbolNums.computeIfAbsent(it) { arrayListOf() }.add(num)
                        }

                        num = 0
                        length = 0
                    }
                }
            }
        }

        part1Result = symbolNums.values.sumOf { it.sum() }

        part2Result =
            symbolNums.filter { input[it.key.first][it.key.second] == '*' && it.value.size > 1 }.values.sumOf {
                it.fold(1) { a, b -> a * b }.toInt()
            }
    }
}