package y2023

import util.input

fun main() {
    val instructions = input[0]

    val map = hashMapOf<String, Pair<String, String>>()

    val regex = "(\\w+) = \\((\\w+), (\\w+)\\)".toRegex()
    input.forEach {
        regex.matchEntire(it)?.groupValues?.drop(1)?.also { (node, left, right) ->
            map[node] = left to right
        }
    }

    val cache = hashMapOf<String, String>()
    fun walk(start: String, ends: Set<String>): Int {
        var result = 0

        var current = start
        while (current !in ends) {
            result++

            if (current !in cache) {
                instructions.forEach { inst ->
                    map[current]?.let { (left, right) ->
                        if (inst == 'L') left else right
                    }?.also {
                        cache[current] = it
                    }
                }
            }
            current = cache[current].orEmpty()
        }
        return result
    }

    val part1Result = walk("AAA", setOf("ZZZ")) * instructions.length
//    val part2Result = walk(map.keys.filter { it.last() == 'A' }.toSet(), map.keys.filter { it.last() == 'Z' }.toSet())
    val ends = map.keys.filter { it.last() == 'Z' }.toSet()
    val steps = map.keys.filter { it.last() == 'A' }.map {
        walk(it, ends).toBigInteger()
    }

    val gcd = steps.reduce { a, b ->
        a.gcd(b)
    }

    val part2Result = steps.reduce { a, b -> a * b } / gcd * instructions.length.toBigInteger()

    println(part1Result)
    println(part2Result)
}
