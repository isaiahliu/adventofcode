package y2023

import util.expectLong
import util.input

fun main() {
    expectLong {
        var currentMaps = arrayListOf<Pair<Pair<Long, Long>, Long>>()
        val fullMap = arrayListOf(currentMaps)

        val seedInput = input.first().split(" ").mapNotNull { it.toLongOrNull() }

        val mapRegex = "(\\d+) (\\d+) (\\d+)".toRegex()
        input.drop(1).forEach {
            mapRegex.matchEntire(it)?.groupValues?.drop(1)?.map { it.toLong() }?.also { (to, from, size) ->
                currentMaps += from to to to size
            } ?: run {
                if (currentMaps.isNotEmpty()) {
                    currentMaps = arrayListOf()
                    fullMap.add(currentMaps)
                }
            }
        }

        fun List<Pair<Long, Long>>.calculate(maps: List<Pair<Pair<Long, Long>, Long>>): List<Pair<Long, Long>> {
            val mappedResult = arrayListOf<Pair<Long, Long>>()
            return maps.fold(this) { sources, map ->
                val (m, size) = map
                val (mapFrom, targetFrom) = m
                val mapTo = mapFrom + size - 1
                val offset = targetFrom - mapFrom

                sources.map { (from, to) ->
                    val remain = arrayListOf<Pair<Long, Long>>()

                    val left = maxOf(from, mapFrom)
                    val right = minOf(to, mapTo)

                    if (left <= right) {
                        mappedResult += left + offset to right + offset

                        if (from < left) {
                            remain += from to left - 1
                        }

                        if (to > right) {
                            remain += right + 1 to to
                        }
                    } else {
                        remain += from to to
                    }

                    remain
                }.flatten()
            } + mappedResult
        }

        part1Result = fullMap.fold(seedInput.map { it to it }) { seeds, maps ->
            seeds.calculate(maps)
        }.minOf { it.first }

        part2Result =
            fullMap.fold((seedInput.indices step 2).map { seedInput[it] to seedInput[it] + seedInput[it + 1] - 1 }) { seeds, maps ->
                seeds.calculate(maps)
            }.minOf { it.first }
    }
}