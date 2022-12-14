package y2016

import util.input

fun main() {
    val list = Array<MutableList<Char>>(input.first().length) {
        arrayListOf()
    }

    input.forEach {
        it.forEachIndexed { index, c ->
            list[index] += c
        }
    }

    val result1 = list.joinToString("") {
        it.groupingBy { it }.eachCount().maxBy { it.value }.key.toString()
    }

    val result2 = list.joinToString("") {
        it.groupingBy { it }.eachCount().minBy { it.value }.key.toString()
    }

    println(result1)
    println(result2)
}