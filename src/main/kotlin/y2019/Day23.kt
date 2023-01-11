package y2019

import util.input
import java.util.*
import kotlin.math.pow

fun main() {
    class IntCodeComputers(private val natMode: Boolean, private val debug: Boolean = false) {
        val computers = Array(50) { IntCodeComputer(it.toLong(), debug) }

        var result = -1L

        var natPacket = arrayListOf<Long>()

        var natSentY = -1L

        fun process() {
            while (result < 0) {
                val busyComputer = computers.firstOrNull { it.inputs.isNotEmpty() }

                if (busyComputer != null) {
                    busyComputer.resume()
                } else if (natMode) {
                    if (debug) {
                        println("NAT: ${natPacket[0]},${natPacket[1]}")
                    }

                    if (natSentY == natPacket[1]) {
                        result = natSentY
                    } else {
                        natSentY = natPacket[1]
                        computers[0].inputs += natPacket
                    }
                } else {
                    break
                }
            }
        }

        inner class IntCodeComputer(private val address: Long, private val debug: Boolean = false) {
            val memory = input.first().let {
                it.split(",").map { it.toLong() }.toLongArray()
            }.mapIndexed { index, l ->
                index.toLong() to l
            }.toMap().toMutableMap()

            var index = 0L
            var relativeBase = 0L

            fun readParam(paramIndex: Long): Long {
                return when ((memory[index]!!.toInt() / 10 / (10.0.pow(paramIndex.toInt())).toInt()) % 10) {
                    0 -> memory[memory[index + paramIndex] ?: 0L] ?: 0L
                    1 -> memory[index + paramIndex] ?: 0L
                    else -> memory[relativeBase + (memory[index + paramIndex] ?: 0L)] ?: 0L
                }
            }

            fun writeParam(paramIndex: Long, value: Long) {
                when ((memory[index]!!.toInt() / 10 / (10.0.pow(paramIndex.toInt())).toInt()) % 10) {
                    0 -> memory[memory[index + paramIndex] ?: 0L] = value
                    1 -> {
                        memory[index + paramIndex] = value
                    }

                    else -> {
                        memory[relativeBase + (memory[index + paramIndex] ?: 0L)] = value
                    }
                }
            }

            var noFurtherValues = false
            fun resume() {
                var suspend = false
                while (!suspend) {
                    when (memory[index]!!.toInt() % 100) {
                        1 -> {
                            writeParam(3, readParam(1) + readParam(2))
                            index += 4
                        }

                        2 -> {
                            writeParam(3, readParam(1) * readParam(2))
                            index += 4
                        }

                        3 -> {
                            val input = inputReader()

                            if (input == null) {
                                suspend = true
                            } else {
                                writeParam(1, input)
                                index += 2
                            }
                        }

                        4 -> {
                            outputWriter(readParam(1))
                            index += 2
                        }

                        5 -> {
                            if (readParam(1) != 0L) {
                                index = readParam(2)
                            } else {
                                index += 3
                            }
                        }

                        6 -> {
                            if (readParam(1) == 0L) {
                                index = readParam(2)
                            } else {
                                index += 3
                            }
                        }

                        7 -> {
                            writeParam(3, if (readParam(1) < readParam(2)) {
                                1
                            } else {
                                0
                            })
                            index += 4
                        }

                        8 -> {
                            writeParam(3, if (readParam(1) == readParam(2)) {
                                1
                            } else {
                                0
                            })
                            index += 4
                        }

                        9 -> {
                            relativeBase += readParam(1)

                            index += 2
                        }

                        99 -> {
                            suspend = true
                        }

                        else -> {
                            println("Error")
                        }
                    }
                }
            }

            val inputs = LinkedList<Long>().also {
                it += address
            }

            fun inputReader(): Long? {
                return if (inputs.isNotEmpty()) {
                    inputs.pop()
                } else if (noFurtherValues) {
                    null
                } else {
                    noFurtherValues = true
                    -1L
                }
            }

            val outputs = arrayListOf<Long>()

            fun outputWriter(outputVal: Long) {
                if (debug) {
                    println("${address} -- ${outputVal}")
                }

                noFurtherValues = false
                outputs += outputVal
                if (outputs.size == 3) {
                    if (outputs[0] == 255L) {
                        if (natMode) {
                            natPacket.clear()
                            natPacket += outputs.drop(1)
                        } else {
                            result = outputs[2]
                        }
                    } else {
                        computers[outputs[0].toInt()].inputs += outputs.drop(1)
                    }
                    outputs.clear()
                }
            }
        }
    }

    println(IntCodeComputers(false).also { it.process() }.result)
    println(IntCodeComputers(true).also { it.process() }.result)
}