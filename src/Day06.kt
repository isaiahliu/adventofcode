fun main() {
    val line = input.first()

    val distinct1 = 4
    val chars1 = CharArray(distinct1) {
        line[it]
    }

    val distinct2 = 14
    val chars2 = CharArray(distinct2) {
        line[it]
    }

    var part1Result = 0
    var part2Result = 0
    for ((index, c) in line.withIndex()) {
        chars1[index % distinct1] = c
        chars2[index % distinct2] = c

        if (part1Result == 0 && index >= distinct1 && chars1.distinct().size == distinct1) {
            part1Result = index + 1
        }

        if (part2Result == 0 && index >= distinct2 && chars2.distinct().size == distinct2) {
            part2Result = index + 1
        }

        if (part1Result > 0 && part2Result > 0) {
            break
        }
    }

    println(part1Result)
    println(part2Result)
}