package dev.arhor.aoc

fun main() {
    operator fun Pair<Int, Int>.plus(other: Pair<Int, Int>) = (first + other.first) to (second + other.second)

    fun handleCommand(command: String, value: Int): Pair<Int, Int> = when (command) {
        "forward" -> value to 0
        "up"      -> 0 to -value
        "down"    -> 0 to value
        else      -> 0 to 0
    }

    val result = ResourceReader.readInput("/Day02_1.txt") { lines ->
        val (position, depth) = lines.map { it.split(" ") }
            .map { (command, value) -> handleCommand(command, value.toInt()) }
            .reduce { prev, next -> prev + next }

        position * depth
    }
    println("result: $result")
}
