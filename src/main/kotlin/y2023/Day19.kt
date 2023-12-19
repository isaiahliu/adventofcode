package y2023

import util.expectLong
import util.input

fun main() {
    expectLong {
        abstract class AbstractExpression {
            abstract fun evaluate(testCase: Map<String, Int>): Boolean

            abstract fun countCombinations(testCase: Map<String, IntRange>): Long
        }

        val accept = object : AbstractExpression() {
            override fun evaluate(testCase: Map<String, Int>): Boolean {
                return true
            }

            override fun countCombinations(testCase: Map<String, IntRange>): Long {
                return testCase.values.fold(1L) { a, b ->
                    a * (b.last - b.first + 1)
                }
            }
        }
        val reject = object : AbstractExpression() {
            override fun evaluate(testCase: Map<String, Int>): Boolean {
                return false
            }

            override fun countCombinations(testCase: Map<String, IntRange>): Long {
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
                return testCase[checkProperty]?.takeIf {
                    if (operator == '<') {
                        it < value
                    } else {
                        it > value
                    }
                }?.let {
                    acceptExp.evaluate(testCase)
                } ?: rejectExp.evaluate(testCase)
            }

            override fun countCombinations(testCase: Map<String, IntRange>): Long {
                val range = testCase[checkProperty] ?: return 0L
                val min = range.first
                val max = range.last

                var result = 0L
                if (operator == '<') {
                    if (min < value) {
                        result += acceptExp.countCombinations(testCase + (checkProperty to (min until value)))
                    }

                    if (max >= value) {
                        result += rejectExp.countCombinations(testCase + (checkProperty to (value..max)))
                    }
                } else {
                    if (max > value) {
                        result += acceptExp.countCombinations(testCase + (checkProperty to (value + 1..max)))
                    }

                    if (min <= value) {
                        result += rejectExp.countCombinations(testCase + (checkProperty to (min..value)))
                    }
                }
                return result
            }
        }

        val map = hashMapOf(
            "A" to accept,
            "R" to reject
        )

        class NamedExp(val name: String) : AbstractExpression() {
            override fun evaluate(testCase: Map<String, Int>): Boolean {
                return map[name]?.evaluate(testCase) ?: false
            }

            override fun countCombinations(testCase: Map<String, IntRange>): Long {
                return map[name]?.countCombinations(testCase) ?: 0L
            }
        }

        val expRegex = "(\\w+)([<>])(\\d+):(\\w+),(.*)".toRegex()
        fun parseExp(detail: String): AbstractExpression {
            return expRegex.matchEntire(detail)?.groupValues?.drop(1)?.let { (prop, op, v, a, r) ->
                Exp(prop, op[0], v.toInt(), parseExp(a), parseExp(r))
            } ?: NamedExp(detail)
        }

        val criteriaRegex = "(\\w+)\\{(.*)}".toRegex()
        val testcaseRegex = "\\{(.*)}".toRegex()
        input.forEach {
            criteriaRegex.matchEntire(it)?.groupValues?.drop(1)?.also { (name, detail) ->
                map[name] = parseExp(detail)
            } ?: testcaseRegex.matchEntire(it)?.groupValues?.drop(1)?.also { (detail) ->
                val testcase = detail.split(",").associate {
                    it.split("=").let { (key, value) -> key to value.toInt() }
                }

                map["in"]?.evaluate(testcase)?.takeIf { it }?.also {
                    part1Result += testcase.values.sum()
                }
            }
        }

        val initRange = 1..4000
        map["in"]?.countCombinations(
            mapOf(
                "x" to initRange,
                "m" to initRange,
                "a" to initRange,
                "s" to initRange
            )
        )?.also {
            part2Result = it
        }
    }
}