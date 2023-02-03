package y2019

import util.input
import java.util.*
import kotlin.math.pow

fun main() {
    val DIRECTIONS = arrayOf("north", "east", "south", "west")

    abstract class AbstractRoom(val name: String) {
        abstract val neighbors: Array<AbstractRoom>

        val goods = hashSetOf<String>()
    }

    val NO_ROOM = object : AbstractRoom("NO_ROOM") {
        override val neighbors: Array<AbstractRoom> = emptyArray()
    }
    val UNKNOWN_ROOM = object : AbstractRoom("UNKNOWN_ROOM") {
        override val neighbors: Array<AbstractRoom> = emptyArray()
    }

    class Room(name: String) : AbstractRoom(name) {
        override val neighbors: Array<AbstractRoom> = arrayOf(NO_ROOM, NO_ROOM, NO_ROOM, NO_ROOM)

        fun findNextUnknownRoom(): List<Int> {
            return findRoom {
                this == UNKNOWN_ROOM
            }
        }

        fun findRoom(name: String): List<Int> {
            return findRoom {
                this.name == name
            }
        }

        private fun findRoom(predicate: AbstractRoom.() -> Boolean): List<Int> {
            val tasks: MutableList<Pair<AbstractRoom, List<Int>>> = arrayListOf(this to emptyList())

            val walked = hashSetOf<AbstractRoom>(this)

            while (tasks.isNotEmpty()) {
                val current = tasks.toList()
                tasks.clear()

                for ((room, route) in current) {
                    if (room.predicate()) {
                        return route
                    }

                    room.neighbors.forEachIndexed { index, neighbor ->
                        if (neighbor != NO_ROOM && walked.add(neighbor)) {
                            tasks += neighbor to (route + index)
                        }
                    }
                }
            }

            return emptyList()
        }
    }

    class IntCodeComputer(private val debug: Boolean = false) {
        fun process() {
            val memory = input.first().let {
                it.split(",").map { it.toLong() }.toLongArray()
            }.mapIndexed { index, l ->
                index.toLong() to l
            }.toMap().toMutableMap()

            var index = 0L
            var relativeBase = 0L
            var done = false

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

            while (!done) {
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
                        writeParam(1, inputReader())
                        index += 2
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
                        writeParam(
                            3, if (readParam(1) < readParam(2)) {
                                1
                            } else {
                                0
                            }
                        )
                        index += 4
                    }

                    8 -> {
                        writeParam(
                            3, if (readParam(1) == readParam(2)) {
                                1
                            } else {
                                0
                            }
                        )
                        index += 4
                    }

                    9 -> {
                        relativeBase += readParam(1)

                        index += 2
                    }

                    99 -> {
                        done = true
                    }

                    else -> {
                        println("Error")
                    }
                }
            }
        }

        val outputs = StringBuilder()

        val instructions = LinkedList<Char>()

        val rooms = hashSetOf<Room>()

        var inventory = hashSetOf<String>()

        var allRoomsVisited = false

        lateinit var currentRoom: Room
        var direction = -1

        var availableGoods = emptyArray<String>()

        var take = 0

        lateinit var securityCheckpointRoom: Room

        fun inputReader(): Long {
            if (instructions.isEmpty()) {
                if (!allRoomsVisited) {
                    val lines = outputs.toString().split("\n")

                    val takeGoods = arrayListOf<String>()

                    lines.map { it.split(" ", limit = 2) }.forEach { line ->
                        when (line[0]) {
                            "==" -> {
                                val roomName = line[1].dropLast(3)

                                currentRoom = rooms.firstOrNull { it.name == roomName } ?: run {
                                    val newRoom = Room(roomName)

                                    if (direction in (0..3)) {
                                        currentRoom.neighbors[direction] = newRoom
                                        newRoom.neighbors[(direction + 2) % 4] = currentRoom
                                    }

                                    rooms += newRoom
                                    newRoom
                                }

                                if (roomName == "Security Checkpoint") {
                                    securityCheckpointRoom = currentRoom
                                }

                                takeGoods.clear()
                            }

                            "-" -> when (val item = line[1]) {
                                "north", "south", "east", "west" -> {
                                    if (currentRoom.neighbors[DIRECTIONS.indexOf(item)] == NO_ROOM) {
                                        currentRoom.neighbors[DIRECTIONS.indexOf(item)] = UNKNOWN_ROOM
                                    }
                                }

                                "escape pod", "giant electromagnet", "photons", "molten lava", "infinite loop" -> {
                                    currentRoom.goods += item
                                }

                                else -> {
                                    takeGoods += item
                                }
                            }
                        }
                    }

                    takeGoods.forEach {
                        instructions += "take ${it}\n".toList()
                        inventory += it
                    }

                    val unknownIndex = currentRoom.neighbors.indexOf(UNKNOWN_ROOM)
                    if (unknownIndex == -1) {
                        val route = currentRoom.findNextUnknownRoom()

                        if (route.isEmpty()) {
                            allRoomsVisited = true

                            currentRoom.findRoom("Security Checkpoint").forEach {
                                instructions += "${DIRECTIONS[it]}\n".toList()
                            }

                            availableGoods = inventory.toTypedArray()

                            availableGoods.forEach {
                                instructions += "drop ${it}\n".toList()
                            }
                        } else {
                            direction = route.last()

                            route.forEach {
                                instructions += "${DIRECTIONS[it]}\n".toList()
                            }
                        }
                    } else {
                        direction = unknownIndex

                        instructions += "${DIRECTIONS[unknownIndex]}\n".toList()
                    }

                    outputs.clear()
                }

                if (allRoomsVisited) {
                    take++

                    val g = availableGoods.filterIndexed { index, s ->
                        take and (1 shl index) > 0
                    }

                    g.forEach {
                        instructions += "take ${it}\n".toList()
                    }

                    instructions += "inv\n".toList()

                    securityCheckpointRoom.findRoom("Pressure-Sensitive Floor").forEach {
                        instructions += "${DIRECTIONS[it]}\n".toList()
                    }

                    g.forEach {
                        instructions += "drop ${it}\n".toList()
                    }
                }
            }

            return instructions.pop().also {
                print(it)
            }.code.toLong()
        }

        fun outputWriter(outputVal: Long) {
            val outputChar = outputVal.toInt().toChar()

            if (debug) {
                print(outputChar)
            }
            outputs.append(outputChar)
        }
    }

    IntCodeComputer(true).also {
        it.process()
    }
}