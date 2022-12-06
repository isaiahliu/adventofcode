fun main() {
    val line = input.first()

    val chars1 = CharArray(4) {
        line[it]
    }

    val chars2 = CharArray(14) {
        line[it]
    }

    var part1Result = 0
    var part2Result = 0
    for ((index, c) in line.withIndex()) {
        chars1[index % 4] = c
        chars2[index % 14] = c

        if (part1Result == 0 && index > 3 && chars1.distinct().size == 4) {
            part1Result = index + 1
        }

        if (part2Result == 0 && index > 13 && chars2.distinct().size == 14) {
            part2Result = index + 1
        }

        if (part1Result > 0 && part2Result > 0) {
            break
        }
    }

    println(part1Result)
    println(part2Result)
}