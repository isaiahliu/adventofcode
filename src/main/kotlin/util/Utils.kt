package util

import java.io.File
import java.math.BigInteger
import java.security.MessageDigest
import kotlin.system.measureTimeMillis

var useSample = false

/**
 * Reads lines from the given input txt file.
 */
val input by lazy {
    RuntimeException().stackTrace.firstOrNull { it.methodName == "main" }?.className?.removeSuffix("Kt")
        ?.replace('.', '/')
        ?.let { ClassLoader.getSystemResource("${it}${if (useSample) "-sample" else ""}.txt") }?.file?.let { File(it) }
        ?.readLines().orEmpty()
}

/**
 * Converts string to util.getMd5 hash.
 */
val String.md5
    get() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray())).toString(16).padStart(32, '0')

data class Results<T>(var part1Result: T, var part2Result: T)

private fun <T> expect(defaultValue: T, dsl: Results<T>.() -> Unit) {
    val results = Results(defaultValue, defaultValue)
    measureTimeMillis {
        results.dsl()
    }.also {
        println("Result1: ")
        println(results.part1Result.toString())
        println()
        println("Result2: ")
        println(results.part2Result.toString())
        println()
        println("Time cost: ${it}ms")
        println()
    }
}

fun expectInt(dsl: Results<Int>.() -> Unit) {
    expect(0, dsl)
}

fun expectLong(dsl: Results<Long>.() -> Unit) {
    expect(0L, dsl)
}

fun expectString(dsl: Results<String>.() -> Unit) {
    expect("", dsl)
}
