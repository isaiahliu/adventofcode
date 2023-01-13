package y2020

import util.input

fun main() {
    class Rule(val id: Int) {
        val subRules = arrayListOf<List<Rule>>()

        var fixedChar: Char? = null

        fun match(charArray: String, index: Int): Int {
            return if (fixedChar != null) {
                val match = charArray.getOrNull(index) == fixedChar
                if (match) {
                    println("--match ${index}-${fixedChar}")
                } else {
                    println("mismatch ${index}-${fixedChar}")
                }
                if (match) 1 else 0
            } else {
                println("processing ${id}")
                subRules.maxOf {
                    var length = 0
                    for (rule in it) {
                        val matchLength = rule.match(charArray, index + length)
                        if (matchLength == 0) {
                            return@maxOf 0
                        }

                        length += matchLength
                    }

                    return@maxOf length
                }
            }
        }
    }

    val rulesMap = hashMapOf<Int, Rule>()
    var part = 0
    val lines = arrayListOf<String>()
    input.forEach {
        if (it.isBlank()) {
            part++
            return@forEach
        }

        if (part == 0) {
            val (ruleId, detail) = it.split(": ")

            val rule = rulesMap.computeIfAbsent(ruleId.toInt()) { Rule(it) }
            if (detail[0] == '"') {
                rule.fixedChar = detail[1]
            } else {
                detail.split("|").forEach {
                    rule.subRules.add(it.trim().split(" ").map { rulesMap.computeIfAbsent(it.toInt()) { Rule(it) } })
                }
            }
        } else {
            lines += it
        }
    }

    val rule0 = rulesMap[0]!!

//    println(lines.count { rule0.match(it, 0) == it.length })

    rulesMap[8]?.also {
        it.fixedChar = null
        it.subRules.clear()
        it.subRules.add(listOf(rulesMap[42]!!))
        it.subRules.add(listOf(rulesMap[42]!!, rulesMap[8]!!))
    }

    rulesMap[11]?.also {
        it.fixedChar = null
        it.subRules.clear()
        it.subRules.add(listOf(rulesMap[42]!!, rulesMap[31]!!))
        it.subRules.add(listOf(rulesMap[42]!!, rulesMap[11]!!, rulesMap[31]!!))
    }

    println(lines.first().let { rule0.match(it, 0) })
}