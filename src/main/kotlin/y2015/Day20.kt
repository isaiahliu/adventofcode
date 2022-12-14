package y2015

import input

fun main() {
    val primes = arrayListOf<Int>()
    fun nextPrime(num: Int): Int {
        val next = primes.firstOrNull { it > num }

        if (next != null) {
            return next
        }

        var temp = num + 1
        while (true) {
            var prime = true
            for (f in 2..temp / 2) {
                if (temp % f == 0) {
                    prime = false

                    break
                }
            }

            if (prime) {
                return temp.also { primes += temp }
            } else {
                temp++
            }
        }
    }

    val target = input.first().toInt()

    var min1 = Long.MAX_VALUE
    var min2 = Long.MAX_VALUE

    fun process1(num: Long, minPrime: Int, factors: Set<Long>) {
        var prime = minPrime
        while (prime < 20) {
            val newFactors = hashSetOf<Long>()

            newFactors += factors
            newFactors += factors.map { it * prime }

            val newNum = num * prime
            if (newFactors.sum() * 10 < target) {
                process1(newNum, prime, newFactors)
                prime = nextPrime(prime)
            } else {
                min1 = min1.coerceAtMost(newNum)

                break
            }
        }
    }

    fun process2(num: Long, minPrime: Int, factors: Set<Long>) {
        var prime = minPrime
        while (prime < 100) {
            val newFactors = hashSetOf<Long>()

            newFactors += factors
            newFactors += factors.map { it * prime }

            val newNum = num * prime
            if (newFactors.filter { newNum / it <= 50 }.sum() * 11 < target) {
                process2(newNum, prime, newFactors)
                prime = nextPrime(prime)
            } else {
                min2 = min2.coerceAtMost(newNum)

                break
            }
        }
    }

    process1(1, 2, setOf(1))
    process2(1, 2, setOf(1))

    println(min1)
    println(min2)
}