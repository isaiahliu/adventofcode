package y2023

import util.expect
import util.input
import java.util.*

fun main() {
    expect(0) {
        val visited = hashSetOf<Pair<Int, Int>>()
        val stack = LinkedList<Pair<Pair<Int, Int>, MutableSet<Pair<Int, Int>>>>()
        stack += 0 to 0 to hashSetOf()

        loop@ while (true) {
            var goBack = false

            val (pos, nexts) = stack.peek()
            val (r, c) = pos

            if (r == input.lastIndex) {
                part1Result = maxOf(part1Result, visited.size - 1)
                goBack = true
            } else {
                val next = when (input[r][c]) {
                    '>' -> {
                        setOf(r to c + 1)
                    }

                    '<' -> {
                        setOf(r to c - 1)
                    }

                    '^' -> {
                        setOf(r - 1 to c)
                    }

                    'v' -> {
                        setOf(r + 1 to c)
                    }

                    else -> {
                        setOf(
                            r to c + 1, r to c - 1, r - 1 to c, r + 1 to c
                        )
                    }
                }.filter {
                    it !in visited && (input.getOrNull(it.first)?.getOrNull(it.second) ?: '#') != '#'
                }.toMutableSet()

                when {
                    next.isEmpty() -> {
                        goBack = true
                    }

                    next.size == 1 -> {
                        val single = next.first()
                        visited.add(single)
                        stack.push(single to hashSetOf())
                    }

                    else -> {
                        val first = next.first()
                        next.remove(first)

                        nexts.addAll(next)

                        visited.add(first)
                        stack.push(first to hashSetOf())
                    }
                }
            }

            if (goBack) {
                while (true) {
                    val top = stack.peek() ?: break@loop

                    if (top.second.isEmpty()) {
                        stack.pop()
                        visited.remove(top.first)
                    } else {
                        val next = top.second.first()
                        top.second.remove(next)
                        visited.add(next)
                        stack.push(next to hashSetOf())
                        break
                    }
                }
            }
        }

        class Node {
            val adjacent = hashMapOf<Node, Int>()
        }

        val start = Node()
        val end = Node()
        val nodeMap = hashMapOf(0 to 1 to start, input.lastIndex to input[0].lastIndex - 1 to end)

        visited.clear()
        visited += 0 to 1
        fun Node.dfs(pos: Pair<Int, Int>) {
            if (visited.add(pos)) {
                var t = pos

                var size = 1
                while (true) {
                    if (t.first == input.lastIndex) {
                        this.adjacent[end] = size
                        end.adjacent[this] = size
                        return
                    } else {
                        val nexts = arrayOf(
                            t.first + 1 to t.second,
                            t.first - 1 to t.second,
                            t.first to t.second + 1,
                            t.first to t.second - 1
                        ).filter {
                            (input.getOrNull(it.first)?.getOrNull(it.second) ?: '#') != '#'
                        }.onEach {
                            nodeMap[it]?.takeIf { it != this }?.also {
                                it.adjacent[this] = it.adjacent[this]?.takeIf { it > size + 1 } ?: (size + 1)
                                this.adjacent[it] = this.adjacent[it]?.takeIf { it > size + 1 } ?: (size + 1)
                            }
                        }

                        if (nexts.size <= 2) {
                            t = nexts.firstOrNull { visited.add(it) } ?: break
                        } else {
                            val newNode = Node()
                            nodeMap[t] = newNode

                            newNode.adjacent[this] = newNode.adjacent[this]?.takeIf { it > size } ?: size
                            this.adjacent[newNode] = this.adjacent[newNode]?.takeIf { it > size } ?: size

                            nexts.forEach {
                                newNode.dfs(it)
                            }

                            break
                        }
                    }

                    size++
                }
            }
        }

        start.dfs(1 to 1)

        fun Node.findMax(visited: Set<Node>): Int {
            if (this == end) {
                return 0
            }

            return adjacent.entries.filter { it.key !in visited }.maxOfOrNull { (next, steps) ->
                next.findMax(visited + next) + steps
            } ?: Int.MIN_VALUE
        }

        part2Result = start.findMax(setOf(start))
    }
}
