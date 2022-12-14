package y2016

import util.input
import util.md5

fun main() {
    val prefix = input.first()

    val password1 = StringBuilder()
    val password2 = CharArray(8)

    var currentNum = 0
    var passwordDone = false
    val passwordPos = hashSetOf<Int>()
    while (!passwordDone) {
        while (true) {
            val md5 = (prefix + currentNum).md5
            if (md5.startsWith("00000")) {
                if (password1.length < 8) {
                    password1.append(md5[5])
                }

                (md5[5] - '0').takeIf { it in (0 until 8) }?.takeIf { passwordPos.add(it) }?.also {
                    password2[it] = md5[6]
                }
                passwordDone = passwordPos.size == 8
                currentNum++
                break
            } else {
                currentNum++
            }
        }
    }

    println(password1.toString())
    println(String(password2))
}