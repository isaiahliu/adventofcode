package y2024

import util.expect
import util.input

fun main() {
    expect(0L, "") {
        var x = 0L
        var y = 0L

        val regex = "(.*) (.*) (.*) -> (.*)".toRegex()

        abstract class AbstractGate(val name: String) {
            abstract val value: Int

            abstract fun asString(force: Boolean = false): String
        }

        val gates = hashMapOf<String, AbstractGate>()
        val replacement = hashMapOf<String, String>()

        class ExpGate(name: String, var op: String, var left: String, var right: String) : AbstractGate(name) {
            private var _value: Int? = null

            override val value: Int
                get() {
                    if (_value == null) {
                        _value = when (op) {
                            "AND" -> (gates[left]?.value ?: 0) and (gates[right]?.value ?: 0)
                            "OR" -> (gates[left]?.value ?: 0) or (gates[right]?.value ?: 0)
                            "XOR" -> (gates[left]?.value ?: 0) xor (gates[right]?.value ?: 0)
                            else -> throw Exception("Error")
                        }

                        if (name[0] == 'z') {
                            part1Result += (_value?.toLong() ?: 0L) shl name.drop(1).toInt()
                        }
                    }
                    return _value ?: 0
                }

            private var _asString: String? = null
            override fun asString(force: Boolean): String {
                if (force) {
                    _asString = arrayOf(gates[replacement[left] ?: left]?.asString(force), gates[replacement[right] ?: right]?.asString(force)).sortedBy { it }.let {
                        "(${it[0]} ${op} ${it[1]})"
                    }
                }

                return _asString.orEmpty()
            }
        }

        class LitGate(name: String, override val value: Int) : AbstractGate(name) {
            init {
                if (name[0] == 'x') {
                    x += value.toLong() shl name.drop(1).toInt()
                } else {
                    y += value.toLong() shl name.drop(1).toInt()
                }
            }

            override fun asString(force: Boolean): String {
                return name
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

        gates.values.forEach { it.value }

        val indices = Array(64) {
            it.toString().padStart(2, '0')
        }
        val dp = Array(indices.size) { arrayOf("(x${indices[it]} XOR y${indices[it]})", "(x${indices[it]} AND y${indices[it]})") }

        for (i in 1 until dp.size) {
            dp[i][0] = "(${dp[i - 1][1]} XOR ${dp[i][0]})"
            dp[i][1] = "((${dp[i - 1][1]} AND (x${indices[i]} XOR y${indices[i]})) OR ${dp[i][1]})"
        }


        next@ while (true) {
            val strMap = gates.values.associateBy { it.asString(true) }

            var index = 0

            while (replacement.size < 8) {
                val gateName = "z${indices[index]}".let { replacement[it] ?: it }
                val gate = gates[gateName] as? ExpGate ?: break@next
                if (gate.asString() != dp[index][0]) {
                    val anotherGate = strMap[dp[index][0]]
                    if (anotherGate != null) {
                        part2Result += gateName
                        part2Result += anotherGate.name

                        replacement[gate.name] = anotherGate.name
                        replacement[anotherGate.name] = gate.name
                        continue@next
                    } else if (gate.op == "XOR") {
                        val left = gate.left
                        val right = gate.right

                        val validPart = hashSetOf(dp[index - 1][1], "(x${indices[index]} XOR y${indices[index]})")

                        when {
                            validPart.remove(gates[left]?.asString()) -> {
                                val r = strMap[validPart.first()] ?: throw Exception("Error")
                                part2Result += r.name
                                part2Result += right

                                replacement[r.name] = right
                                replacement[right] = r.name

                                continue@next
                            }

                            validPart.remove(gates[right]?.asString()) -> {
                                val r = strMap[validPart.first()] ?: throw Exception("Error")
                                part2Result += r.name
                                part2Result += left

                                replacement[r.name] = left
                                replacement[left] = r.name

                                continue@next
                            }

                            else -> {
                                throw Exception("Error")
                            }
                        }
                    } else {
                        throw Exception("Error")
                    }
                }

                index++
            }

            break
        }

        part2Result = replacement.keys.sorted().joinToString(",")
    }
}
