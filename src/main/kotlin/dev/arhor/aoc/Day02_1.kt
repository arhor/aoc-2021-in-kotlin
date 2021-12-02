package dev.arhor.aoc

fun main() {
    val result = ResourceReader.readInput("/Day02_1.txt") { lines ->
        val (position, depth) = lines.map { it.split(" ") }
            .map { (command, value) -> command to value.toInt() }
            .fold(Pair(0, 0)) { (position, depth), (command, value) ->
                when (command) {
                    "up"      -> position to (depth - value)
                    "down"    -> position to (depth + value)
                    "forward" -> (position + value) to depth
                    else      -> position to depth
                }
            }

        position * depth
    }
    println("result: $result")
}
