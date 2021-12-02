package dev.arhor.aoc

fun main() {
    val result = ResourceReader.readInput("/Day02_1.txt") { lines ->
        var position = 0
        var depth = 0
        var aim = 0

        lines.map { it.split(" ") }.forEach { (command, value) ->
            when (command) {
                "up"      -> aim -= value.toInt()
                "down"    -> aim += value.toInt()
                "forward" -> {
                    position += value.toInt()
                    depth += aim * value.toInt()
                }
            }
        }
        position * depth
    }
    println("result: $result")
}
