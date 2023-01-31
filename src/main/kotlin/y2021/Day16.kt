package y2021

import util.input

fun main() {
    class Packet(val version: Int, val typeId: Int) {
        val data = arrayListOf<Int>()

        val subPackets = arrayListOf<Packet>()

        var size = 6

        val value: Long by lazy {
            when (typeId) {
                4 -> {
                    data.joinToString("") { it.toString(2).padStart(4, '0') }.toLong(2)
                }

                0 -> {
                    subPackets.sumOf { it.value }
                }

                1 -> {
                    subPackets.fold(1L) { a, b -> a * b.value }
                }

                2 -> {
                    subPackets.minOf { it.value }
                }

                3 -> {
                    subPackets.maxOf { it.value }
                }

                5 -> {
                    if (subPackets[0].value > subPackets[1].value) 1 else 0
                }

                6 -> {
                    if (subPackets[0].value < subPackets[1].value) 1 else 0
                }

                7 -> {
                    if (subPackets[0].value == subPackets[1].value) 1 else 0
                }

                else -> {
                    throw RuntimeException("Error ${typeId}")
                }
            }
        }

        val versionSum: Int
            get() {
                return version + subPackets.sumOf { it.versionSum }
            }
    }

    val bits = buildString {
        input.first().forEach {
            append(it.toString().toInt(16).toString(2).padStart(4, '0'))
        }
    }

    fun read(startIndex: Int): Packet {
        val version = bits.substring(startIndex, startIndex + 3).toInt(2)
        val id = bits.substring(startIndex + 3, startIndex + 6).toInt(2)

        val packet = Packet(version, id)

        when (id) {
            4 -> {
                for (index in startIndex + 6 until bits.length step 5) {
                    packet.size += 5
                    packet.data += bits.substring(index + 1, index + 5).toInt(2)

                    if (bits[index] == '0') {
                        break
                    }
                }
            }

            else -> {
                packet.size += 1
                when (bits[startIndex + 6]) {
                    '0' -> {
                        packet.size += 15
                        val subSize = bits.substring(startIndex + 7, startIndex + 7 + 15).toInt(2)

                        var subStart = startIndex + packet.size
                        while (packet.subPackets.sumOf { it.size } < subSize) {
                            val subPacket = read(subStart)

                            packet.subPackets += subPacket

                            subStart += subPacket.size
                        }
                        packet.size += subSize
                    }

                    '1' -> {
                        packet.size += 11
                        val subCount = bits.substring(startIndex + 7, startIndex + 7 + 11).toInt(2)

                        while (packet.subPackets.size < subCount) {
                            val subPacket = read(startIndex + packet.size)

                            packet.size += subPacket.size
                            packet.subPackets += subPacket
                        }
                    }
                }
            }
        }

        return packet
    }

    val packet = read(0)

    println(packet.versionSum)
    println(packet.value)
}