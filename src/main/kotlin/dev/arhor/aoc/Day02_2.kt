package dev.arhor.aoc

fun main() {
    val result = ResourceReader.readInput("/Day02_1.txt") { lines ->
        val (position, depth) = lines.map { it.split(" ") }
            .map { (command, value) -> command to value.toInt() }
            .fold(Triple(0, 0, 0)) { (position, depth, aim), (command, value) ->
                when (command) {
                    "up"      -> Triple(position, depth, (aim - value))
                    "down"    -> Triple(position, depth, (aim + value))
                    "forward" -> Triple((position + value), (depth + (aim * value)), aim)
                    else      -> Triple(position, depth, aim)
                }
            }
        position * depth
    }
    println("result: $result")
}
