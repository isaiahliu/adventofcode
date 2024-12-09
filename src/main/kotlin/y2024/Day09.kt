package y2024

import util.expect
import util.input

fun main() {
    expect(0L, 0L) {
        val line = input[0]

        var leftIndex = 0
        var rightIndex = (line.lastIndex or 1) - 1

        var diskIndex = 0L

        var freeSpace = 0
        var remainingSize = line[rightIndex] - '0'
        while (leftIndex + 1 < rightIndex) {
            when {
                leftIndex % 2 == 0 -> {
                    val size = line[leftIndex] - '0'

                    part1Result += leftIndex / 2L * (diskIndex * 2 + size - 1) * size / 2

                    diskIndex += size
                    freeSpace = line[++leftIndex] - '0'
                }

                freeSpace == 0 -> {
                    leftIndex++
                }

                remainingSize == 0 -> {
                    rightIndex -= 2
                    remainingSize = line[rightIndex] - '0'
                }

                else -> {
                    val size = minOf(freeSpace, remainingSize)

                    part1Result += rightIndex / 2L * (diskIndex * 2 + size - 1) * size / 2

                    freeSpace -= size
                    remainingSize -= size
                    diskIndex += size
                }
            }
        }

        part1Result += rightIndex / 2L * (diskIndex + diskIndex + remainingSize - 1) * remainingSize / 2

        class SegNode(val leftNodeIndex: Int, val rightNodeIndex: Int) {
            var maxFreeSpace: Int = 0
            var startDiskIndex: Long = 0

            val children by lazy {
                arrayOf(
                    SegNode(leftNodeIndex, (leftNodeIndex + rightNodeIndex) / 2), SegNode((leftNodeIndex + rightNodeIndex) / 2 + 1, rightNodeIndex)
                )
            }

            fun setFreeSpace(nodeIndex: Int, freeSpace: Int, startDiskIndex: Long) {
                when {
                    nodeIndex > rightNodeIndex || nodeIndex < leftNodeIndex -> {
                        return
                    }

                    leftNodeIndex < rightNodeIndex -> {
                        children.forEach {
                            it.setFreeSpace(nodeIndex, freeSpace, startDiskIndex)
                        }

                        maxFreeSpace = children.maxOf { it.maxFreeSpace }
                    }

                    else -> {
                        this.startDiskIndex = startDiskIndex
                        maxFreeSpace = freeSpace
                    }
                }
            }

            fun firstAvailableNode(freeSpace: Int): SegNode? {
                return when {
                    maxFreeSpace < freeSpace -> {
                        null
                    }

                    leftNodeIndex == rightNodeIndex -> {
                        this
                    }

                    else -> {
                        children[0].firstAvailableNode(freeSpace) ?: children[1].firstAvailableNode(freeSpace)
                    }
                }
            }
        }

        val root = SegNode(0, line.lastIndex)

        diskIndex = 0
        line.forEachIndexed { index, ch ->
            val size = ch - '0'

            if (index % 2 == 1) {
                root.setFreeSpace(index, size, diskIndex)
            }

            diskIndex += size
        }

        for (index in line.lastIndex downTo 0) {
            val ch = line[index]
            val size = ch - '0'

            diskIndex -= size

            if (index % 2 == 0) {
                val node = root.firstAvailableNode(size)?.takeIf { it.leftNodeIndex < index }

                val moveToDiskIndex = node?.startDiskIndex ?: diskIndex

                part2Result += index / 2L * (moveToDiskIndex * 2 + size - 1) * size / 2

                node?.also {
                    root.setFreeSpace(it.leftNodeIndex, it.maxFreeSpace - size, it.startDiskIndex + size)
                }
            }
        }
    }
}
