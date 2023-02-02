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

    println(process(List(14) { 9L }))

    val params = arrayListOf<Long>()
    input.forEachIndexed { index, s ->
        when (index % 18) {
            5, 4, 15 -> params += s.split(" ").last().toLong()
        }
    }

    var z = 0L
    for (index in params.indices step 3) {
        val param1 = params[index]
        val param2 = params[index + 1]
        val param3 = params[index + 2]
        println("${param1} ${param2} ${param3}")
        val x = if (z.mod(26L) + param2 == 9L) 0L else 1L
        z /= param1
        z = z * (x * 25L + 1L) + (9L + param3) * x
        println("x=${x} z=${z}")
    }
    println(z)
}