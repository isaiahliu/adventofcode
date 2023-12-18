package y2022

import util.expectInt
import util.input

fun main() {
    expectInt {
        input.map { it.split(" ") }.filter { it.size == 2 }.map {
            Shape.ofCode(it[0]) to it[1]
        }.forEach { pair ->
            val r1 = Shape.ofCode(pair.second)
            part1Result += r1.score
            part1Result += r1.battle(pair.first).score

            val r2 = BattleResult.ofCode(pair.second)
            part2Result += r2.findShapeForTarget(pair.first).score
            part2Result += r2.score
        }
    }
}

private enum class BattleResult(val score: Int, val code: String) {
    LOSE(0, "X"), DRAW(3, "Y"), WIN(6, "Z");

    companion object {
        fun ofCode(code: String): BattleResult = BattleResult.values().first { code == it.code }
    }

    fun findShapeForTarget(shape: Shape): Shape {
        return when (this) {
            LOSE -> Shape.values()[(shape.ordinal - 1).mod(3)]
            DRAW -> shape
            WIN -> Shape.values()[(shape.ordinal + 1).mod(3)]
        }
    }
}

private enum class Shape(val score: Int, vararg val codes: String) {
    ROCK(1, "A", "X"), PAPER(2, "B", "Y"), SCISSORS(3, "C", "Z");

    companion object {
        fun ofCode(code: String): Shape = Shape.values().first { code in it.codes }
    }

    fun battle(shape: Shape): BattleResult {
        return when ((this.ordinal - shape.ordinal).mod(3)) {
            0 -> BattleResult.DRAW
            1 -> BattleResult.WIN
            else -> BattleResult.LOSE
        }
    }
}

