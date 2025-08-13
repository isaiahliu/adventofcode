package y2015

import util.expect
import util.input

fun main() {
    expect(0, 0) {
        val regex = "(\\w+) would (gain|lose) (\\d+) happiness units? by sitting next to (\\w+).".toRegex()
        val happinessMap = hashMapOf<String, MutableMap<String, Int>>()
        input.forEach {
            val match = regex.matchEntire(it) ?: return@forEach

            val from = match.groupValues[1]
            val to = match.groupValues[4]
            val happiness = match.groupValues[3].toInt() * if (match.groupValues[2] == "gain") 1 else -1

            happinessMap.computeIfAbsent(from) { hashMapOf() }[to] = happiness
        }

        fun dfs(queue: List<String>, score: Int) {
            if (queue.size == happinessMap.size) {
                val first = queue.first()
                val last = queue.last()

                part1Result = maxOf(part1Result, score + (happinessMap[first]?.get(last) ?: 0) + (happinessMap[last]?.get(first) ?: 0))
                part2Result = maxOf(part2Result, score)
            } else {
                happinessMap.keys.forEach {
                    if (it !in queue) {
                        dfs(queue + it, score + (queue.lastOrNull()?.let { last ->
                            (happinessMap[it]?.get(last) ?: 0) + (happinessMap[last]?.get(it) ?: 0)
                        } ?: 0))
                    }
                }
            }
        }

        dfs(emptyList(), 0)
    }
}
