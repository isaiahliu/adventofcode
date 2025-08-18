package y2015

import util.expect
import util.input

fun main() {
    expect(0, 0) {
        val rsexp =
            "\\w+: capacity (-?\\d+), durability (-?\\d+), flavor (-?\\d+), texture (-?\\d+), calories (-?\\d+)".toRegex()

        data class Ingredient(
            val capacity: Int,
            val durability: Int,
            val flavor: Int,
            val texture: Int,
            val calories: Int,
            var teaspoon: Int = 0
        )

        val ingredients = arrayListOf<Ingredient>()

        input.forEach {
            val match = rsexp.matchEntire(it) ?: return@forEach

            val capacity = match.groupValues[1].toInt()
            val durability = match.groupValues[2].toInt()
            val flavor = match.groupValues[3].toInt()
            val texture = match.groupValues[4].toInt()
            val calories = match.groupValues[5].toInt()

            ingredients += Ingredient(capacity, durability, flavor, texture, calories)
        }

        fun dfs(index: Int) {
            val current = ingredients[index]
            val currentRemaining = 100 - ingredients.take(index).sumOf { it.teaspoon }

            if (index == ingredients.lastIndex) {
                current.teaspoon = currentRemaining

                val capacity = maxOf(ingredients.sumOf { it.capacity * it.teaspoon }, 0)
                val durability = maxOf(ingredients.sumOf { it.durability * it.teaspoon }, 0)
                val flavor = maxOf(ingredients.sumOf { it.flavor * it.teaspoon }, 0)
                val texture = maxOf(ingredients.sumOf { it.texture * it.teaspoon }, 0)
                val calories = maxOf(ingredients.sumOf { it.calories * it.teaspoon }, 0)

                val score = capacity * durability * flavor * texture

                part1Result = maxOf(part1Result, score)

                if (calories == 500) {
                    part2Result = maxOf(part2Result, score)
                }
            } else {
                for (teaspoon in 0..currentRemaining) {
                    current.teaspoon = teaspoon

                    dfs(index + 1)
                }
            }
        }

        dfs(0)
    }
}

