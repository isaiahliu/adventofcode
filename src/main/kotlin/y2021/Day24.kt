package y2021

import util.input

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
            4, 5, 15 -> {
                t += s.split(" ").last().toLong()
                if (t.size == 3) {
                    params += t.toLongArray()
                    t.clear()
                }
            }
        }
    }

    fun process(v2: Boolean): String {
        val initValue = if (v2) 0L else 10L
        val nextValue: (Long) -> (Long) = if (v2) {
            { it + 1 }
        } else {
            { it - 1 }
        }

        val inputs = LongArray(14) { initValue }
        val results = LongArray(14)
        var index = 0
        while (index < results.size) {
            val (param1, param2, param3) = params[index]

            val (nextParam1, nextParam2) = params.getOrNull(index + 1) ?: longArrayOf(1L, 0L)

            val zRange = if (nextParam1 == 26L) {
                1 - nextParam2..9L - nextParam2
            } else {
                null
            }

            inputs[index] = nextValue(inputs[index])
            while (true) {
                if (inputs[index] == 10L - initValue) {
                    inputs[index] = initValue
                    index--
                    break
                }

                val z = results.getOrNull(index - 1) ?: 0L

                val x = if ((z.mod(26L) + param2) == inputs[index]) 0L else 1L
                val newZ = (z / param1) * (x * 25L + 1L) + (inputs[index] + param3) * x

                var match = true

                if (param1 == 26L && x != 0L) {
                    match = false
                }

                if (zRange != null && newZ.mod(26L) !in zRange) {
                    match = false
                }

                if (match) {
                    results[index] = newZ
                    index++
                    break
                } else {
                    inputs[index] = nextValue(inputs[index])
                }
            }
        }
        return inputs.joinToString("")
    }

    println(process(false))
    println(process(true))
}