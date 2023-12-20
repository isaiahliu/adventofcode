package y2023

import util.expectLong
import util.input
import java.math.BigInteger
import java.util.*

fun main() {
    expectLong {
        abstract class AbstractModule(val children: Array<String>) {
            abstract fun receive(from: String, pulse: Int): List<Pair<String, Int>>

            val outputs = Array(2) { p ->
                children.map { it to p }
            }

            open fun registerParent(parent: String) {
            }
        }

        class FlipFlop(children: Array<String>) : AbstractModule(children) {
            var status = 0

            override fun receive(from: String, pulse: Int): List<Pair<String, Int>> {
                return if (pulse == 0) {
                    status = status xor 1
                    outputs[status]
                } else {
                    emptyList()
                }
            }
        }

        class Conjunction(children: Array<String>) : AbstractModule(children) {
            val rem = hashMapOf<String, Int>()

            override fun registerParent(parent: String) {
                rem[parent] = 0
            }

            override fun receive(from: String, pulse: Int): List<Pair<String, Int>> {
                rem[from] = pulse

                return outputs[rem.values.reduce { a, b -> a and b } xor 1]
            }
        }

        class Simple(children: Array<String>) : AbstractModule(children) {
            override fun receive(from: String, pulse: Int): List<Pair<String, Int>> {
                return outputs[pulse]
            }
        }

        val modules = hashMapOf<String, AbstractModule>()

        val regex = "([%&]?)(\\w+) -> (.*)".toRegex()
        input.forEach {
            regex.matchEntire(it)?.groupValues?.drop(1)?.also { (type, name, childrenStr) ->
                val children = childrenStr.split(", ").toTypedArray()
                modules[name] = when (type) {
                    "%" -> {
                        FlipFlop(children)
                    }

                    "&" -> {
                        Conjunction(children)
                    }

                    else -> {
                        Simple(children)
                    }
                }
            } ?: println("Error: $it")
        }

        modules.forEach { (name, module) ->
            module.children.forEach {
                modules[it]?.registerParent(name)
            }
        }

        val pulses = LinkedList<Pair<String, Pair<String, Int>>>()
        val counts = longArrayOf(0, 0)

        val rxParentEntry = modules.entries.first { "rx" in it.value.children }
        val parentSize = (rxParentEntry.value as Conjunction).rem.keys.size
        val firstTurns = hashMapOf<String, BigInteger>()

        var loop = 0
        while (true) {
            loop++
            pulses += "button" to ("broadcaster" to 0)
            while (pulses.isNotEmpty()) {
                val (from, pair) = pulses.poll()
                val (to, pulse) = pair

                if (to == rxParentEntry.key && pulse == 1) {
                    firstTurns[from] = firstTurns[from] ?: loop.toBigInteger()

                    if (firstTurns.size == parentSize) {
                        part2Result = firstTurns.values.reduce { a, b ->
                            a * b / a.gcd(b)
                        }.toLong()

                        return@expectLong
                    }
                }

                counts[pulse]++

                modules[to]?.receive(from, pulse)?.forEach {
                    pulses += to to it
                }
            }

            if (loop == 1000) {
                part1Result = counts[0] * counts[1]
            }
        }
    }
}