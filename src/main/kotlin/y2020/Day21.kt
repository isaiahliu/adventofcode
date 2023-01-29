package y2020

import util.input

fun main() {
    val regex = "(.*) \\(contains (.*)\\)".toRegex()
    val map = hashMapOf<String, MutableSet<String>>()
    val allIngredients = arrayListOf<String>()
    input.mapNotNull {
        regex.matchEntire(it)?.groupValues?.drop(1)?.let {
            it[0].split(" ").toSet() to it[1].split(" ").map { it.trim(',') }
        }
    }.forEach { (ingredients, allergens) ->
        allIngredients += ingredients

        allergens.forEach { allergen ->
            map[allergen] = (map[allergen]?.intersect(ingredients) ?: ingredients).toMutableSet()
        }
    }

    val possible = map.values.flatten()

    allIngredients.removeIf { it in possible }

    println(allIngredients.size)

    val processed = hashSetOf<String>()

    while (true) {
        val found = map.filter { it.value.size == 1 && it.key !in processed }
        found.forEach { (key, value) ->
            processed += key

            map.forEach { (k, v) ->
                if (k != key) {
                    v -= value.first()
                }
            }
        }

        if (found.isEmpty()) {
            break
        }
    }
    println(map.entries.sortedWith(compareBy { it.key }).joinToString(",") { it.value.first() })
}