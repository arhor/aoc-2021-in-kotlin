package dev.arhor.aoc

object ResourceReader {

    fun readInput(name: String, action: (Sequence<String>) -> Unit) {
        this::class.java.getResourceAsStream(name)?.bufferedReader()?.useLines(action)
    }
}