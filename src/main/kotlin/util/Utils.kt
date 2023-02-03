package util

import java.io.File
import java.math.BigInteger
import java.security.MessageDigest

var useSample = false

/**
 * Reads lines from the given input txt file.
 */
val input by lazy {
    RuntimeException().stackTrace.firstOrNull { it.methodName == "main" }?.className?.removeSuffix("Kt")
        ?.replace('.', '/')
        ?.let { ClassLoader.getSystemResource("${it}${if (useSample) "-sample" else ""}.txt") }?.file?.let { File(it) }
        ?.readLines()
        .orEmpty()
}

/**
 * Converts string to util.getMd5 hash.
 */
val String.md5
    get() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
        .toString(16)
        .padStart(32, '0')
