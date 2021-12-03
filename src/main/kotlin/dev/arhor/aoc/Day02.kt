package dev.arhor.aoc

import dev.arhor.aoc.ResourceReader.readInput

fun main() {
    fun solvePuzzle1(input: Sequence<String>) = input.map { it.split(" ") }
        .map { (command, value) -> command to value.toInt() }
        .fold(Pair(0, 0)) { (position, depth), (command, value) ->
            when (command) {
                "up"      -> position to (depth - value)
                "down"    -> position to (depth + value)
                "forward" -> (position + value) to depth
                else      -> position to depth
            }
        }
        .let { (position, depth) -> position * depth }

    fun solvePuzzle2(input: Sequence<String>) = input.map { it.split(" ") }
        .map { (command, value) -> command to value.toInt() }
        .fold(Triple(0, 0, 0)) { (position, depth, aim), (command, value) ->
            when (command) {
                "up"      -> Triple(position, depth, (aim - value))
                "down"    -> Triple(position, depth, (aim + value))
                "forward" -> Triple((position + value), (depth + (aim * value)), aim)
                else      -> Triple(position, depth, aim)
            }
        }
        .let { (position, depth) -> position * depth }

    println("result 1: ${readInput("/Day02.txt", ::solvePuzzle1)}")
    println("result 2: ${readInput("/Day02.txt", ::solvePuzzle2)}")
}
