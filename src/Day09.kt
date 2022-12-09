import kotlin.math.absoluteValue

fun main() {
    val rope1 = Array(2) { RopeNode(0, 0) }
    val rope2 = Array(10) { RopeNode(0, 0) }

    input.map { it.split(" ") }.forEach {
        val direction = it[0]

        repeat(it[1].toInt()) {
            rope1[0].move(direction)
            repeat(rope1.size - 1) {
                rope1[it + 1].follow(rope1[it])
            }

            rope2[0].move(direction)
            repeat(rope2.size - 1) {
                rope2[it + 1].follow(rope2[it])
            }
        }
    }

    println(rope1.last().visited.size)
    println(rope2.last().visited.size)
}

private class RopeNode(var x: Int, var y: Int) {
    val visited: MutableSet<String> = hashSetOf()

    init {
        move(x, y)
    }

    fun move(direction: String) {
        when (direction) {
            "R" -> {
                move(toX = x + 1)
            }

            "L" -> {
                move(toX = x - 1)
            }

            "U" -> {
                move(toY = y + 1)
            }

            "D" -> {
                move(toY = y - 1)
            }
        }
    }

    fun move(toX: Int = x, toY: Int = y) {
        x = toX
        y = toY

        visited += "${x}_${y}"
    }

    fun follow(previous: RopeNode) {
        val diffX = previous.x - x
        val diffY = previous.y - y

        var newX = x
        var newY = y
        if (diffX.absoluteValue > 1 || diffY.absoluteValue > 1) {
            if (diffX.absoluteValue > 0) {
                newX += diffX / diffX.absoluteValue
            }
            if (diffY.absoluteValue > 0) {
                newY += diffY / diffY.absoluteValue
            }
        }

        move(newX, newY)
    }
}