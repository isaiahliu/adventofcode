package y2017

import util.expect
import util.input

fun main() {
    expect("", 0) {
        val children = hashMapOf<String, Set<String>>()
        val weights = hashMapOf<String, Int>()
        val leaves = hashSetOf<String>()
        input.map { it.split(" ") }.forEach {
            val name = it[0]

            weights[name] = it[1].trim('(', ')').toInt()

            children[name] = it.drop(3).map {
                val childName = it.trim(',')
                leaves += childName
                childName
            }.toMutableSet()
        }

        part1Result = (children.keys - leaves).first()

        val sums = hashMapOf<String, Int>()
        fun sumDfs(node: String) {
            sums[node] = (weights[node] ?: 0) + (children[node]?.sumOf {
                sumDfs(it)
                sums[it] ?: 0
            } ?: 0)
        }
        sumDfs(part1Result)

        fun dfs(node: String, diff: Int?): Int? {
            val valueGroup = children[node]?.groupBy { sums[it] ?: 0 }.orEmpty()

            when {
                valueGroup.size == 2 -> {
                    val target = valueGroup.entries.first { it.value.size == 1 }
                    val others = valueGroup.entries.first { it.value.size > 1 }

                    return dfs(target.value.first(), others.key - target.key)
                }

                diff != null -> {
                    return (weights[node] ?: 0) + diff
                }

                else -> {
                    children.forEach {
                        dfs(it.key, null)?.also {
                            return it
                        }
                    }
                }
            }

            return null
        }

        part2Result = dfs(part1Result, null) ?: 0
    }
}