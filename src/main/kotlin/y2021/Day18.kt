package y2021

import util.input
import java.util.*

fun main() {
    data class Node(var value: Int, var depth: Int, var pos: Int) {
        val left: Boolean get() = left(depth)
        val right: Boolean get() = !left(depth)

        fun left(d: Int): Boolean {
            return pos and (1 shl d) == 0
        }

        fun increaseDepth(right: Boolean) {
            depth++

            pos *= 2
            if (right) {
                pos += 1
            }
        }
    }

    fun List<Node>.printSnails() {
        var depth = -1
        buildString {
            this@printSnails.forEach { node ->
                repeat(node.depth - depth) {
                    append("[")
                }

                append(node.value)

                depth = node.depth
                while (!node.left(depth)) {
                    append("]")

                    depth--
                }

                if (depth >= 0) {
                    append(",")
                }
            }
        }.also {
            println(it)
        }
    }

    fun List<Node>.calculate(): Int {
        val stack = LinkedList<Pair<Int, Int>>()

        val remaining = LinkedList(this.map { it.depth to it.value })
        while (remaining.isNotEmpty()) {
            val (topDepth, right) = remaining.pop()
            if (stack.isNotEmpty() && stack.peek().first == topDepth) {
                val (depth, left) = stack.pop()

                remaining.push(depth - 1 to left * 3 + right * 2)
            } else {
                stack.push(topDepth to right)
            }
        }

        return stack.peek().second
    }

    fun process(lines: List<String>): List<Node> {
        return lines.map {
            var depth = -1
            var pos = 0

            it.toCharArray().toList().mapNotNull {
                when (it) {
                    ',' -> {
                        pos += 1 shl depth
                        null
                    }

                    '[' -> {
                        depth++
                        null
                    }

                    ']' -> {
                        pos %= (1 shl depth)
                        depth--
                        null
                    }

                    else -> {
                        Node(it - '0', depth, pos)
                    }
                }
            }
        }.reduce { a, b ->
            a.forEach { it.increaseDepth(false) }
            b.forEach { it.increaseDepth(true) }

            val sum = LinkedList<Node>()

            sum += a
            sum += b

            while (true) {
                var foundIndex = -1
                for (index in 0 until sum.size - 1) {
                    val left = sum[index]
                    val right = sum[index + 1]
                    if (left.depth > 3 && left.left && right.depth == left.depth && right.right) {
                        foundIndex = index
                        break
                    }
                }

                if (foundIndex >= 0) {
                    sum.getOrNull(foundIndex - 1)?.also {
                        it.value += sum[foundIndex].value
                    }

                    sum.removeAt(foundIndex)

                    sum.getOrNull(foundIndex + 1)?.also {
                        it.value += sum[foundIndex].value
                    }

                    sum[foundIndex].also {
                        it.value = 0

                        it.pos %= 1 shl it.depth
                        it.depth--
                    }

                    continue
                }

                foundIndex = sum.indexOfFirst { it.value >= 10 }

                if (foundIndex >= 0) {
                    val leftValue = sum[foundIndex].value / 2
                    val rightValue = sum[foundIndex].value - leftValue

                    sum.add(foundIndex, sum[foundIndex].copy())

                    sum[foundIndex].value = leftValue
                    sum[foundIndex].depth++

                    sum[foundIndex + 1].value = rightValue
                    sum[foundIndex + 1].depth++
                    sum[foundIndex + 1].pos += 1 shl sum[foundIndex + 1].depth

                    continue
                }

                break
            }

            sum
        }
    }

    println(process(input).calculate())

    var result2 = 0
    for (i in input.indices) {
        for (j in i + 1 until input.size) {
            result2 = result2.coerceAtLeast(process(listOf(input[i], input[j])).calculate())
            result2 = result2.coerceAtLeast(process(listOf(input[j], input[i])).calculate())
        }
    }

    println(result2)
}