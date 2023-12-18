package y2023

import util.expectLong
import util.input

fun main() {
    expectLong {
        fun dfs(
            map: String,
            counts: IntArray,
            size: Int = 0,
            mapIndex: Int = 0,
            countIndex: Int = 0,
            cache: Array<Array<LongArray>> = Array(map.length) { Array(counts.size + 1) { LongArray(map.length) { -1L } } }
        ): Long {
            return when {
                countIndex > counts.size -> 0
                size > counts.getOrElse(countIndex) { 0 } -> 0
                mapIndex == map.length && countIndex == counts.size -> 1
                mapIndex == map.length -> 0
                cache[mapIndex][countIndex][size] >= 0 -> cache[mapIndex][countIndex][size]
                else -> {
                    var result = 0L

                    if (map[mapIndex] == '#' || map[mapIndex] == '?') {
                        result += dfs(map, counts, size + 1, mapIndex + 1, countIndex, cache)
                    }

                    if (map[mapIndex] == '.' || map[mapIndex] == '?') {
                        if (size == 0 || countIndex == counts.size) {
                            result += dfs(map, counts, 0, mapIndex + 1, countIndex, cache)
                        } else if (size == counts[countIndex]) {
                            result += dfs(map, counts, 0, mapIndex + 1, countIndex + 1, cache)
                        }
                    }

                    cache[mapIndex][countIndex][size] = result

                    result
                }
            }
        }

        input.forEach {
            val (map, counts) = it.split(" ").let { (m, c) -> m to c.split(",").map { it.toInt() }.toIntArray() }

            part1Result += dfs("$map.", counts)
            part2Result += dfs(
                "${map}?${map}?${map}?${map}?${map}.",
                IntArray(counts.size * 5) { counts[it % counts.size] })
        }
    }
}