package y2024

import util.expect
import util.input
import kotlin.math.sign

fun main() {
    expect(0L, 0L) {
        fun Char.operatorIndex(): Int = when (this) {
            '^' -> 1
            '>' -> 2
            'v' -> 3
            '<' -> 4
            else -> 0
        }

        //Movement Priority: <^v> (Only if possible)
        fun route(from: Pair<Int, Int>, to: Pair<Int, Int>, forbiddenNode: Pair<Int, Int>): String {
            val (curR, curC) = from
            val (targetR, targetC) = to

            val deltaR = targetR - curR
            val deltaC = targetC - curC

            return buildString {
                append("A")

                val actions = arrayListOf<String>()
                var reverse = false
                if (deltaC < 0) {
                    if (curR == forbiddenNode.first && curC + deltaC == forbiddenNode.second) {
                        reverse = true
                    }
                    actions.add("<".repeat(-deltaC))
                }

                if (deltaR < 0) {
                    if (actions.isEmpty() && curR + deltaR == forbiddenNode.first && curC == forbiddenNode.second) {
                        reverse = true
                    }

                    actions.add("v".repeat(-deltaR))
                }

                if (deltaR > 0) {
                    if (actions.isEmpty() && curR + deltaR == forbiddenNode.first && curC == forbiddenNode.second) {
                        reverse = true
                    }

                    actions.add("^".repeat(deltaR))
                }

                if (deltaC > 0) {
                    actions.add(">".repeat(deltaC))
                }

                if (reverse) {
                    actions.reverse()
                }

                actions.forEach { append(it) }
                append("A")
            }
        }

        val robotPad = arrayOf(
            1 to 2,
            1 to 1,
            0 to 2,
            0 to 1,
            0 to 0,
        )

        val dp =            //A, ^, >, v, <
            Array(5) {
                Array(5) {
                    LongArray(26) { 1L - it.sign }
                }
            }

        for (i in 1 until 26) {
            repeat(5) { from ->
                repeat(5) { to ->
                    route(robotPad[from], robotPad[to], 1 to 0).reduce { last, cur ->
                        dp[from][to][i] += dp[last.operatorIndex()][cur.operatorIndex()][i - 1]
                        cur
                    }
                }
            }
        }

        val doorPad = hashMapOf(
            '0' to (0 to 1),
            'A' to (0 to 2),
            '1' to (1 to 0),
            '2' to (1 to 1),
            '3' to (1 to 2),
            '4' to (2 to 0),
            '5' to (2 to 1),
            '6' to (2 to 2),
            '7' to (3 to 0),
            '8' to (3 to 1),
            '9' to (3 to 2),
        )

        input.forEach {
            val strNum = it.filter { it in '0'..'9' }.toCharArray().concatToString().toInt()

            it.fold('A') { last, cur ->
                route(doorPad[last] ?: (0 to 0), doorPad[cur] ?: (0 to 0), 0 to 0).reduce { lastMovement, curMovement ->
                    dp[lastMovement.operatorIndex()][curMovement.operatorIndex()].also {
                        part1Result += it[2] * strNum
                        part2Result += it[25] * strNum
                    }
                    curMovement
                }

                cur
            }
        }
    }
}
