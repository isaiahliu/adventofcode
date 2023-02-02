package y2021

import util.input
import kotlin.math.absoluteValue

fun main() {
    operator fun LongArray.get(char: String): Long {
        return this[char[0] - 'w']
    }

    operator fun LongArray.set(char: String, value: Long) {
        this[char[0] - 'w'] = value
    }

    val insts = input.map {
        it.split(" ").let { it[0] to it.drop(1) }
    }

    fun process(inputNums: List<Long>): Long {
        val registers = LongArray(4)

        fun String.evaluate(): Long {
            return this.toLongOrNull() ?: registers[this]
        }

        var inputNumIndex = 0

        var instIndex = 0
        while (instIndex < insts.size) {
            val (inst, params) = insts[instIndex]

            when (inst) {
                "inp" -> {
                    registers[params[0]] = inputNums[inputNumIndex++]
                }

                "add" -> {
                    registers[params[0]] += params[1].evaluate()
                }

                "mul" -> {
                    registers[params[0]] *= params[1].evaluate()
                }

                "div" -> {
                    registers[params[0]] /= params[1].evaluate()
                }

                "mod" -> {
                    registers[params[0]] = params[0].evaluate().mod(params[1].evaluate())
                }

                "eql" -> {
                    registers[params[0]] = if (params[0].evaluate() == params[1].evaluate()) 1L else 0L
                }

                else -> throw RuntimeException(inst)
            }

            instIndex++
        }

        return registers.last()
    }


    val params = arrayListOf<LongArray>()
    val t = arrayListOf<Long>()
    input.forEachIndexed { index, s ->
        when (index % 18) {
            5, 4, 15 -> {
                t += s.split(" ").last().toLong()
                if (t.size == 3) {
                    params += t.toLongArray()
                    t.clear()
                }
            }
        }
    }

    var done = false
    val inputs = LongArray(14)
    val results = LongArray(14)
    var index = 0
    loop@ while (index < results.size) {
        if (index < 0) {
            val a = 1
        }
        val (param1, param2, param3) = params[index]
        //println("${param1} ${param2} ${param3}")


        var z = results.getOrNull(index - 1) ?: 0L
        val targetNum = z.mod(26L) + param2

        if (param1 == 26L) {
            if (targetNum in 1..9) {
                inputs[index] = -targetNum
            } else {
                while (true) {
                    if (index < 0) {
                        val b = 1
                    }
                    when {
                        inputs[index] <= 0 -> {
                            index--
                        }

                        inputs[index] == 1L -> {
                            inputs[index] = 0L
                            index--
                        }

                        else -> {
                            inputs[index]--
                            break
                        }
                    }
                }
                continue
            }
        } else if (inputs[index] == 0L) {
            inputs[index] = 9L
        }

        val x = if (targetNum == inputs[index]) 0L else 1L
        z /= param1
        results[index] = z * (x * 25L + 1L) + (inputs[index] + param3) * x

        index++
    }
    println(inputs.joinToString("") { it.absoluteValue.toString() })

    println(process(inputs.map { it.absoluteValue }))

}