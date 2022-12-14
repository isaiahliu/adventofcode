package y2017

import util.input

fun main() {
    val nodes = hashMapOf<String, Node>()

    input.map { it.split(" ") }.forEach {
        val name = it[0]
        val node = nodes.computeIfAbsent(name) { Node(name) }

        node.weight = it[1].trim('(', ')').toInt()

        it.drop(3).forEach {
            val childName = it.trim(',')
            val child = nodes.computeIfAbsent(childName) { Node(childName) }

            node.children += child
            child.parent = node
        }
    }

    val root = nodes.values.first { it.parent == null }

    println(root.name)

    root.clearTotalWeight()

    val unbalancedNodes = nodes.values.filter { it.children.map { it.totalWeight }.distinct().size > 1 }.toMutableList()

    val additionalWeight = unbalancedNodes.firstOrNull { it.children.size > 2 }?.children?.let {
        val weights = it.groupingBy { it.totalWeight }.eachCount().entries

        val diffWeight = weights.first { it.value == 1 }.key
        val balanceWeight = weights.first { it.value != 1 }.key

        diffWeight - balanceWeight
    } ?: 0

    unbalancedNodes.toList().forEach {
        it.parent?.also { unbalancedNodes -= it }
    }

    val unbalanceChildren = unbalancedNodes.first().children

    val higherWeight = unbalanceChildren.maxOf { it.totalWeight }
    val lowerWeight = unbalanceChildren.minOf { it.totalWeight }
    val result2 = if (additionalWeight > 0) {
        unbalanceChildren.first { it.totalWeight == higherWeight }.weight - additionalWeight
    } else {
        unbalanceChildren.first { it.totalWeight == lowerWeight }.weight - additionalWeight
    }
    println(result2)
}

private class Node(val name: String) {
    var weight: Int = 0

    var parent: Node? = null

    val children = arrayListOf<Node>()

    private var _totalWeight: Int? = null

    fun clearTotalWeight() {
        _totalWeight = null

        children.forEach { it.clearTotalWeight() }
    }

    val totalWeight: Int
        get() {
            if (_totalWeight == null) {
                _totalWeight = weight + children.sumOf { it.totalWeight }
            }
            return _totalWeight!!
        }
}