package y2015

import util.input

fun main() {
    val packages = input.map { it.toInt() }.sortedDescending()

    fun process(packageSum: Int): Long {
        var minPackageCount = Int.MAX_VALUE
        var minQe = Long.MAX_VALUE

        repeat(1 shl packages.size) { t ->
            val picked = packages.filterIndexed { index, _ ->
                t and (1 shl index) > 0
            }

            if (picked.size > minPackageCount) {
                return@repeat
            }

            if (picked.sum() == packageSum) {
                val qe = picked.fold(1L) { a, b -> a * b }

                if (picked.size < minPackageCount) {
                    minPackageCount = picked.size
                    minQe = qe
                } else {
                    minQe = minQe.coerceAtMost(qe)
                }
            }
        }

        return minQe
    }

    println(process(packages.sum() / 3))
    println(process(packages.sum() / 4))
}