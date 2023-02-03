package y2017

import util.input

fun main() {
    val nodes = input.map { it.split("/").map { it.toInt() } }

    val lengthResults = hashMapOf<Int, Int>()
    fun walk(sum: Int, length: Int, currentNum: Int, nodes: List<List<Int>>): Int {
        return nodes.filter { it.contains(currentNum) }.maxOfOrNull {
            walk(
                sum + it.sum(),
                length + 1,
                it.fold(currentNum, Int::xor),
                nodes.toMutableList().also { list -> list -= it })
        } ?: run {
            lengthResults[length] = (lengthResults[length] ?: 0).coerceAtLeast(sum)
            sum
        }
    }

    println(walk(0, 0, 0, nodes))
    println(lengthResults[lengthResults.keys.max()])
}