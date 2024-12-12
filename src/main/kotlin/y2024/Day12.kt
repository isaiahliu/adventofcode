package y2024

import util.expect
import util.input

fun main() {
    expect(0, 0) {
        class Group {
            var size = 1

            var innerParent: Group? = null
                private set

            var parent: Group
                set(value) {
                    innerParent = value
                    value.size += size
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

        val grids = Array(input.size) {
            Array(input[0].length) { Group() }
        }

        grids.forEachIndexed { r, row ->
            row.forEachIndexed { c, grid ->
                arrayOf(r - 1 to c, r to c - 1).filter { (nr, nc) -> input.getOrNull(nr)?.getOrNull(nc) == input[r][c] }.forEach { (nr, nc) ->
                    grid.join(grids[nr][nc])
                }
            }
        }

        grids.forEachIndexed { r, row ->
            row.forEachIndexed { c, grid ->
                val area = grid.parent.size

                val ch = input[r][c]
                val topLeft = input.getOrNull(r - 1)?.getOrNull(c - 1)
                val top = input.getOrNull(r - 1)?.getOrNull(c)
                val topRight = input.getOrNull(r - 1)?.getOrNull(c + 1)
                val left = input.getOrNull(r)?.getOrNull(c - 1)
                val right = input.getOrNull(r)?.getOrNull(c + 1)
                val bottomLeft = input.getOrNull(r + 1)?.getOrNull(c - 1)
                val bottom = input.getOrNull(r + 1)?.getOrNull(c)

                if (ch != top) {
                    part1Result += area

                    if (ch != left || left == topLeft) {
                        part2Result += area
                    }
                }

                if (ch != left) {
                    part1Result += area

                    if (ch != top || top == topLeft) {
                        part2Result += area
                    }
                }

                if (ch != bottom) {
                    part1Result += area

                    if (ch != left || left == bottomLeft) {
                        part2Result += area
                    }
                }

                if (ch != right) {
                    part1Result += area

                    if (ch != top || top == topRight) {
                        part2Result += area
                    }
                }
            }
        }

    }
}