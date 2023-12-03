package y2023

import util.input

fun main() {
    fun Pair<Pair<Int, Int>, Int>.adjacent(symbol: Pair<Int, Int>): Boolean {
        val (p, num) = this
        val (r, c) = p

        val (symbolR, symbolC) = symbol
        val length = num.toString().length

        return r == symbolR && (c - 1 == symbolC || c + length == symbolC) || (c - 1..c + length).any {
            r - 1 == symbolR && it == symbolC || r + 1 == symbolR && it == symbolC
        }
    }

    val nums = arrayListOf<Pair<Pair<Int, Int>, Int>>()
    val symbols = hashSetOf<Pair<Int, Int>>()
    val times = hashSetOf<Pair<Int, Int>>()
    input.forEachIndexed { r, s ->
        var num = 0
        s.forEachIndexed { c, ch ->
            if (ch in '0'..'9') {
                num *= 10
                num += ch - '0'
            } else {
                if (num > 0) {
                    nums.add(r to c - num.toString().length to num)
                }

                num = 0

                if (ch != '.') {
                    symbols.add(r to c)

                    if (ch == '*') {
                        times.add(r to c)
                    }
                }
            }
        }

        if (num > 0) {
            nums.add(r to s.length - num.toString().length to num)
        }
    }

    val part1Result = nums.filter { num ->
        symbols.any { num.adjacent(it) }
    }.sumOf { it.second }

    val part2Result = times.sumOf { sym ->
        nums.filter { it.adjacent(sym) }.takeIf { it.size > 1 }?.fold(1) { a, b -> a * b.second }?.toInt() ?: 0
    }

    println(part1Result)
    println(part2Result)
}
