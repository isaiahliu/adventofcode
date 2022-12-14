package y2015

import input

fun main() {
    val weapons = listOf(
            Equipment("Dagger", 8, 4, 0),
            Equipment("Shortsword", 10, 5, 0),
            Equipment("Warhammer", 25, 6, 0),
            Equipment("Longsword", 40, 7, 0),
            Equipment("Greataxe", 74, 8, 0))
    val armors = listOf(
            Equipment("Leather", 13, 0, 1),
            Equipment("Chainmail", 31, 0, 2),
            Equipment("Splintmail", 53, 0, 3),
            Equipment("Bandedmail", 75, 0, 4),
            Equipment("Platemail", 102, 0, 5))
    val rings = listOf(
            Equipment("Damage +1", 25, 1, 0),
            Equipment("Damage +2", 50, 2, 0),
            Equipment("Damage +3", 100, 3, 0),
            Equipment("Defense +1", 20, 0, 1),
            Equipment("Defense +2", 40, 0, 2),
            Equipment("Defense +3", 80, 0, 3))

    var bossHitPoints = 0
    var bossDamage = 0
    var bossArmor = 0

    input.forEach {
        val (key, value) = it.split(": ")

        value.toIntOrNull()?.also {
            when (key) {
                "Hit Points" -> bossHitPoints = it
                "Armor" -> bossArmor = it
                "Damage" -> bossDamage = it
            }
        }
    }

    val equips = arrayListOf<Equip>()

    for (weaponIndex in weapons.indices) {
        for (armorIndex in -1 until armors.size) {
            for (ringIndex1 in -1 until rings.size) {
                for (ringIndex2 in ringIndex1 until rings.size) {
                    if (ringIndex2 >= 0 && ringIndex1 == ringIndex2) {
                        continue
                    }

                    equips += Equip(weapons[weaponIndex].cost + (armors.getOrNull(armorIndex)?.cost
                            ?: 0) + (rings.getOrNull(ringIndex1)?.cost ?: 0) + (rings.getOrNull(ringIndex2)?.cost
                            ?: 0), weaponIndex, armorIndex.takeIf { it >= 0 }, ringIndex1.takeIf { it >= 0 }, ringIndex2.takeIf { it >= 0 })
                }
            }
        }
    }

    fun Equip.battle(): Boolean {
        val player = Stat(true, 100,
                weapons[weaponIndex].damage + (armorIndex?.let { armors[it] }?.damage
                        ?: 0) + (ringIndex1?.let { rings[it] }?.damage ?: 0) + (ringIndex2?.let { rings[it] }?.damage
                        ?: 0),

                weapons[weaponIndex].armor + (armorIndex?.let { armors[it] }?.armor
                        ?: 0) + (ringIndex1?.let { rings[it] }?.armor ?: 0) + (ringIndex2?.let { rings[it] }?.armor
                        ?: 0)
        )
        val boss = Stat(false, bossHitPoints, bossDamage, bossArmor)

        var attacker = player
        var defender = boss

        while (true) {
            defender.hitPoints -= (attacker.damage - defender.armor).coerceAtLeast(1)

            if (defender.hitPoints <= 0) {
                return attacker.player
            }

            val t = attacker
            attacker = defender
            defender = t
        }
    }

    val sortedEquips = equips.sortedBy { it.cost }
    val result1 = sortedEquips.first {
        it.battle()
    }

    val result2 = sortedEquips.last {
        !it.battle()
    }

    println(result1.cost)
    println(result2.cost)
}

private data class Equipment(val name: String, val cost: Int, val damage: Int, val armor: Int)

private data class Stat(val player: Boolean, var hitPoints: Int, var damage: Int, var armor: Int)

private data class Equip(val cost: Int, val weaponIndex: Int, val armorIndex: Int?, val ringIndex1: Int?, val ringIndex2: Int?)