package dev.arhor.aoc

fun main() {
    ResourceReader.readInput("/Day01_1.txt") {
        var result = 0
        it.map(String::toInt).reduce { prev, next ->
            if (next > prev) {
                result++
            }
            next
        }
        println("result: $result")
    }
}
