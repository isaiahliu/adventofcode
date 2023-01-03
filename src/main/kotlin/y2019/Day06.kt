package y2019

import util.input

fun main() {
    class Node(val name: String) {
        val children = arrayListOf<Node>()

        var depth = 0

        fun containsChild(name: String): Boolean {
            return this.name == name || children.any { it.containsChild(name) }
        }
    }

    val nodeMap = hashMapOf<String, Node>()

    input.map { it.split(")") }.forEach { (from, to) ->
        nodeMap.computeIfAbsent(from) { Node(from) }.children += nodeMap.computeIfAbsent(to) { Node(to) }
    }

    val root = nodeMap[(nodeMap.keys - nodeMap.values.map { it.children.map { it.name } }.flatten().toSet()).first()]!!

    fun calculateDepth(node: Node, depth: Int) {
        node.depth = depth

        node.children.forEach {
            calculateDepth(it, depth + 1)
        }
    }

    calculateDepth(root, 0)

    println(nodeMap.values.sumOf { it.depth })

    val sanRoute = nodeMap.values.filter { it.containsChild("SAN") }.toSet()
    val youRoute = nodeMap.values.filter { it.containsChild("YOU") }.toSet()

    val t = sanRoute.intersect(youRoute).maxBy { it.depth }

    val san = nodeMap["SAN"]!!
    val you = nodeMap["YOU"]!!

    println(san.depth - t.depth + you.depth - t.depth - 2)
}