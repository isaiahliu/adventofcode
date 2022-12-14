package y2015

import util.input

fun main() {
    val rsexp = "\\w+: capacity (-?\\d+), durability (-?\\d+), flavor (-?\\d+), texture (-?\\d+), calories (-?\\d+)".toRegex()

    val ingredients = arrayListOf<Ingredient>()

    input.forEach {
        val match = rsexp.matchEntire(it) ?: return

        val capacity = match.groupValues[1].toInt()
        val durability = match.groupValues[2].toInt()
        val flavor = match.groupValues[3].toInt()
        val texture = match.groupValues[4].toInt()
        val calories = match.groupValues[5].toInt()

        ingredients += Ingredient(capacity, durability, flavor, texture, calories)
    }

    var part1Max = 0
    var part2Max = 0

    fun walk(index: Int) {
        val current = ingredients[index]
        if (index == ingredients.size - 1) {
            current.teaspoon = 100 - ingredients.take(index).sumOf { it.teaspoon }

            val capacity = ingredients.sumOf { it.capacity * it.teaspoon }.coerceAtLeast(0)
            val durability = ingredients.sumOf { it.durability * it.teaspoon }.coerceAtLeast(0)
            val flavor = ingredients.sumOf { it.flavor * it.teaspoon }.coerceAtLeast(0)
            val texture = ingredients.sumOf { it.texture * it.teaspoon }.coerceAtLeast(0)
            val calories = ingredients.sumOf { it.calories * it.teaspoon }.coerceAtLeast(0)

            part1Max = part1Max.coerceAtLeast(capacity * durability * flavor * texture)

            if (calories == 500) {
                part2Max = part2Max.coerceAtLeast(capacity * durability * flavor * texture)
            }
        } else {
            val currentRemaining = 100 - ingredients.take(index).sumOf { it.teaspoon }

            for (teaspoon in 0..currentRemaining) {
                current.teaspoon = teaspoon

                walk(index + 1)
            }
        }
    }

    walk(0)

    println(part1Max)
    println(part2Max)
}

private data class Ingredient(val capacity: Int, val durability: Int, val flavor: Int, val texture: Int, val calories: Int, var teaspoon: Int = 0)