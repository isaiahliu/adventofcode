package y2016

import util.expect
import util.input

fun main() {
    expect("", "") {
        abstract class AbstractKeyBoard(protected var x: Int, protected var y: Int) {
            protected abstract fun inBound(): Boolean

            fun move(direction: Char) {
                var deltaX = 0
                var deltaY = 0

                when (direction) {
                    'U' -> deltaY = -1
                    'D' -> deltaY = 1
                    'L' -> deltaX = -1
                    'R' -> deltaX = 1
                }

                x += deltaX
                y += deltaY

                if (!inBound()) {
                    x -= deltaX
                    y -= deltaY
                }
            }

            val password = StringBuilder()
            fun markCode() {
                password.append(code())
            }

            protected abstract fun code(): Char
        }

        class KeyBoard1 : AbstractKeyBoard(2, 2) {
            override fun inBound(): Boolean {
                return x in 0..2 && y in 0..2
            }

            override fun code(): Char {
                return '1' + y * 3 + x
            }
        }

        class KeyBoard2 : AbstractKeyBoard(2, 0) {
            override fun inBound(): Boolean {
                return x - y in -2..2 && x + y in 2..6
            }

            private val map = arrayOf(
                "  1  ",
                " 234 ",
                "56789",
                " ABC ",
                "  D  "
            )

            override fun code(): Char {
                return map[y][x]
            }
        }

        val boards = arrayOf(KeyBoard1(), KeyBoard2())

        input.forEach {
            it.forEach {
                boards.forEach { b ->
                    b.move(it)
                }
            }

            boards.forEach { it.markCode() }
        }
        part1Result = boards[0].password.toString()
        part2Result = boards[1].password.toString()
    }
}