fun main() {
    var part1Sum = 0
    var part2Sum = 0
    input.map { it.split(" ") }.filter { it.size == 2 }.map {
        Shape.ofCode(it[0]) to it[1]
    }.forEach { pair ->
        val part1Shape = Shape.ofCode(pair.second)
        part1Sum += part1Shape.score
        part1Sum += part1Shape.battle(pair.first).score

        val part2Result = BattleResult.ofCode(pair.second)
        part2Sum += part2Result.findShapeForTarget(pair.first).score
        part2Sum += part2Result.score
    }

    println(part1Sum)
    println(part2Sum)
}

enum class BattleResult(val score: Int, val code: String) {
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

enum class Shape(val score: Int, vararg val codes: String) {
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

