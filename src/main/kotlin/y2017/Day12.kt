package y2017

import util.input

fun main() {
    val map = hashMapOf<Int, MutableSet<Int>>()
    input.map { it.split(" ") }.forEach {
        val left = it[0].toInt()

        it.drop(2).map { it.trimEnd(',').toInt() }.forEach {
            map.computeIfAbsent(left) { hashSetOf() } += it
            map.computeIfAbsent(it) { hashSetOf() } += left
        }
    }

    fun walk(id: Int, group: MutableSet<Int>) {
        if (group.add(id)) {
            map[id]?.forEach {
                walk(it, group)
            }
        }
    }

    val result1 = hashSetOf<Int>()

    walk(0, result1)

    println(result1.size)

    val remaining = map.keys.toHashSet()
    var result2 = 0

    while (remaining.isNotEmpty()) {
        val group = hashSetOf<Int>()

        walk(remaining.first(), group)

        result2++

        remaining.removeAll(group)
    }

    println(result2)
}