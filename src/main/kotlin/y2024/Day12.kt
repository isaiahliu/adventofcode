package y2024

import util.expect
import util.input

fun main() {
    expect(0, 0) {
        class Group {
            var size = 0

            val leftEdges = hashSetOf<Pair<Int, Int>>()
            val rightEdges = hashSetOf<Pair<Int, Int>>()
            val topEdges = hashSetOf<Pair<Int, Int>>()
            val bottomEdges = hashSetOf<Pair<Int, Int>>()

            var edgeCount: Int = 0

            fun calculateEdges() {
                arrayOf(leftEdges, rightEdges).forEach { edges ->
                    while (edges.isNotEmpty()) {
                        val (r, c) = edges.first()

                        edges -= r to c

                        arrayOf(-1, 1).forEach {
                            var nr = r + it
                            while (edges.remove(nr to c)) {
                                nr += it
                            }
                        }

                        edgeCount++
                    }
                }

                arrayOf(topEdges, bottomEdges).forEach { edges ->
                    while (edges.isNotEmpty()) {
                        val (r, c) = edges.first()

                        edges -= r to c

                        arrayOf(-1, 1).forEach {
                            var nc = c + it
                            while (edges.remove(r to nc)) {
                                nc += it
                            }
                        }

                        edgeCount++
                    }
                }
            }

            var innerParent: Group? = null
                private set

            var parent: Group
                set(value) {
                    innerParent = value
                    value.size += size
                    value.leftEdges += leftEdges
                    value.rightEdges += rightEdges
                    value.topEdges += topEdges
                    value.bottomEdges += bottomEdges
                }
                get() {
                    return innerParent?.parent?.also {
                        innerParent = it
                    } ?: this
                }

            fun join(target: Group) {
                val leftParent = parent
                val rightParent = target.parent

                if (leftParent != rightParent) {
                    leftParent.parent = rightParent
                }
            }
        }

        val grids = Array(input.size) { r ->
            Array(input[0].length) { c ->
                Group().also {
                    it.size++

                    when (r) {
                        0 -> {
                            it.topEdges += 0 to c
                        }

                        input.lastIndex -> {
                            it.bottomEdges += input.size to c
                        }
                    }

                    when (c) {
                        0 -> {
                            it.leftEdges += r to 0
                        }

                        input[0].lastIndex -> {
                            it.rightEdges += r to input[0].length
                        }
                    }
                }
            }
        }

        grids.forEachIndexed { r, row ->
            row.forEachIndexed { c, grid ->
                if (input.getOrNull(r - 1)?.getOrNull(c) == input[r][c]) {
                    grid.join(grids[r - 1][c])
                } else {
                    grid.parent.topEdges += r to c
                    grids.getOrNull(r - 1)?.getOrNull(c)?.parent?.bottomEdges?.add(r to c)
                }

                if (input.getOrNull(r)?.getOrNull(c - 1) == input[r][c]) {
                    grid.join(grids[r][c - 1])
                } else {
                    grid.parent.leftEdges += r to c
                    grids[r].getOrNull(c - 1)?.parent?.rightEdges?.add(r to c)
                }
            }
        }

        grids.forEachIndexed { r, row ->
            row.forEachIndexed { c, grid ->
                arrayOf(r - 1 to c, r + 1 to c, r to c - 1, r to c + 1).forEach { (nr, nc) ->
                    if (input.getOrNull(nr)?.getOrNull(nc) != input[r][c]) {
                        part1Result += grid.parent.size
                    }
                }
            }
        }

        part2Result = grids.map { it.map { it.parent } }.flatten().distinct().sumOf {
            it.calculateEdges()

            it.edgeCount * it.size
        }
    }
}