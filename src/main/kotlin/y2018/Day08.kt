package y2018

import util.input

fun main() {
    val nodes = input.first().split(" ").map { it.toInt() }.toIntArray()

    var index = 0
    fun walk(): Day07Node {
        val node = Day07Node()

        val childrenCount = nodes[index++]
        val metaCount = nodes[index++]

        repeat(childrenCount) {
            node.children += walk()
        }

        repeat(metaCount) {
            node.meta += nodes[index++]
        }

        return node
    }

    val root = walk()

    println(root.sum)
    println(root.sum2)
}

private class Day07Node {
    val meta: MutableList<Int> = arrayListOf()

    val children: MutableList<Day07Node> = arrayListOf()

    val sum: Int get() = meta.sum() + children.sumOf { it.sum }

    val sum2: Int
        get() {
            return if (children.isEmpty()) {
                meta.sum()
            } else {
                meta.sumOf {
                    children.getOrNull(it - 1)?.sum2 ?: 0
                }
            }
        }
}