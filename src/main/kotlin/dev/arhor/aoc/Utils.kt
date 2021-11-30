package dev.arhor.aoc

import java.math.BigInteger
import java.security.MessageDigest

/**
 * Converts string to dev.arhor.aoc.md5 hash.
 */
fun String.md5(): String {
    val messageDigest = MessageDigest.getInstance("MD5")
    val bytes = toByteArray()
    return BigInteger(1, messageDigest.digest(bytes)).toString(16)
}
