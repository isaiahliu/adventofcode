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

        //A, ^, >, v, <
        val robotMovement = arrayOf(
            arrayOf("", "<", "v", "<v", "v<<"),
            arrayOf(">", "", "v>", "v", "v<"),
            arrayOf("^", "<^", "", "<", "<<"),
            arrayOf("^>", "^", ">", "", "<"),
            arrayOf(">>^", ">^", ">>", ">", ""),
        )

        val dp = Array(26) { index ->
            Array(5) {
                LongArray(5) { 1L - index.sign }
            }
        }

        for (i in 1 until dp.size) {
            val lastDp = dp[i - 1]
            val curDp = dp[i]

            for (from in 0 until 5) {
                for (to in 0 until 5) {
                    "A${robotMovement[from][to]}A".reduce { last, cur ->
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
                val (curR, curC) = doorPad[cur] ?: (0 to 0)
                val (targetR, targetC) = doorPad[target] ?: (0 to 0)

                val deltaR = targetR - curR
                val deltaC = targetC - curC

                buildString {
                    append("A")

                    val actions = arrayListOf<String>()
                    var reverse = false
                    if (deltaC < 0) {
                        actions.add("<".repeat(-deltaC))

                        if (curR == 0 && curC + deltaC == 0) {
                            reverse = true
                        }
                    }

                    if (deltaR < 0) {
                        actions.add("v".repeat(-deltaR))

                        if (curR + deltaR == 0 && curC == 0) {
                            reverse = true
                        }
                    }

                    if (deltaR > 0) {
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
                }.reduce { last, cur ->
                    result += dp[intermediateRobotCount][last.operatorIndex()][cur.operatorIndex()]
                    cur
                }

                target
            }

            return result * str.filter { it in '0'..'9' }.toCharArray().concatToString().toInt()
        }

        input.forEach { str ->
            part1Result += process(str, 2)
            part2Result += process(str, 25)
        }
    }
}
