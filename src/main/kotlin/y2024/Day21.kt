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

        val dp = Array(26) { index ->
            //A, ^, >, v, <
            Array(5) {
                LongArray(5) { 1L - index.sign }
            }
        }

        for (i in 1 until dp.size) {
            val lastDp = dp[i - 1]
            val curDp = dp[i]

            for (from in 0 until 5) {
                for (to in 0 until 5) {
                    route(robotPad[from], robotPad[to], 1 to 0).reduce { last, cur ->
                        curDp[from][to] += lastDp[last.operatorIndex()][cur.operatorIndex()]
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

        fun process(str: String, intermediateRobotCount: Int): Long {
            var result = 0L

            str.fold('A') { cur, target ->
                route(doorPad[cur] ?: (0 to 0), doorPad[target] ?: (0 to 0), 0 to 0).reduce { last, cur ->
                    result += dp[intermediateRobotCount][last.operatorIndex()][cur.operatorIndex()]
                    cur
                }

                target
            }

            return result * str.filter { it in '0'..'9' }.toCharArray().concatToString().toInt()
        }

        input.forEach { str ->
            val strNum = str.filter { it in '0'..'9' }.toCharArray().concatToString().toInt()

            str.fold('A') { cur, target ->
                route(doorPad[cur] ?: (0 to 0), doorPad[target] ?: (0 to 0), 0 to 0).reduce { last, cur ->
                    part1Result += dp[2][last.operatorIndex()][cur.operatorIndex()] * strNum
                    part2Result += dp[25][last.operatorIndex()][cur.operatorIndex()] * strNum
                    cur
                }

                target
            }
        }
    }
}
