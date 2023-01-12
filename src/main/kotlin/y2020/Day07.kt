package y2020

import util.input

fun main() {
    val bags = hashMapOf<String, Map<String, Int>>()
    input.map { it.split(" ") }.forEach {
        val bag = "${it[0]} ${it[1]}"

        val containBags = hashMapOf<String, Int>()

        var readingName = false
        var name = ""
        var count = 0
        it.drop(4).forEach { text ->
            if (readingName) {
                if (text.startsWith("bag")) {
                    count.takeIf { it > 0 }?.also {
                        containBags[name.trim()] = it
                    }

                    readingName = false
                    name = ""
                    count = 0
                } else {
                    name += " $text"
                }
            } else {
                count = text.toIntOrNull() ?: 0
                readingName = true
            }
        }

        bags[bag] = containBags
    }

    var t = setOf("shiny gold")

    val result = hashSetOf<String>()
    while (t.isNotEmpty()) {
        val t2 = bags.filter { bag ->
            t.any {
                it in bag.value.keys
            }
        }.keys

        t = t2 - result

        result += t2
    }

    println(result.size)

    var result2 = -1

    var tasks = mapOf("shiny gold" to 1)
    while (tasks.isNotEmpty()) {
        tasks = buildMap {
            tasks.forEach { (bag, count) ->
                result2 += count

                bags[bag].orEmpty().forEach { (contain, containCount) ->
                    this[contain] = (this[contain] ?: 0) + count * containCount
                }
            }
        }
    }

    println(result2)
}

