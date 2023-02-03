package y2019

import util.input

fun main() {
    val DOORS = 'A'..'Z'
    val KEYS = 'a'..'z'

    class AccessNode(val distance: Int, pass: Set<Char>) {
        val passDoors: Set<Char> = pass.filter { it in DOORS }.toSet()
        val passKeys: Set<Char> = pass.filter { it in KEYS }.toSet()
    }

    fun process(splitIntoFour: Boolean): Int {
        val map = input.map {
            it.toCharArray()
        }.toTypedArray()

        val currentRowIndex = map.indexOfFirst { '@' in it }
        val currentColumnIndex = map[currentRowIndex].indexOf('@')

        if (splitIntoFour) {
            map[currentRowIndex][currentColumnIndex] = '#'
            map[currentRowIndex - 1][currentColumnIndex] = '#'
            map[currentRowIndex + 1][currentColumnIndex] = '#'
            map[currentRowIndex][currentColumnIndex - 1] = '#'
            map[currentRowIndex][currentColumnIndex + 1] = '#'
        } else {
            map[currentRowIndex][currentColumnIndex] = '.'
        }

        val allNodes = map.map { it.filter { it in KEYS || it in DOORS } }.flatten().toSet()
        val allKeys = allNodes.filter { it in KEYS }.toSet()

        val accessNodes = hashMapOf<Char, Map<Char, AccessNode>>()

        fun accessNodes(pos: Pair<Int, Int>, stopOnFirst: Boolean): Map<Char, AccessNode> {
            val result = hashMapOf<Char, AccessNode>()

            val tasks = arrayListOf(pos to emptySet<Char>())
            var distance = 0

            val visited = hashSetOf(pos)

            fun visit(p: Pair<Int, Int>, pass: Set<Char>, distance: Int, node: Char? = null) {
                if (visited.add(p)) {
                    if (node != null) {
                        result[map[p.first][p.second]] = AccessNode(distance, pass)

                        tasks += p to pass + node
                    } else {
                        tasks += p to pass
                    }
                }
            }

            while (tasks.isNotEmpty()) {
                val current = tasks.toList()
                tasks.clear()
                distance++

                for ((p, pass) in current) {
                    val (rowIndex, columnIndex) = p

                    arrayOf(
                        rowIndex - 1 to columnIndex,
                        rowIndex + 1 to columnIndex,
                        rowIndex to columnIndex - 1,
                        rowIndex to columnIndex + 1
                    ).filter {
                        it !in visited
                    }.forEach { (r, c) ->
                        when (val node = map[r][c]) {
                            '#' -> return@forEach
                            '.' -> {
                                visit(r to c, pass, distance)
                            }

                            else -> {
                                visit(r to c, pass, distance, node)

                                if (stopOnFirst) {
                                    tasks.removeLast()
                                }
                            }
                        }
                    }
                }
            }

            return result
        }

        allNodes.forEach { node ->
            val r = map.indexOfFirst { node in it }
            val c = map[r].indexOf(node)

            accessNodes[node] = accessNodes(r to c, false)
        }

        val firstAccessNodes = buildSet {
            if (splitIntoFour) {
                add(currentRowIndex - 1 to currentColumnIndex - 1)
                add(currentRowIndex - 1 to currentColumnIndex + 1)
                add(currentRowIndex + 1 to currentColumnIndex - 1)
                add(currentRowIndex + 1 to currentColumnIndex + 1)
            } else {
                add(currentRowIndex to currentColumnIndex)
            }
        }.map {
            accessNodes(it, true)
        }.toTypedArray()

        val walked = hashMapOf<String, Int>()
        var min = Int.MAX_VALUE
        fun walk(distance: Int, nodesList: Array<Map<Char, AccessNode>>, collectedNodes: Set<Char>) {
            if (distance >= min) {
                return
            }

            if (allKeys.all { it in collectedNodes }) {
                min = distance
                return
            }

            nodesList.forEachIndexed { index, nodes ->
                nodes.filter { (c, accessNode) ->
                    c !in collectedNodes && accessNode.passDoors.all { it in collectedNodes } && (c in KEYS || (c + 32) in collectedNodes)
                }.forEach { (c, accessNode) ->
                    val walkedKey = "${c}_${collectedNodes.sorted().joinToString("")}"
                    val walkedDistance = distance + accessNode.distance

                    if (walkedDistance < (walked[walkedKey] ?: Int.MAX_VALUE)) {
                        walked[walkedKey] = walkedDistance

                        Array(nodesList.size) {
                            if (index == it) {
                                accessNodes[c]!!
                            } else {
                                nodesList[it]
                            }
                        }.also {
                            walk(walkedDistance, it, collectedNodes + c + accessNode.passKeys + accessNode.passDoors)
                        }

                    }
                }
            }
        }

        walk(0, firstAccessNodes, emptySet())

        return min
    }
    println(process(false))
    println(process(true))
}