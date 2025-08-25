package y2015

import util.expect
import util.input

fun main() {
    data class Equipment(val name: String, val cost: Int, val damage: Int, val armor: Int)

    data class Character(var hitPoints: Int, var damage: Int, var armor: Int)

    class Equip(
        vararg equipments: Equipment?
    ) {
        val cost = equipments.sumOf { it?.cost ?: 0 }
        val damage = equipments.sumOf { it?.damage ?: 0 }
        val armor = equipments.sumOf { it?.armor ?: 0 }
    }
    expect(0, 0) {
        val weapons = listOf(
            Equipment("Dagger", 8, 4, 0),
            Equipment("Shortsword", 10, 5, 0),
            Equipment("Warhammer", 25, 6, 0),
            Equipment("Longsword", 40, 7, 0),
            Equipment("Greataxe", 74, 8, 0)
        )
        val armors = listOf(
            Equipment("Leather", 13, 0, 1),
            Equipment("Chainmail", 31, 0, 2),
            Equipment("Splintmail", 53, 0, 3),
            Equipment("Bandedmail", 75, 0, 4),
            Equipment("Platemail", 102, 0, 5)
        )
        val rings = listOf(
            Equipment("Damage +1", 25, 1, 0),
            Equipment("Damage +2", 50, 2, 0),
            Equipment("Damage +3", 100, 3, 0),
            Equipment("Defense +1", 20, 0, 1),
            Equipment("Defense +2", 40, 0, 2),
            Equipment("Defense +3", 80, 0, 3)
        )

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

                        equips += Equip(
                            weapons[weaponIndex],
                            armors.getOrNull(armorIndex),
                            rings.getOrNull(ringIndex1),
                            rings.getOrNull(ringIndex2)
                        )
                    }
                }
            }
        }

        fun Equip.battle(): Int {
            val characters = arrayOf(
                Character(100, this.damage, this.armor),
                Character(bossHitPoints, bossDamage, bossArmor)
            )

            var turn = 0

            while (true) {
                val attacker = characters[turn]
                val defender = characters[1 - turn]

                defender.hitPoints -= (attacker.damage - defender.armor).coerceAtLeast(1)

                if (defender.hitPoints <= 0) {
                    return turn
                }

                turn = 1 - turn
            }
        }

        equips.sortBy { it.cost }

        part1Result = equips.first { it.battle() == 0 }.cost
        part2Result = equips.last { it.battle() == 1 }.cost
    }
}
