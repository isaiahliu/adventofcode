package y2023

import util.expectLong
import util.input

fun main() {
    expectLong {
        abstract class AbstractExpression {
            abstract fun evaluate(testCase: Map<String, Int>): Boolean

            abstract fun findCombinations(testCase: Map<String, IntRange>): Long
        }

        val accept = object : AbstractExpression() {
            override fun evaluate(testCase: Map<String, Int>): Boolean {
                return true
            }

            override fun findCombinations(testCase: Map<String, IntRange>): Long {
                return testCase.values.fold(1L) { a, b ->
                    a * (b.last - b.first + 1)
                }
            }
        }
        val reject = object : AbstractExpression() {
            override fun evaluate(testCase: Map<String, Int>): Boolean {
                return false
            }

            override fun findCombinations(testCase: Map<String, IntRange>): Long {
                return 0L
            }
        }

        class Exp(
            val checkProperty: String,
            val operator: Char,
            val value: Int,
            val acceptExp: AbstractExpression,
            val rejectExp: AbstractExpression
        ) : AbstractExpression() {
            override fun evaluate(testCase: Map<String, Int>): Boolean {
                val propertyValue = testCase[checkProperty] ?: 0

                val result = if (operator == '<') propertyValue < value else propertyValue > value

                return if (result) {
                    acceptExp.evaluate(testCase)
                } else {
                    rejectExp.evaluate(testCase)
                }
            }

            override fun findCombinations(testCase: Map<String, IntRange>): Long {
                val range = testCase[checkProperty] ?: return 0L
                val min = range.first
                val max = range.last

                var result = 0L
                if (operator == '<') {
                    if (min < value) {
                        val case = testCase.toMutableMap()
                        case[checkProperty] = min until value
                        result += acceptExp.findCombinations(case)
                    }

                    if (max >= value) {
                        val case = testCase.toMutableMap()
                        case[checkProperty] = value..max
                        result += rejectExp.findCombinations(case)
                    }
                } else {
                    if (max > value) {
                        val case = testCase.toMutableMap()
                        case[checkProperty] = value + 1..max
                        result += acceptExp.findCombinations(case)
                    }

                    if (min <= value) {
                        val case = testCase.toMutableMap()
                        case[checkProperty] = min..value
                        result += rejectExp.findCombinations(case)
                    }
                }
                return result
            }
        }

        val map = hashMapOf<String, AbstractExpression>()

        class ExpProxy(val name: String) : AbstractExpression() {
            override fun evaluate(testCase: Map<String, Int>): Boolean {
                return map[name]?.evaluate(testCase) ?: false
            }

            override fun findCombinations(testCase: Map<String, IntRange>): Long {
                return map[name]?.findCombinations(testCase) ?: 0L
            }
        }

        val expRegex = "(\\w+)([<>])(\\d+):(\\w+),(.*)".toRegex()
        fun parse(detail: String): AbstractExpression {
            val match = expRegex.matchEntire(detail)?.groupValues?.drop(1)

            return when {
                match != null -> {
                    val (prop, op, v, a, r) = match

                    Exp(prop, op[0], v.toInt(), parse(a), parse(r))
                }

                detail == "A" -> {
                    accept
                }

                detail == "R" -> {
                    reject
                }

                else -> {
                    ExpProxy(detail)
                }
            }
        }

        val criteriaRegex = "(\\w+)\\{(.*)\\}".toRegex()
        val testcaseRegex = "\\{(.*)\\}".toRegex()
        input.forEach {
            criteriaRegex.matchEntire(it)?.groupValues?.drop(1)?.also { (name, detail) ->
                map[name] = parse(detail)
            } ?: testcaseRegex.matchEntire(it)?.groupValues?.drop(1)?.also { (detail) ->
                val testcase = detail.split(",").map {
                    it.split("=").let { (key, value) -> key to value.toInt() }
                }.toMap()

                map["in"]?.evaluate(testcase)?.takeIf { it }?.also {
                    part1Result += testcase.values.sum()
                }
            }
        }

        map["in"]?.findCombinations(mapOf("x" to 1..4000, "m" to 1..4000, "a" to 1..4000, "s" to 1..4000))?.also {
            part2Result = it
        }
    }
}