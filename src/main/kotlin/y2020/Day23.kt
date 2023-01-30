package y2020

import util.input

fun main() {
    fun process(v2: Boolean, debug: Boolean = false): String {
        class Cup(val label: Int) {
            val outputLabel = label + 1
            lateinit var next: Cup
        }

        val cups = input.first().map { Cup(it - '1') }.toMutableList()

        val size = if (v2) 1000000 else cups.size

        for (addIndex in cups.size until size) {
            cups += Cup(addIndex)
        }

        val cup1 = cups.first { it.outputLabel == 1 }
        for (index in cups.indices) {
            cups[index].next = cups[(index + 1) % size]
        }

        val cupMap = cups.associateBy { it.label }

        var current = cups.first()
        repeat(if (v2) 10_000_000 else 100) {
            val nextHead = current.next

            current.next = nextHead.next.next.next

            val pickedLabel = arrayOf(nextHead.label, nextHead.next.label, nextHead.next.next.label)

            var targetLabel = (current.label - 1).mod(size)
            while (targetLabel in pickedLabel) {
                targetLabel = (targetLabel - 1).mod(size)
            }

            val target = cupMap[targetLabel]!!

            nextHead.next.next.next = target.next
            target.next = nextHead

            current = current.next

            if (debug) {
                var t = cup1
                repeat(size) {
                    print(t.outputLabel)
                    print(",")
                    t = t.next
                }
                println()
            }
        }

        return buildString {
            var t = cup1

            if (v2) {
                append(1L * t.next.outputLabel * t.next.next.outputLabel)
            } else {
                repeat(size - 1) {
                    t = t.next
                    append((t.outputLabel).toString())
                }
            }
        }
    }
    println(process(false))
    println(process(true))
}