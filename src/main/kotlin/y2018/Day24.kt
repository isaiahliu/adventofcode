package y2018

import util.input

fun main() {
    class BattleGroup(val hpPerUnit: Int, var unitCount: Int, val str: Int, val damageType: String, val speed: Int, val immune: Set<String>, val weak: Set<String>) {
        val alive: Boolean get() = unitCount > 0

        val effectivePower: Int get() = unitCount * str

        fun damageTo(target: BattleGroup): Int {
            val times = when (damageType) {
                in target.immune -> 0
                in target.weak -> 2
                else -> 1
            }

            return unitCount * str * times
        }

        fun attack(target: BattleGroup) {
            target.unitCount -= (damageTo(target) / target.hpPerUnit).coerceAtMost(target.unitCount)
        }
    }

    fun process(boost: Int): Pair<Int, Int> {
        val immuneSystem = arrayListOf<BattleGroup>()
        val infection = arrayListOf<BattleGroup>()

        var currentGroup = immuneSystem

        val regex = "(\\d+) units each with (\\d+) hit points (\\(.*\\) )?with an attack that does (\\d+) (\\w+) damage at initiative (\\d+)".toRegex()
        var additionalStr = 0
        input.forEach {
            when (it) {
                "Immune System:" -> {
                    currentGroup = immuneSystem
                    additionalStr = boost
                }

                "Infection:" -> {
                    currentGroup = infection
                    additionalStr = 0
                }

                "" -> {}
                else -> {
                    val match = regex.matchEntire(it)?.groupValues ?: return@forEach

                    val unitCount = match[1].toInt()
                    val hp = match[2].toInt()
                    val special = match[3].trim(' ', '(', ')').split(" ").map { it.trim(';', ',') }
                    val str = match[4].toInt() + additionalStr
                    val damageType = match[5]
                    val speed = match[6].toInt()
                    val immunes = hashSetOf<String>()
                    val weaks = hashSetOf<String>()

                    var c = immunes
                    var index = 0
                    while (index < special.size) {
                        when (special[index]) {
                            "immune" -> {
                                c = immunes
                                index++
                            }

                            "weak" -> {
                                c = weaks
                                index++
                            }

                            else -> {
                                c += special[index]
                            }
                        }
                        index++
                    }

                    currentGroup += BattleGroup(hp, unitCount, str, damageType, speed, immunes, weaks)
                }
            }
        }

        val attackTasks = arrayListOf<Pair<BattleGroup, BattleGroup>>()
        val groups = arrayOf(immuneSystem, infection)
        while (immuneSystem.isNotEmpty() && infection.isNotEmpty()) {
            attackTasks.clear()

            repeat(2) {
                val attackGroup = groups[it].sortedWith(compareByDescending<BattleGroup> { it.effectivePower }.thenByDescending { it.speed })
                val defendGroup = groups[1 - it].sortedWith(compareByDescending<BattleGroup> { it.effectivePower }.thenByDescending { it.speed }).toMutableList()

                attackGroup.forEach { attack ->
                    defendGroup.sortedWith(compareByDescending<BattleGroup> { defense ->
                        attack.damageTo(defense)
                    }.thenByDescending { it.effectivePower }.thenByDescending { it.speed }).firstOrNull()?.takeIf { attack.damageTo(it) > 0 }?.also { defense ->
                        attackTasks += attack to defense
                        defendGroup -= defense
                    }
                }
            }

            if (attackTasks.isEmpty()) {
                break
            }

            attackTasks.sortedWith(compareByDescending { it.first.speed }).forEach { (attack, defense) ->
                if (attack.alive) {
                    attack.attack(defense)

                    if (!defense.alive) {
                        immuneSystem -= defense
                        infection -= defense
                    }
                }
            }
        }

        return immuneSystem.sumOf { it.unitCount } to infection.sumOf { it.unitCount }
    }

    println(process(0).let { it.first + it.second })

    var boost = 1
    while (true) {
        val (immuCount, infectionCount) = process(boost)

        if (infectionCount == 0) {
            println(immuCount)
            break
        }

        boost++
    }
}