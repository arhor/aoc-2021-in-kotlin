package dev.arhor.aoc

fun main() {
    val result = ResourceReader.readInput("/Day01_1.txt") { lines ->
        lines.map(String::toInt)
             .windowed(2)
             .count { (a, b) -> b > a }
    }
    println("result: $result")
}
