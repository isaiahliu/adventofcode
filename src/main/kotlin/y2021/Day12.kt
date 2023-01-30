package y2021

import util.input

fun main() {
    val map = hashMapOf<String, MutableSet<String>>()

    fun add(c1: String, c2: String) {
        if (c2 == "start") {
            return
        }

        if (c1 == "end") {
            return
        }

        map.computeIfAbsent(c1) { hashSetOf() } += c2
    }

    input.map { it.split("-") }.forEach { (c1, c2) ->
        add(c1, c2)
        add(c2, c1)
    }

    fun walk(cave: String, visited: Set<String>, smallVisited: Boolean): Int {
        if (cave == "end") {
            return 1
        }

        return map[cave]?.sumOf {
            var t = smallVisited
            if (it == it.lowercase() && it in visited) {
                if (smallVisited) {
                    return@sumOf 0
                } else {
                    t = true
                }
            }
            walk(it, visited + it, t)
        } ?: 0
    }

    println(walk("start", emptySet(), true))
    println(walk("start", emptySet(), false))
}