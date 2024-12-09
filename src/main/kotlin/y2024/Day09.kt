package y2024

import util.expect
import util.input

fun main() {
    expect(0L, 0L) {
        val line = input[0]

        run {
            var leftIndex = 0
            var rightIndex = (line.lastIndex or 1) - 1

            var diskIndex = 0L

            var freeSpace = 0
            var remainingSize = line[rightIndex] - '0'
            while (leftIndex + 1 < rightIndex) {
                when {
                    leftIndex % 2 == 0 -> {
                        val size = line[leftIndex] - '0'

                        part1Result += leftIndex / 2 * (diskIndex * 2 + size - 1) * size / 2

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

                        part1Result += rightIndex / 2 * (diskIndex * 2 + size - 1) * size / 2

                        freeSpace -= size
                        remainingSize -= size
                        diskIndex += size
                    }
                }
            }

            part1Result += rightIndex / 2 * (diskIndex + diskIndex + remainingSize - 1) * remainingSize / 2
        }

        run {
            class SegNode(val leftBlockIndex: Int, val rightBlockIndex: Int) {
                var maxFreeSpace: Int = 0
                var startDiskIndex: Long = 0

                val children by lazy {
                    arrayOf(
                        SegNode(leftBlockIndex, (leftBlockIndex + rightBlockIndex) / 2), SegNode((leftBlockIndex + rightBlockIndex) / 2 + 1, rightBlockIndex)
                    )
                }

                fun setBlock(blockIndex: Int, freeSpace: Int, startDiskIndex: Long) {
                    when {
                        blockIndex > rightBlockIndex || blockIndex < leftBlockIndex -> return
                        leftBlockIndex < rightBlockIndex -> {
                            children.forEach {
                                it.setBlock(blockIndex, freeSpace, startDiskIndex)
                            }

                            maxFreeSpace = children.maxOf { it.maxFreeSpace }
                        }

                        else -> {
                            this.startDiskIndex = startDiskIndex
                            maxFreeSpace = freeSpace
                        }
                    }
                }

                fun findFirstAvailableBlock(freeSpace: Int, maxBlockIndex: Int): SegNode? {
                    return when {
                        maxFreeSpace < freeSpace -> null
                        maxBlockIndex < leftBlockIndex -> null
                        leftBlockIndex == rightBlockIndex -> this
                        else -> children[0].findFirstAvailableBlock(freeSpace, maxBlockIndex) ?: children[1].findFirstAvailableBlock(freeSpace, maxBlockIndex)
                    }
                }
            }

            val root = SegNode(0, line.lastIndex)

            var diskIndex = 0L
            line.forEachIndexed { blockIndex, ch ->
                val blockSize = ch - '0'

                root.setBlock(blockIndex, blockSize * (blockIndex % 2), diskIndex)

                diskIndex += blockSize
            }

            for (blockIndex in line.lastIndex downTo 0) {
                val blockSize = line[blockIndex] - '0'

                diskIndex -= blockSize

                if (blockIndex % 2 == 0) {
                    val block = root.findFirstAvailableBlock(blockSize, blockIndex)

                    val moveToDiskIndex = block?.startDiskIndex ?: diskIndex

                    part2Result += blockIndex / 2 * (moveToDiskIndex * 2 + blockSize - 1) * blockSize / 2

                    block?.also {
                        root.setBlock(it.leftBlockIndex, it.maxFreeSpace - blockSize, it.startDiskIndex + blockSize)
                    }
                }
            }
        }
    }
}