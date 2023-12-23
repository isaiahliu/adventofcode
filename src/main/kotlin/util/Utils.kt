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

data class Results<T1, T2>(var part1Result: T1, var part2Result: T2)

fun <T1, T2> expect(defaultValue1: T1, defaultValue2: T2, dsl: Results<T1, T2>.() -> Unit) {
    val results = Results(defaultValue1, defaultValue2)
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

fun <T> expect(defaultValue: T, dsl: Results<T, T>.() -> Unit) {
    return expect(defaultValue, defaultValue, dsl)
}
