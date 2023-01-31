package y2021

import util.input

fun main() {
    val init = input.first()

    val map = hashMapOf<Pair<Char, Char>, Char>()

    operator fun LongArray.set(c: Char, value: Long) {
        this[c - 'A'] = value
    }

    operator fun LongArray.get(c: Char): Long {
        return this[c - 'A']
    }

    fun LongArray.merge(target: LongArray) {
        this.forEachIndexed { index, i ->
            this[index] = i + target[index]
        }
    }

    fun Pair<Char, Char>.cacheKey(depth: Int): String {
        return "${first}${second}_${depth}"
    }

    input.drop(2).map { it.split(" -> ").let { it[0][0] to it[0][1] to it[1][0] } }.forEach { (pair, c) ->
        map[pair] = c
    }

    fun process(depth: Int): Long {
        val counts = LongArray(26)
        val cache = hashMapOf<String, LongArray>()

        fun walk(pair: Pair<Char, Char>, depth: Int): LongArray? {
            if (depth == 0) {
                return null
            }

            val cacheKey = pair.cacheKey(depth)

            if (cacheKey in cache) {
                return cache[cacheKey]
            }

            val result = LongArray(26)

            val m = map[pair] ?: throw RuntimeException("Missing map value")

            result[m]++

            walk(pair.first to m, depth - 1)?.also { result.merge(it) }
            walk(m to pair.second, depth - 1)?.also { result.merge(it) }

            cache[cacheKey] = result
            
            return result
        }

        init.forEach { counts[it]++ }

        for (index in 0 until init.length - 1) {
            walk(init[index] to init[index + 1], depth)?.also { counts.merge(it) }
        }


        return counts.max() - counts.filter { it > 0 }.min()
    }

    println(process(10))
    println(process(40))
}