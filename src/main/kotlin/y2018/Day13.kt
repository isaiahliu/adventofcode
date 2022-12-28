package y2018

import util.input

fun main() {
    val UP = 0
    val RIGHT = 1
    val DOWN = 2
    val LEFT = 3

    val ROAD = 1
    val TOP_LEFT_CORNER = 2
    val TOP_RIGHT_CORNER = 3
    val CROSS = 4

    val NEXT_TURN_LEFT = 0
    val NEXT_GO_STRAIGHT = 1
    val NEXT_TURN_RIGHT = 2

    val height = input.size
    val width = input.maxOf { it.length }

    fun process(firstCrash: Boolean): String {
        val carts = arrayListOf<IntArray>()

        val map = Array(height) { rowIndex ->
            IntArray(width) { columnIndex ->
                when (input[rowIndex].getOrElse(columnIndex) { ' ' }) {
                    ' ' -> 0
                    '+' -> CROSS
                    '/' -> TOP_LEFT_CORNER
                    '\\' -> TOP_RIGHT_CORNER
                    '<' -> {
                        carts += intArrayOf(rowIndex, columnIndex, LEFT, NEXT_TURN_LEFT, 1)
                        ROAD
                    }

                    '>' -> {
                        carts += intArrayOf(rowIndex, columnIndex, RIGHT, NEXT_TURN_LEFT, 1)
                        ROAD
                    }

                    '^' -> {
                        carts += intArrayOf(rowIndex, columnIndex, UP, NEXT_TURN_LEFT, 1)
                        ROAD
                    }

                    'v' -> {
                        carts += intArrayOf(rowIndex, columnIndex, DOWN, NEXT_TURN_LEFT, 1)
                        ROAD
                    }

                    else -> ROAD
                }
            }
        }

        var remainingCartCount = carts.size
        while (true) {
            carts.sortedWith(compareBy<IntArray> { it[0] }.thenBy { it[1] }).forEach { cart ->
                if (cart[4] == 0) {
                    return@forEach
                }

                when (cart[2]) {
                    UP -> {
                        cart[0]--
                    }

                    RIGHT -> {
                        cart[1]++
                    }

                    DOWN -> {
                        cart[0]++
                    }

                    LEFT -> {
                        cart[1]--
                    }
                }
                when (map[cart[0]][cart[1]]) {
                    ROAD -> {}
                    TOP_LEFT_CORNER -> {
                        if (cart[2] % 2 == 0) {
                            cart[2] = (cart[2] + 1).mod(4)
                        } else {
                            cart[2] = (cart[2] - 1).mod(4)
                        }
                    }

                    TOP_RIGHT_CORNER -> {
                        if (cart[2] % 2 == 0) {
                            cart[2] = (cart[2] - 1).mod(4)
                        } else {
                            cart[2] = (cart[2] + 1).mod(4)
                        }
                    }

                    CROSS -> {
                        when (cart[3]) {
                            NEXT_TURN_LEFT -> {
                                cart[2] = (cart[2] - 1).mod(4)
                            }

                            NEXT_TURN_RIGHT -> {
                                cart[2] = (cart[2] + 1).mod(4)
                            }

                            NEXT_GO_STRAIGHT -> {}
                        }

                        cart[3] = (cart[3] + 1) % 3
                    }

                    else -> {
                        println("ERROR")
                    }
                }

                val crashedCarts = (carts.filter { it[4] > 0 && it[0] == cart[0] && it[1] == cart[1] })

                if (crashedCarts.size > 1) {
                    if (firstCrash) {
                        return crashedCarts.first().let { "${it[1]},${it[0]}" }
                    } else {
                        crashedCarts.forEach { it[4] = 0 }
                        remainingCartCount -= 2
                    }
                }
            }

            if (!firstCrash && remainingCartCount == 1) {
                return carts.first { it[4] > 0 }.let { "${it[1]},${it[0]}" }
            }
        }
    }

    println(process(true))
    println(process(false))
}