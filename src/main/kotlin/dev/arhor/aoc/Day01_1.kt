package dev.arhor.aoc

fun main() {
    ResourceReader.readInput("/Day01_1.txt") { lines ->
        var result = 0

        lines.map(String::toInt).reduce { prev, next ->
            if (next > prev) {
                result++
            }
            next
        }

        println("result: $result")
    }
}
