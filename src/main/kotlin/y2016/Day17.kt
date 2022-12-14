package y2016

import input
import md5

fun main() {
    val line = input.first()
//    val line = "ihgpwlah"

    fun process(): Pair<String, String> {
        val current = arrayListOf(Stat(0, 0, ""))
        var max = ""
        var min = ""

        while (current.isNotEmpty()) {
            val tasks = current.toList()

            current.clear()

            for (task in tasks) {
                if (task.x == 3 && task.y == 3) {
                    if (min.isEmpty()) {
                        min = task.step
                    }
                    max = task.step

                    continue
                }

                val (up, down, left, right) = (line + task.step).md5().take(4).map {
                    it != 'a' && it !in '0'..'9'
                }

                if (up && task.y > 0) {
                    current += task.copy(y = task.y - 1, step = task.step + "U")
                }

                if (down && task.y < 3) {
                    current += task.copy(y = task.y + 1, step = task.step + "D")
                }

                if (left && task.x > 0) {
                    current += task.copy(x = task.x - 1, step = task.step + "L")
                }

                if (right && task.x < 3) {
                    current += task.copy(x = task.x + 1, step = task.step + "R")
                }
            }
        }

        return min to max
    }

    val (min, max) = process()

    println(min)
    println(max.length)
}

private data class Stat(val x: Int, val y: Int, val step: String)