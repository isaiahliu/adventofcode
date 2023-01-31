package y2021

import util.input

fun main() {
    val map = input.map { it.map { it - '0' }.toIntArray() }.toTypedArray()

    fun process(times: Int): Int {
        val fullMap = Array(map.size * times) { r ->
            IntArray(map[r % map.size].size * times) { c ->
                val addRisk = r / map.size + c / map[r % map.size].size

                map[r % map.size][c % map[r % map.size].size].let {
                    (it - 1 + addRisk) % 9 + 1
                }
            }
        }
        val risks = Array(fullMap.size) { IntArray(fullMap[it].size) { Int.MAX_VALUE } }

        risks[0][0] = 0

        val tasks = hashSetOf(0 to 0)

        while (tasks.isNotEmpty()) {
            val current = tasks.toList()
            tasks.clear()

            for ((r, c) in current) {
                val risk = risks[r][c]
                arrayOf(r - 1 to c, r + 1 to c, r to c - 1, r to c + 1).filter {
                    fullMap.getOrNull(it.first)?.getOrNull(it.second) != null
                }.filter {
                    risk + fullMap[it.first][it.second] < risks[it.first][it.second]
                }.forEach {
                    risks[it.first][it.second] = risk + fullMap[it.first][it.second]
                    tasks += it
                }
            }
        }

        return risks.last().last()
    }

    println(process(1))
    println(process(5))
}