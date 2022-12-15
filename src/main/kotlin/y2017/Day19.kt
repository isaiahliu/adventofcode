package y2017

import util.input

fun main() {
    val map = input.map { it.toCharArray() }.toTypedArray()

    //up=0,right=1,down=2,left=3
    var direction = 2

    var column = map.first().indexOfFirst { it != ' ' }
    var row = 0

    val forwardChars = charArrayOf('|', '-')

    var steps = 0

    fun forward() {
        when (direction) {
            0 -> {
                row--
            }

            1 -> {
                column++
            }

            2 -> {
                row++
            }

            3 -> {
                column--
            }
        }

        steps++
    }

    fun turn() {
        var turnRight = true
        when (direction) {
            0 -> {
                val left = map.getOrNull(row)?.getOrNull(column - 1)
                if (left == forwardChars[1] || left in ('A'..'Z')) {
                    turnRight = false
                }
            }

            1 -> {
                val left = map.getOrNull(row - 1)?.getOrNull(column)
                if (left == forwardChars[0] || left in ('A'..'Z')) {
                    turnRight = false
                }
            }

            2 -> {
                val left = map.getOrNull(row)?.getOrNull(column + 1)
                if (left == forwardChars[1] || left in ('A'..'Z')) {
                    turnRight = false
                }
            }

            3 -> {
                val left = map.getOrNull(row + 1)?.getOrNull(column)
                if (left == forwardChars[0] || left in ('A'..'Z')) {
                    turnRight = false
                }
            }
        }

        if (turnRight) {
            direction++
        } else {
            direction--
        }

        direction = direction.mod(4)
    }

    val path = StringBuilder()
    while (true) {
        val char = map.getOrNull(row)?.getOrNull(column)?.takeIf { it != ' ' } ?: break

        when (char) {
            in forwardChars -> {
            }

            '+' -> {
                turn()
            }

            else -> {
                path.append(char)
            }
        }

        forward()
    }
    println(path.toString())
    println(steps)
}