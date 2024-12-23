package y2024

import util.expect
import util.input
import kotlin.math.sign

fun main() {
    expect(0, 0) {
        input.forEachIndexed { r, row ->
            row.forEachIndexed { c, ch ->
                when (ch) {
                    'X' -> {
                        part1Result += arrayOf(
                            -1 to -1, -1 to 0, -1 to 1,
                            0 to -1, 0 to 1,
                            1 to -1, 1 to 0, 1 to 1,
                        ).count { (deltaR, deltaC) ->
                            input.getOrNull(r + deltaR)?.getOrNull(c + deltaC) == 'M' && input.getOrNull(r + deltaR * 2)
                                ?.getOrNull(c + deltaC * 2) == 'A' && input.getOrNull(r + deltaR * 3)?.getOrNull(c + deltaC * 3) == 'S'
                        }
                    }

                    'A' -> {
                        part2Result += (arrayOf(-1 to -1, -1 to 1, 1 to -1, 1 to 1).sumOf { (deltaR, deltaC) ->
                            when (input.getOrNull(r + deltaR)?.getOrNull(c + deltaC)) {
                                'M' -> 0b10
                                'S' -> 0b01
                                else -> 0b00
                            } shl (deltaR * deltaC + 1)
                        } xor 0b1111).sign xor 1
                    }
                }
            }
        }
    }
}