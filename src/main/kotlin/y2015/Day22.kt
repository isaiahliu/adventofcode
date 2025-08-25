package y2015

import util.expect
import util.input

fun main() {
    expect(0, 0) {
        data class Player(var hitPoint: Int, var mana: Int, var shieldTurns: Int = 0, var rechargeTurns: Int = 0, var totalCost: Int = 0)
        data class Boss(var hitPoint: Int, val damage: Int, var poisonTurns: Int = 0)

        abstract class AbstractSpell(val cost: Int) {
            open fun canCast(player: Player, boss: Boss): Boolean {
                return player.mana >= cost
            }

            open fun cast(player: Player, boss: Boss) {
                player.mana -= cost
                player.totalCost += cost
            }
        }

        class MagicMissileSpell : AbstractSpell(53) {
            override fun cast(player: Player, boss: Boss) {
                super.cast(player, boss)

                boss.hitPoint -= 4
            }
        }

        class DrainSpell : AbstractSpell(73) {
            override fun cast(player: Player, boss: Boss) {
                super.cast(player, boss)

                player.hitPoint += 2
                boss.hitPoint -= 2
            }
        }

        class ShieldSpell : AbstractSpell(113) {
            override fun cast(player: Player, boss: Boss) {
                super.cast(player, boss)

                player.shieldTurns += 6
            }

            override fun canCast(player: Player, boss: Boss): Boolean {
                return super.canCast(player, boss) && player.shieldTurns == 0
            }
        }

        class PoisonSpell : AbstractSpell(173) {
            override fun cast(player: Player, boss: Boss) {
                super.cast(player, boss)

                boss.poisonTurns += 6
            }

            override fun canCast(player: Player, boss: Boss): Boolean {
                return super.canCast(player, boss) && boss.poisonTurns == 0
            }
        }

        class RechargeSpell : AbstractSpell(229) {
            override fun cast(player: Player, boss: Boss) {
                super.cast(player, boss)

                player.rechargeTurns += 5
            }

            override fun canCast(player: Player, boss: Boss): Boolean {
                return super.canCast(player, boss) && player.rechargeTurns == 0
            }
        }

        var bossHitPoints = 0
        var bossDamage = 0

        input.forEach {
            val (key, value) = it.split(": ")

            value.toIntOrNull()?.also {
                when (key) {
                    "Hit Points" -> bossHitPoints = it
                    "Damage" -> bossDamage = it
                }
            }
        }

        val spells = arrayOf(MagicMissileSpell(), DrainSpell(), ShieldSpell(), PoisonSpell(), RechargeSpell())

        fun battle(hardMode: Boolean): Int {
            var result = Int.MAX_VALUE
            fun dfs(playerTurn: Boolean, player: Player, boss: Boss) {
                if (player.totalCost >= result) {
                    return
                }

                if (hardMode && playerTurn) {
                    player.hitPoint--

                    if (player.hitPoint <= 0) {
                        return
                    }
                }

                if (player.rechargeTurns > 0) {
                    player.mana += 101
                    player.rechargeTurns--
                }

                if (player.shieldTurns > 0) {
                    player.shieldTurns--
                }

                if (boss.poisonTurns > 0) {
                    boss.hitPoint -= 3
                    boss.poisonTurns--

                    if (boss.hitPoint <= 0) {
                        result = minOf(result, player.totalCost)
                        return
                    }
                }

                if (playerTurn) {
                    for (spell in spells) {
                        if (spell.canCast(player, boss)) {
                            val playerCopy = player.copy()
                            val bossCopy = boss.copy()

                            spell.cast(playerCopy, bossCopy)

                            if (bossCopy.hitPoint > 0) {
                                dfs(false, playerCopy, bossCopy)
                            } else {
                                result = minOf(result, playerCopy.totalCost)
                            }
                        }
                    }
                } else {
                    var damage = boss.damage
                    if (player.shieldTurns > 0) {
                        damage = maxOf(damage - 7, 1)
                    }

                    player.hitPoint -= damage

                    if (player.hitPoint > 0) {
                        dfs(true, player, boss)
                    }
                }
            }

            dfs(true, Player(50, 500), Boss(bossHitPoints, bossDamage))

            return result
        }

        part1Result = battle(false)
        part2Result = battle(true)
    }
}
