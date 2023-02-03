package y2015

import util.input

fun main() {
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

    val initPlayer = Player(50, 500)
    val initBoss = Boss(bossHitPoints, bossDamage)

    val spells = listOf(MagicMissileSpell(), DrainSpell(), ShieldSpell(), PoisonSpell(), RechargeSpell())

    var result = Int.MAX_VALUE
    fun nextTurn(playerTurn: Boolean, player: Player, boss: Boss, hardMode: Boolean = false) {
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
                result = player.totalCost.coerceAtMost(result)
                return
            }
        }

        if (playerTurn) {
            for (spell in spells.filter { it.canCast(player, boss) }) {
                val playerCopy = player.copy()
                val bossCopy = boss.copy()

                spell.cast(playerCopy, bossCopy)

                if (bossCopy.hitPoint > 0) {
                    nextTurn(false, playerCopy, bossCopy, hardMode)
                } else {
                    result = playerCopy.totalCost.coerceAtMost(result)
                }
            }
        } else {
            var damage = boss.damage
            if (player.shieldTurns > 0) {
                damage -= 7
                damage = damage.coerceAtLeast(1)
            }

            player.hitPoint -= damage

            if (player.hitPoint > 0) {
                nextTurn(true, player, boss, hardMode)
            }
        }
    }

    nextTurn(true, initPlayer.copy(), initBoss.copy())
    val part1Result = result
    result = Int.MAX_VALUE

    nextTurn(true, initPlayer.copy(), initBoss.copy(), true)
    val part2Result = result

    println(part1Result)
    println(part2Result)
}

private data class Player(
    var hitPoint: Int,
    var mana: Int,
    var shieldTurns: Int = 0,
    var rechargeTurns: Int = 0,
    var totalCost: Int = 0
)

private data class Boss(var hitPoint: Int, val damage: Int, var poisonTurns: Int = 0)

private abstract class AbstractSpell(val cost: Int) {
    open fun canCast(player: Player, boss: Boss): Boolean {
        return player.mana >= cost
    }

    open fun cast(player: Player, boss: Boss) {
        player.mana -= cost
        player.totalCost += cost
    }
}

private class MagicMissileSpell : AbstractSpell(53) {
    override fun cast(player: Player, boss: Boss) {
        super.cast(player, boss)

        boss.hitPoint -= 4
    }
}

private class DrainSpell : AbstractSpell(73) {
    override fun cast(player: Player, boss: Boss) {
        super.cast(player, boss)

        player.hitPoint += 2
        boss.hitPoint -= 2
    }
}

private class ShieldSpell : AbstractSpell(113) {
    override fun cast(player: Player, boss: Boss) {
        super.cast(player, boss)

        player.shieldTurns += 6
    }

    override fun canCast(player: Player, boss: Boss): Boolean {
        return super.canCast(player, boss) && player.shieldTurns == 0
    }
}

private class PoisonSpell : AbstractSpell(173) {
    override fun cast(player: Player, boss: Boss) {
        super.cast(player, boss)

        boss.poisonTurns += 6
    }

    override fun canCast(player: Player, boss: Boss): Boolean {
        return super.canCast(player, boss) && boss.poisonTurns == 0
    }
}

private class RechargeSpell : AbstractSpell(229) {
    override fun cast(player: Player, boss: Boss) {
        super.cast(player, boss)

        player.rechargeTurns += 5
    }

    override fun canCast(player: Player, boss: Boss): Boolean {
        return super.canCast(player, boss) && player.rechargeTurns == 0
    }
}
