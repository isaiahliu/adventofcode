package y2022

import input

fun main() {
    val rootFolder = Day07Folder("root", null)

    var currentFolder = rootFolder

    val allFolders = arrayListOf(rootFolder)

    input.map { it.split(" ") }.forEach { nodes ->
        when (nodes[0]) {
            "$" -> {
                when (nodes[1]) {
                    "cd" -> {
                        when (nodes[2]) {
                            "/" -> {
                                currentFolder = rootFolder
                            }

                            ".." -> {
                                currentFolder.parent?.also { currentFolder = it }
                            }

                            else -> {
                                currentFolder = currentFolder.childrenFolders.first { it.name == nodes[2] }
                            }
                        }
                    }

                    "ls" -> {
                    }
                }
            }

            "dir" -> {
                Day07Folder(nodes[1], currentFolder).also {
                    currentFolder.childrenFolders += it
                    allFolders += it
                }
            }

            else -> {
                currentFolder.childrenFiles += Day07File(nodes[1], nodes[0].toInt())
            }
        }
    }

    println(allFolders.filter { it.parent != null }.map { it.size }.filter { it <= 100000 }.sum())

    val freeSpace = rootFolder.size - (70000000 - 30000000)
    println(allFolders.map { it.size }.sorted().first { it >= freeSpace })
}

private sealed interface IDay07File {
    val name: String
    val size: Int
}

private class Day07Folder(override val name: String, val parent: Day07Folder?) : IDay07File {
    val childrenFolders = arrayListOf<Day07Folder>()
    val childrenFiles = arrayListOf<Day07File>()


    override val size: Int
        get() {
            return childrenFolders.sumOf { it.size } + childrenFiles.sumOf { it.size }
        }
}

private class Day07File(override val name: String, override val size: Int) : IDay07File