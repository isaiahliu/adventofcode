package y2022

import util.input

fun main() {
    val blueprints = arrayListOf<Day19Blueprint>()

    input.map { it.split(" ").mapNotNull { it.toIntOrNull() } }.forEach { usage ->
        blueprints += Day19Blueprint().also {
            it.oreRobot.ore = usage[0]
            it.clayRobot.ore = usage[1]
            it.obsidianRobot.ore = usage[2]
            it.obsidianRobot.clay = usage[3]
            it.geodeRobot.ore = usage[4]
            it.geodeRobot.obsidian = usage[5]
        }
    }

    fun walk(minutes: Int, blueprint: Day19Blueprint, robot: Day19Context, materials: Day19Context, target: Int, idleMinutes: Int = 0): Int {
        if (minutes == 0) {
            return materials.geode
        }

        var enough = false
        when (target) {
            //ore
            0 -> {
                enough = materials.enoughFor(blueprint.oreRobot) && !materials.enoughFor(blueprint.oreRobot * 2)
            }

            //clay
            1 -> {
                enough = materials.enoughFor(blueprint.clayRobot) && !materials.enoughFor(blueprint.clayRobot * 2)
            }

            //obsidian
            2 -> {
                enough = materials.enoughFor(blueprint.obsidianRobot) && !materials.enoughFor(blueprint.obsidianRobot * 2)
            }

            //geode
            3 -> {
                enough = materials.enoughFor(blueprint.geodeRobot) && !materials.enoughFor(blueprint.geodeRobot * 2)
            }
        }

        val results = arrayListOf<Int>()
        if (enough) {
            when (target) {
                0 -> {
                    results += walk(minutes - 1, blueprint, robot.copy(ore = robot.ore + 1), materials - blueprint.oreRobot + robot, 0)
                    results += walk(minutes - 1, blueprint, robot.copy(ore = robot.ore + 1), materials - blueprint.oreRobot + robot, 1)
                    results += walk(minutes - 1, blueprint, robot.copy(ore = robot.ore + 1), materials - blueprint.oreRobot + robot, 2)
                    results += walk(minutes - 1, blueprint, robot.copy(ore = robot.ore + 1), materials - blueprint.oreRobot + robot, 3)
                }

                1 -> {
                    results += walk(minutes - 1, blueprint, robot.copy(clay = robot.clay + 1), materials - blueprint.clayRobot + robot, 0)
                    results += walk(minutes - 1, blueprint, robot.copy(clay = robot.clay + 1), materials - blueprint.clayRobot + robot, 1)
                    results += walk(minutes - 1, blueprint, robot.copy(clay = robot.clay + 1), materials - blueprint.clayRobot + robot, 2)
                    results += walk(minutes - 1, blueprint, robot.copy(clay = robot.clay + 1), materials - blueprint.clayRobot + robot, 3)
                }

                2 -> {
                    results += walk(minutes - 1, blueprint, robot.copy(obsidian = robot.obsidian + 1), materials - blueprint.obsidianRobot + robot, 0)
                    results += walk(minutes - 1, blueprint, robot.copy(obsidian = robot.obsidian + 1), materials - blueprint.obsidianRobot + robot, 1)
                    results += walk(minutes - 1, blueprint, robot.copy(obsidian = robot.obsidian + 1), materials - blueprint.obsidianRobot + robot, 2)
                    results += walk(minutes - 1, blueprint, robot.copy(obsidian = robot.obsidian + 1), materials - blueprint.obsidianRobot + robot, 3)
                }

                3 -> {
                    results += walk(minutes - 1, blueprint, robot.copy(geode = robot.geode + 1), materials - blueprint.geodeRobot + robot, 0)
                    results += walk(minutes - 1, blueprint, robot.copy(geode = robot.geode + 1), materials - blueprint.geodeRobot + robot, 1)
                    results += walk(minutes - 1, blueprint, robot.copy(geode = robot.geode + 1), materials - blueprint.geodeRobot + robot, 2)
                    results += walk(minutes - 1, blueprint, robot.copy(geode = robot.geode + 1), materials - blueprint.geodeRobot + robot, 3)
                }
            }
        } else {
            results += walk(minutes - 1, blueprint, robot, materials + robot, target, idleMinutes + 1)
        }

        return results.maxOrNull() ?: 0
    }

    fun process(minutes: Int, size: Int): List<Pair<Int, Int>> {
        return blueprints.take(size).mapIndexed { index, blueprint ->
            (index + 1) to listOf(
                    walk(minutes, blueprint, Day19Context(ore = 1), Day19Context(), 0),
                    walk(minutes, blueprint, Day19Context(ore = 1), Day19Context(), 1),
                    walk(minutes, blueprint, Day19Context(ore = 1), Day19Context(), 2),
                    walk(minutes, blueprint, Day19Context(ore = 1), Day19Context(), 3)).max()
        }
    }

    println(process(24, blueprints.size).sumOf { it.first * it.second })
    println(process(32, 3).fold(1) { a, b -> a * b.second })
}

private data class Day19Context(var ore: Int = 0, var clay: Int = 0, var obsidian: Int = 0, var geode: Int = 0) {
    operator fun plusAssign(robot: Day19Context) {
        ore += robot.ore
        clay += robot.clay
        obsidian += robot.obsidian
        geode += robot.geode
    }

    fun enoughFor(target: Day19Context): Boolean {
        return ore >= target.ore && clay >= target.clay && obsidian >= target.obsidian && geode >= target.geode
    }

    operator fun minus(cost: Day19Context): Day19Context {
        val result = copy()
        result.ore -= cost.ore
        result.clay -= cost.clay
        result.obsidian -= cost.obsidian
        result.geode -= cost.geode

        return result
    }

    operator fun plus(cost: Day19Context): Day19Context {
        val result = copy()
        result.ore += cost.ore
        result.clay += cost.clay
        result.obsidian += cost.obsidian
        result.geode += cost.geode

        return result
    }

    operator fun times(t: Int): Day19Context {
        val result = copy()
        result.ore *= t
        result.clay *= t
        result.obsidian *= t
        result.geode *= t

        return result
    }
}

private class Day19Blueprint {
    val oreRobot = Day19Context()
    val clayRobot = Day19Context()
    val obsidianRobot = Day19Context()
    val geodeRobot = Day19Context()
}