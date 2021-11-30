package dev.arhor.aoc

object ResourceReader {

    fun readInput(name: String): List<String> {
        val resourceName = if (name.startsWith("/")) name else "/$name"
        return this::class.java.getResourceAsStream(resourceName)
            ?.bufferedReader()
            ?.readLines()
            ?: emptyList()
    }
}