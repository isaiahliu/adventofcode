package y2015

import util.expect
import util.input
import util.md5

fun main() {
    expect(0, 0) {
        var num = 0

        while (true) {
            val md5 = "${input[0]}${++num}".md5

            if (part1Result == 0 && md5.startsWith("00000")) {
                part1Result = num
            }

            if (md5.startsWith("000000")) {
                part2Result = num
                break
            }
        }
    }
}
