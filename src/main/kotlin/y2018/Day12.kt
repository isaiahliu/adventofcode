package y2018

import util.input

fun main() {
    val initial = hashMapOf<Long, Boolean>()
    input.first().split(" ").last().forEachIndexed { index, c ->
        if (c == '#') {
            initial[index.toLong()] = true
        }
    }

    val spread = input.drop(2).map { it.split(" ") }.map {
        it[0].map { it == '#' } to (it[2] == "#")
    }


    fun process(times: Long): Long {
        var map: Map<Long, Boolean> = initial

        fun serializeMap(): Pair<Long, String> {
            return map.keys.min() to buildString {
                for (i in map.keys.min() - 2..map.keys.max() + 2) {
                    append(if (map[i] == true) "#" else ".")
                }
            }
        }

        var t = 0
        var lastSerialization = ""
        var lastStart = Long.MIN_VALUE
        var additional = 0L
        while (t < times) {
            map = (map.keys.min() - 2..map.keys.max() + 2).mapNotNull { index ->
                val posM2 = map[index - 2] ?: false
                val posM1 = map[index - 1] ?: false
                val pos = map[index] ?: false
                val pos1 = map[index + 1] ?: false
                val pos2 = map[index + 2] ?: false

                spread.firstOrNull { (pair, _) ->
                    pair[0] == posM2 && pair[1] == posM1 && pair[2] == pos && pair[3] == pos1 && pair[4] == pos2
                }?.second?.takeIf { it }?.let {
                    index to true
                }
            }.toMap()
            t++

            val (start, serialization) = serializeMap()

            if (serialization == lastSerialization) {
                additional = (times - t) * (start - lastStart) * map.size
                break
            } else {
                lastSerialization = serialization
                lastStart = start
            }
        }

        return map.keys.sum() + additional
    }

    println(process(20))
    println(process(50000000000))
}