package y2020

import util.input

fun main() {
    var passport = hashMapOf<String, String>()

    val passports = arrayListOf<Map<String, String>>(passport)

    input.map { it.split(" ").filter { it.isNotBlank() }.map { it.split(":") } }.forEach {
        if (it.isEmpty()) {
            passport = hashMapOf()
            passports += passport
        }

        passport += it.map { it[0] to it[1] }
    }

    val requiredFields = arrayOf("byr", "iyr", "eyr", "hgt", "hcl", "ecl", "pid")

    println(passports.count { requiredFields.all { key -> key in it.keys } })

    fun valid(key: String, passport: Map<String, String>): Boolean {
        return passport[key]?.let {
            when (key) {
                "byr" -> {
                    it.takeIf { it.length == 4 }?.toIntOrNull()?.takeIf { it in 1920..2002 }
                }

                "iyr" -> {
                    it.takeIf { it.length == 4 }?.toIntOrNull()?.takeIf { it in 2010..2020 }
                }

                "eyr" -> {
                    it.takeIf { it.length == 4 }?.toIntOrNull()?.takeIf { it in 2020..2030 }
                }

                "hgt" -> {
                    it.takeIf { it.endsWith("cm") }?.dropLast(2)?.toIntOrNull()?.takeIf { it in 150..193 }
                        ?: it.takeIf { it.endsWith("in") }?.dropLast(2)?.toIntOrNull()?.takeIf { it in 59..76 }
                }

                "hcl" -> {
                    it.takeIf { it.length == 7 }?.takeIf { it[0] == '#' }
                        ?.takeIf { it.drop(1).all { it in '0'..'9' || it in 'a'..'f' } }
                }

                "ecl" -> {
                    it.takeIf { it in arrayOf("amb", "blu", "brn", "gry", "grn", "hzl", "oth") }
                }

                "pid" -> {
                    it.takeIf { it.length == 9 }?.takeIf { it.all { it in '0'..'9' } }
                }

                else -> null
            }
        } != null
    }

    println(passports.count { requiredFields.all { key -> valid(key, it) } })
}