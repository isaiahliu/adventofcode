package y2015

import util.expect
import util.input

fun main() {
    expect(0, 0) {
        class Trie(val length: Int = 0) {
            val children = hashMapOf<Char, Trie>()
            var replacements = hashSetOf<String>()

            fun appendString(str: String, index: Int = 0): Trie {
                return str.getOrNull(index)?.let {
                    children.computeIfAbsent(it) { Trie(length + 1) }.appendString(str, index + 1)
                } ?: this
            }
        }

        val replacementRoot1 = Trie()
        val replacements2 = arrayListOf<Pair<Int, Trie>>()

        input.forEach {
            it.split(" => ").takeIf { it.size == 2 }?.also { (from, to) ->
                replacementRoot1.appendString(from).replacements += to

                replacements2 += (to.length - from.length) to Trie().also {
                    it.appendString(to).replacements += from
                }
            }
        }

        replacements2.sortByDescending { it.first }

        fun process(line: String, replacementRoot: Trie): Set<String> {
            val result = hashSetOf<String>()

            var replacementNodes = setOf(replacementRoot)
            line.forEachIndexed { index, ch ->
                replacementNodes = buildSet {
                    add(replacementRoot)

                    replacementNodes.forEach {
                        it.children[ch]?.also { replacementNode ->
                            replacementNode.replacements.forEach { r ->
                                result += line.replaceRange(index - replacementNode.length + 1, index + 1, r)
                            }

                            add(replacementNode)
                        }
                    }
                }
            }

            return result
        }

        part1Result = process(input.last(), replacementRoot1).size

        fun dfs(line: String, step: Int): Boolean {
            if (line == "e") {
                part2Result = step
                return true
            }

            replacements2.forEach { (_, r) ->
                process(line, r).forEach {
                    if (dfs(it, step + 1)) {
                        return true
                    }
                }
            }

            return false
        }

        dfs(input.last(), 0)
    }
}