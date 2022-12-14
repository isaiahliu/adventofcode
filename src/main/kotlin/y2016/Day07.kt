package y2016

import input

fun main() {
    val regex = "(\\w+)\\[(\\w+)\\](.*)".toRegex()

    var result1 = 0
    var result2 = 0

    input.forEach {
        val outside = arrayListOf<String>()
        val inside = arrayListOf<String>()

        var line = it

        while (true) {
            if (line.isEmpty()) {
                break
            }

            val match = regex.matchEntire(line)
            if (match == null) {
                outside += line
                break
            } else {
                outside += match.groupValues[1]
                inside += match.groupValues[2]
                line = match.groupValues[3]
            }
        }

        if (outside.any { it.abba } && inside.none { it.abba }) {
            result1++
        }

        val aba = outside.map { it.aba }.flatten().toSet()
        val bab = inside.map { it.bab }.flatten().toSet()

        if (aba.intersect(bab).isNotEmpty()) {
            result2++
        }
    }

    println(result1)
    println(result2)
}

private val String.abba: Boolean
    get() {
        repeat(length - 3) {
            if (this[it] == this[it + 3] && this[it + 1] == this[it + 2] && this[it] != this[it + 1]) {
                return true
            }
        }

        return false
    }

private val String.aba: Set<String>
    get() {
        val set = hashSetOf<String>()
        repeat(length - 2) {
            if (this[it] == this[it + 2] && this[it] != this[it + 1]) {
                set += "${this[it]}${this[it + 1]}"
            }
        }

        return set
    }
private val String.bab: Set<String>
    get() {
        val set = hashSetOf<String>()
        repeat(length - 2) {
            if (this[it] == this[it + 2] && this[it] != this[it + 1]) {
                set += "${this[it + 1]}${this[it]}"
            }
        }

        return set
    }


