package dev.arhor.aoc

import dev.arhor.aoc.ResourceReader.readInput
import java.util.stream.IntStream
import kotlin.math.abs

fun main() {
    fun solvePuzzleGeneric(input: Sequence<String>, calcCost: (steps: Int) -> Int): Int {
        val crabs = input.first().split(",").map(String::toInt).toIntArray()
        val positions = IntStream.of(*crabs).summaryStatistics().let { it.min..it.max }
        return positions.minOfOrNull { position -> crabs.sumOf { calcCost(abs(position - it)) } }
            ?: 0
    }

    fun solvePuzzle1(input: Sequence<String>) = solvePuzzleGeneric(input) { steps -> steps }

    fun solvePuzzle2(input: Sequence<String>) = solvePuzzleGeneric(input, caching { steps -> (0..steps).sum() })

    println("result 1: ${readInput("/Day07.txt", ::solvePuzzle1)}")
    println("result 2: ${readInput("/Day07.txt", ::solvePuzzle2)}")
}
