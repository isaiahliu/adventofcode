package y2024

import util.expect
import util.input

fun main() {
    expect(0L, "") {
        val regex = "(.*) (.*) (.*) -> (.*)".toRegex()

        fun Int.twoDigitStr(): String {
            return this.toString().padStart(2, '0')
        }

        abstract class AbstractGate(val name: String) {
            abstract val value: Int

            abstract fun asString(force: Boolean = false): String
        }

        class LitGate(name: String, override val value: Int) : AbstractGate(name) {
            override fun asString(force: Boolean): String {
                return name
            }
        }

        val gates = hashMapOf<String, AbstractGate>()
        val replacement = hashMapOf<String, String>()

        class ExpGate(name: String, var op: String, var left: String, var right: String) : AbstractGate(name) {
            override val value: Int
                get() {
                    val leftValue = gates[left]?.value ?: throw Exception("Error")
                    val rightValue = gates[right]?.value ?: throw Exception("Error")

                    return when (op) {
                        "AND" -> leftValue and rightValue
                        "OR" -> leftValue or rightValue
                        "XOR" -> leftValue xor rightValue
                        else -> throw Exception("Error")
                    }
                }

            private var _asString: String? = null
            override fun asString(force: Boolean): String {
                if (force) {
                    _asString = arrayOf(gates[replacement[left] ?: left]?.asString(force), gates[replacement[right] ?: right]?.asString(force)).sortedBy { it }.let {
                        "(${it[0]} $op ${it[1]})"
                    }
                }

                return _asString.orEmpty()
            }
        }

        var readingExp = false

        input.forEach {
            when {
                it.isEmpty() -> readingExp = true
                !readingExp -> {
                    it.split(": ").also { (gate, value) ->
                        gates[gate] = LitGate(gate, value.toInt())
                    }
                }

                else -> {
                    regex.matchEntire(it)?.groupValues?.drop(1)?.also { (left, op, right, output) ->
                        gates[output] = ExpGate(output, op, left, right)
                    }
                }
            }
        }

        var zSize = 0
        while (true) {
            gates["z${zSize.twoDigitStr()}"]?.also {
                part1Result += (1L shl zSize++) * it.value
            } ?: break
        }

        val dp = Array(zSize) {
            it.twoDigitStr().let {
                arrayOf("(x$it XOR y$it)", "(x$it AND y$it)")
            }
        }

        for (i in 1 until dp.size) {
            dp[i][0] = "(${dp[i - 1][1]} XOR ${dp[i][0]})"

            val d = i.twoDigitStr()
            dp[i][1] = "((${dp[i - 1][1]} AND (x$d XOR y$d)) OR ${dp[i][1]})"
        }

        while (replacement.size < 8) {
            val strMap = gates.values.associateBy { it.asString(true) }

            for (index in 0 until zSize) {
                val zGateName = "z${index.twoDigitStr()}".let { replacement[it] ?: it }
                val gate = gates[zGateName]?.let { it as? ExpGate }?.takeIf { it.asString() != dp[index][0] } ?: continue

                val targetGate = strMap[dp[index][0]]

                when {
                    targetGate != null -> {
                        replacement[gate.name] = targetGate.name
                        replacement[targetGate.name] = gate.name
                    }

                    gate.op == "XOR" -> {
                        val validParts = hashSetOf(dp[index - 1][1], "(x${index.twoDigitStr()} XOR y${index.twoDigitStr()})")

                        when {
                            validParts.remove(gates[gate.left]?.asString()) -> {
                                strMap[validParts.first()]?.also {
                                    replacement[it.name] = gate.right
                                    replacement[gate.right] = it.name
                                } ?: throw Exception("Error")
                            }

                            validParts.remove(gates[gate.right]?.asString()) -> {
                                strMap[validParts.first()]?.also {
                                    replacement[it.name] = gate.left
                                    replacement[gate.left] = it.name
                                } ?: throw Exception("Error")
                            }

                            else -> {
                                throw Exception("Error")
                            }
                        }
                    }

                    else -> {
                        throw Exception("Error")
                    }
                }
                
                break
            }
        }

        part2Result = replacement.keys.sorted().joinToString(",")
    }
}
