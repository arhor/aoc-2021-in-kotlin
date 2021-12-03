package dev.arhor.aoc

import dev.arhor.aoc.ResourceReader.readInput

fun main() {
    fun solvePuzzle1(input: Sequence<String>) = input.map(String::toInt)
        .windowed(2)
        .count { (a, b) -> b > a }

    fun solvePuzzle2(input: Sequence<String>) = input.map(String::toInt)
        .windowed(3) { (a, b ,c) -> a + b + c }
        .windowed(2)
        .count { (a, b) -> b > a }

    println("result 1: ${readInput("/Day01.txt", ::solvePuzzle1)}")
    println("result 2: ${readInput("/Day01.txt", ::solvePuzzle2)}")
}
