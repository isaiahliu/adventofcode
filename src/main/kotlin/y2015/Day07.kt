package y2015

import util.input

fun main() {
    val exp = "(.*) -> (\\w+)".toRegex()

    val part1 = hashMapOf<String, Node>()

    input.mapNotNull { exp.matchEntire(it) }.forEach {
        val left = it.groupValues[1]
        val right = it.groupValues[2]

        val rightNode = part1.computeIfAbsent(right) { Node() }

        val tokens = left.split(" ")

        rightNode.expression = when (tokens.size) {
            3 -> {
                when (tokens[1]) {
                    "OR" -> {
                        OrExp(part1.toExpression(tokens[0]), part1.toExpression(tokens[2]))
                    }

                    "LSHIFT" -> {
                        ShiftLExp(part1.toExpression(tokens[0]), part1.toExpression(tokens[2]))
                    }

                    "RSHIFT" -> {
                        ShiftRExp(part1.toExpression(tokens[0]), part1.toExpression(tokens[2]))
                    }

                    else -> {
                        AndExp(part1.toExpression(tokens[0]), part1.toExpression(tokens[2]))
                    }
                }
            }

            2 -> {
                NotExp(part1.toExpression(tokens[1]))
            }

            else -> {
                part1.toExpression(tokens[0])
            }
        }
    }

    val part1Result = part1["a"]?.value ?: 0

    part1.forEach { (key, node) ->
        node.clear()

        if (key == "b") {
            node.expression = ConstantExp(part1Result)
        }
    }

    println(part1Result)
    println(part1["a"]?.value)
}

private class Node {
    private var _value: Int? = null

    val value: Int
        get() {
            if (_value == null) {
                _value = expression?.evaluate()
            }

            return _value!!
        }

    fun clear() {
        _value = null
    }

    var expression: IExpression? = null
}

private sealed interface IExpression {
    fun evaluate(): Int
}

private fun MutableMap<String, Node>.toExpression(key: String): IExpression {
    val const = key.toIntOrNull()

    return if (const != null) {
        ConstantExp(const)
    } else {
        VariableExp(computeIfAbsent(key) { Node() })
    }
}

private class AndExp(private val param1: IExpression, private val param2: IExpression) : IExpression {
    override fun evaluate(): Int {
        return param1.evaluate() and param2.evaluate()
    }
}

private class OrExp(private val param1: IExpression, private val param2: IExpression) : IExpression {
    override fun evaluate(): Int {
        return param1.evaluate() or param2.evaluate()
    }
}

private class ShiftLExp(private val param: IExpression, private val digit: IExpression) : IExpression {
    override fun evaluate(): Int {
        return param.evaluate() shl digit.evaluate()
    }
}

private class ShiftRExp(private val param: IExpression, private val digit: IExpression) : IExpression {
    override fun evaluate(): Int {
        return param.evaluate() ushr digit.evaluate()
    }
}

private class NotExp(private val param: IExpression) : IExpression {
    override fun evaluate(): Int {
        return param.evaluate().inv()
    }
}

private class VariableExp(private val param: Node) : IExpression {
    override fun evaluate(): Int {
        return param.value
    }
}

private class ConstantExp(private val value: Int) : IExpression {
    override fun evaluate(): Int {
        return value
    }
}
