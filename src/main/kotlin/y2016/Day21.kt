package y2016

import input

fun main() {
    fun CharArray.process(instruction: List<String>, reverse: Boolean = false) {
        when (instruction[0]) {
            "swap" -> {
                when (instruction[1]) {
                    "position" -> {
                        val x = instruction[2].toInt()
                        val y = instruction[5].toInt()

                        val t = this[x]
                        this[x] = this[y]
                        this[y] = t
                    }

                    "letter" -> {
                        val x = instruction[2][0]
                        val y = instruction[5][0]
                        for (index in this.indices) {
                            when (this[index]) {
                                x -> this[index] = y
                                y -> this[index] = x
                            }
                        }
                    }
                }
            }

            "rotate" -> {
                val dupString = String(this)
                val rotateTimes = when (instruction[1]) {
                    "left" -> {
                        instruction[2].toInt() * if (reverse) {
                            1
                        } else {
                            -1
                        }
                    }

                    "right" -> {
                        instruction[2].toInt() * if (reverse) {
                            -1
                        } else {
                            1
                        }
                    }

                    else -> {
                        if (reverse) {
                            val pos = this.indexOfFirst { c -> c == instruction[6][0] }

                            0 - if (pos % 2 == 0) {
                                ((pos + this.size - 1) % this.size) / 2 + 6
                            } else {
                                pos / 2 + 1
                            }
                        } else {
                            (this.indexOfFirst { c -> c == instruction[6][0] }.let {
                                it + if (it < 4) {
                                    0
                                } else {
                                    1
                                }
                            } + 1) % this.size
                        }
                    }
                }

                for (i in this.indices) {
                    this[i] = dupString[(i - rotateTimes + this.size) % this.size]
                }
            }

            "reverse" -> {
                val x = instruction[2].toInt()
                val y = instruction[4].toInt()

                val dupString = String(this)

                for (i in x..y) {
                    this[i] = dupString[y + x - i]
                }
            }

            "move" -> {
                val dupString = String(this)
                var x = instruction[2].toInt()
                var y = instruction[5].toInt()

                if (reverse) {
                    val t = x
                    x = y
                    y = t
                }

                val xLetter = this[x]
                when {
                    x > y -> {
                        for (i in y until x) {
                            this[i + 1] = dupString[i]
                        }

                        this[y] = xLetter
                    }

                    x < y -> {
                        for (i in x until y) {
                            this[i] = dupString[i + 1]
                        }

                        this[y] = xLetter
                    }
                }
            }
        }
    }

    val password = "abcdefgh".toCharArray()

    input.map { it.split(" ") }.forEach {
        password.process(it)
    }
    println(String(password))

    val scrambledPassword = "fbgdceah".toCharArray()
    input.reversed().map { it.split(" ") }.forEach {
        scrambledPassword.process(it, true)
    }
    println(String(scrambledPassword))
}