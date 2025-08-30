package y2016

import util.expect
import util.input

fun main() {
    expect(0, 1) {
        val bots = hashMapOf<Int, Pair<MutableList<Int>, Pair<(Int) -> Unit, (Int) -> Unit>>>()
        val inputs = arrayListOf<Pair<Int, Int>>()
        fun writeToOutput(index: Int, value: Int) {
            if (index in 0..2) {
                part2Result *= value
            }
        }

        fun writeToBot(index: Int, value: Int) {
            bots[index]?.also { (values, writer) ->
                values.add(value)

                if (values.size == 2) {
                    values.sorted().also { (low, high) ->
                        if (low == 17 && high == 61) {
                            part1Result = index
                        }

                        writer.first(low)
                        writer.second(high)
                    }
                }
            }
        }

        input.map { it.split(" ") }.forEach {
            when (it[0]) {
                "value" -> {
                    inputs += it[5].toInt() to it[1].toInt()
                }

                "bot" -> {
                    val lower = when (it[5]) {
                        "bot" -> { value: Int -> writeToBot(it[6].toInt(), value) }
                        else -> { value: Int -> writeToOutput(it[6].toInt(), value) }
                    }

                    val higher = when (it[10]) {
                        "bot" -> { value: Int -> writeToBot(it[11].toInt(), value) }
                        else -> { value: Int -> writeToOutput(it[11].toInt(), value) }
                    }

                    bots[it[1].toInt()] = mutableListOf<Int>() to (lower to higher)
                }

                else -> {
                    throw Exception("else")
                }
            }
        }
        
        inputs.forEach { (index, value) ->
            writeToBot(index, value)
        }
    }
}