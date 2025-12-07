package y2025

import util.expect
import util.input

fun main() {
    expect(0, 0L) {
        var beam = hashMapOf<Int, Long>()
        input.forEach {
            val newBeam = hashMapOf<Int, Long>()
            it.forEachIndexed { index, ch ->
                when (ch) {
                    'S' -> newBeam[index] = 1L
                    '.' -> Unit
                    else -> {
                        beam.remove(index)?.also {
                            part1Result++
                            newBeam[index - 1] = (newBeam[index - 1] ?: 0) + it
                            newBeam[index + 1] = (newBeam[index + 1] ?: 0) + it
                        }
                    }
                }
            }

            beam.forEach { (key, value) ->
                newBeam[key] = (newBeam[key] ?: 0) + value
            }
            beam = newBeam
        }

        part2Result += beam.values.sum()
    }
}


