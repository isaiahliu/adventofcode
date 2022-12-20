package y2018

import util.input
import java.time.LocalDateTime

fun main() {
//    var guardSleeps = hashMapOf<Int, Int>()
    val guardSleepMinutes = hashMapOf<Int, MutableMap<Int, Int>>()

    var currentGuard = 0
    var time = LocalDateTime.now()

    input.sorted().map { it.split('-', ']', ':', ' ') }.forEach {
        val now = LocalDateTime.of(2018, it[1].toInt(), it[2].toInt(), it[3].toInt(), it[4].toInt(), 0)

        when (it[6]) {
            "Guard" -> {
                currentGuard = it[7].trimStart('#').toInt()
            }

            "falls" -> {
                time = now
            }

            "wakes" -> {
//                guardSleeps[currentGuard] = (guardSleeps[currentGuard]
//                        ?: 0) + Duration.between(time, now).toMinutes().toInt()

                val minutes = guardSleepMinutes.computeIfAbsent(currentGuard) { hashMapOf() }

                (time.minute until now.minute).forEach {
                    minutes[it] = (minutes[it] ?: 0) + 1
                }
            }
        }

    }
    val maxSleepGuard = guardSleepMinutes.entries.maxBy { it.value.values.sum() }

    println(maxSleepGuard.key * maxSleepGuard.value.entries.maxBy { it.value }.key)

    val maxSleepMinuteGuard = guardSleepMinutes.entries.maxBy { it.value.values.max() }

    println(maxSleepMinuteGuard.key * maxSleepMinuteGuard.value.entries.maxBy { it.value }.key)
}