package y2015

import util.input

fun main() {
    val instructions = input.toTypedArray()

    fun execute(registers: IntArray): Int {
        var instIndex = 0
        while (instIndex < instructions.size) {
            val nodes = instructions[instIndex].split(" ")

            var instMovement = 1
            when (nodes[0]) {
                "hlf" -> {
                    registers[nodes[1][0] - 'a'] /= 2
                }

                "tpl" -> {
                    registers[nodes[1][0] - 'a'] *= 3
                }

                "inc" -> {
                    registers[nodes[1][0] - 'a']++
                }

                "jmp" -> {
                    instMovement = nodes[1].toInt()
                }

                "jie" -> {
                    if (registers[nodes[1][0] - 'a'] % 2 == 0) {
                        instMovement = nodes[2].toInt()
                    }
                }

                "jio" -> {
                    if (registers[nodes[1][0] - 'a'] == 1) {
                        instMovement = nodes[2].toInt()
                    }
                }
            }

            instIndex += instMovement
        }

        return registers[1]
    }

    println(execute(intArrayOf(0, 0)))
    println(execute(intArrayOf(1, 0)))

}