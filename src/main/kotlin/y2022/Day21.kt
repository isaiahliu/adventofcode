package y2022

import util.input

fun main() {
    val monkeys = hashMapOf<String, IDay21Monkey>()

    input.map { it.split(" ") }.forEach {
        val left = it[1]
        val right = it.getOrNull(3).orEmpty()

        var calculator: ICalculator? = null

        when (it.getOrNull(2)) {
            "+" -> {
                calculator = Plus
            }

            "-" -> {
                calculator = Minus
            }

            "*" -> {
                calculator = Multiply
            }

            "/" -> {
                calculator = Divide
            }
        }

        monkeys[it[0].trimEnd(':')] = if (calculator == null) {
            Constant(left.toLong())
        } else {
            Day21Monkey(monkeys, left, right, calculator)
        }
    }

    println(monkeys["root"]?.value)


    monkeys.values.forEach { it.clear() }
    monkeys["humn"] = Humn()

    monkeys["root"]?.let { it as? Day21Monkey }?.also {
        if (it.left.value == null) {
            it.left.forceValue(it.right.value!!)
        } else if (it.right.value == null) {
            it.right.forceValue(it.left.value!!)
        }
    }

    println(monkeys["humn"]?.value)
}

private interface IDay21Monkey {
    val value: Long?

    fun clear()

    fun forceValue(v: Long)
}

private class Constant(override val value: Long?) : IDay21Monkey {
    override fun clear() {
    }

    override fun forceValue(v: Long) {
    }
}

private class Humn : IDay21Monkey {
    override var value: Long? = null
    override fun clear() {

    }

    override fun forceValue(v: Long) {
        value = v
    }
}

private class Day21Monkey(val monkeys: Map<String, IDay21Monkey>, val leftKey: String, val rightKey: String, val calculator: ICalculator) : IDay21Monkey {
    val left: IDay21Monkey get() = monkeys[leftKey]!!
    val right: IDay21Monkey get() = monkeys[rightKey]!!

    private var _value: Long? = null

    override val value: Long?
        get() {
            val leftValue = left.value
            val rightValue = right.value

            if (_value == null && leftValue != null && rightValue != null) {
                _value = calculator.calculateResult(leftValue, rightValue)
            }

            return _value
        }

    override fun clear() {
        _value = null
    }

    override fun forceValue(v: Long) {
        _value = v

        if (left.value == null) {
            left.forceValue(calculator.calculateLeft(v, right.value!!))
        } else if (right.value == null) {
            right.forceValue(calculator.calculateRight(v, left.value!!))
        }
    }
}

private interface ICalculator {
    fun calculateResult(left: Long, right: Long): Long
    fun calculateLeft(result: Long, right: Long): Long
    fun calculateRight(result: Long, left: Long): Long
}

private object Plus : ICalculator {
    override fun calculateResult(left: Long, right: Long): Long {
        return left + right
    }

    override fun calculateLeft(result: Long, right: Long): Long {
        return result - right
    }

    override fun calculateRight(result: Long, left: Long): Long {
        return result - left
    }
}

private object Minus : ICalculator {
    override fun calculateResult(left: Long, right: Long): Long {
        return left - right
    }

    override fun calculateLeft(result: Long, right: Long): Long {
        return result + right
    }

    override fun calculateRight(result: Long, left: Long): Long {
        return left - result
    }
}

private object Multiply : ICalculator {
    override fun calculateResult(left: Long, right: Long): Long {
        return left * right
    }

    override fun calculateLeft(result: Long, right: Long): Long {
        return result / right
    }

    override fun calculateRight(result: Long, left: Long): Long {
        return result / left
    }
}

private object Divide : ICalculator {
    override fun calculateResult(left: Long, right: Long): Long {
        return left / right
    }

    override fun calculateLeft(result: Long, right: Long): Long {
        return result * right
    }

    override fun calculateRight(result: Long, left: Long): Long {
        return left / result
    }
}
