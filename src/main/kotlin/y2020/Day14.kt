package y2020

import util.input

fun main() {
    fun process(v2: Boolean): Long {
        val memory = hashMapOf<Long, Long>()

        var addressMapper: (Long) -> List<Long> = { listOf(it) }
        val valueWriters = arrayListOf<(Long) -> Long>()

        input.map { it.split(' ', '[', ']', '=').filter { it.isNotBlank() } }.forEach { nodes ->
            when (nodes[0]) {
                "mask" -> {
                    if (v2) {
                        addressMapper = { address ->
                            val addresses = arrayListOf<Long>()

                            var baseAddress = 0L

                            val optionalNums = arrayListOf<Long>()
                            repeat(36) {
                                val num = 1L shl it

                                when (nodes[1][35 - it]) {
                                    'X' -> {
                                        optionalNums += num
                                    }

                                    '1' -> {
                                        baseAddress += num
                                    }

                                    '0' -> {
                                        if (address and num > 0) {
                                            baseAddress += num
                                        }
                                    }
                                }
                            }

                            if (optionalNums.isEmpty()) {
                                addresses += baseAddress
                            } else {
                                repeat(2 shl optionalNums.size) { p ->
                                    var addr = baseAddress
                                    repeat(optionalNums.size) {
                                        if (p and (1 shl it) > 0) {
                                            addr += optionalNums[it]
                                        }
                                    }

                                    addresses += addr
                                }
                            }

                            addresses
                        }
                    } else {
                        valueWriters.clear()

                        nodes[1].reversed().forEachIndexed { index, c ->
                            when (c) {
                                '1' -> {
                                    valueWriters += {
                                        it or (1L shl index)
                                    }
                                }

                                '0' -> {
                                    valueWriters += {
                                        it and (-1L - (1L shl index))
                                    }
                                }
                            }
                        }
                    }
                }

                "mem" -> {
                    val value = valueWriters.fold(nodes[2].toLong()) { a, b ->
                        b(a)
                    }
                    addressMapper(nodes[1].toLong()).forEach {
                        memory[it] = value
                    }
                }

                else -> throw RuntimeException("Error")
            }
        }

        return memory.values.sum()
    }

    println(process(false))
    println(process(true))
}