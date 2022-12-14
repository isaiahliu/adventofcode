package y2015

import util.input

fun main() {
    val line = "{\"root\":${input.first()}}"

    val part1Sum = line.sumResult

    var root: JsonNode? = null

    var current = root

    line.forEach {
        when (it) {
            '{' -> {
                current = JsonNode(current)
                if (root == null) {
                    root = current
                }
            }

            '}' -> {
                current = current!!.parent
            }

            else -> {
                current!!.str.append(it)
            }
        }
    }

    println(part1Sum)
    println(root!!.sum)
}

private val regex = "(-?\\d+)".toRegex()

private val String.sumResult: Int
    get() {
        return regex.findAll(this).sumOf { it.groupValues[1].toInt() }
    }

private class JsonNode(val parent: JsonNode? = null) {
    init {
        parent?.children?.add(this)
    }

    var str: StringBuilder = StringBuilder()

    val children = arrayListOf<JsonNode>()

    val sum: Int
        get() {
            return if (str.contains(":\"red\"")) {
                0
            } else {
                str.toString().sumResult + children.sumOf { it.sum }
            }
        }
}