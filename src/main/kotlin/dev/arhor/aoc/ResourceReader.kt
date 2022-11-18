package dev.arhor.aoc

object ResourceReader {

    fun <T> readInput(name: String, action: (Sequence<String>) -> T): T {
        return this::class.java.getResourceAsStream(name)?.bufferedReader()?.useLines(action) ?: kotlin.run {
            println("Resource '$name' is not found: ${Thread.currentThread().stackTrace[2]}")
            action(emptySequence())
        }
    }
}