package dev.arhor.aoc

import dev.arhor.aoc.ResourceReader.readInput
import kotlin.math.abs

fun main() {
    fun solvePuzzleGeneric(input: Sequence<String>, calcCost: (steps: Int) -> Int): Int {
        val crabs = input.first().split(",").map(String::toInt)
        val statistics = crabs.stream().mapToInt { it }.summaryStatistics()
        val sums = ArrayList<Int>()

        for (position in statistics.min..statistics.max) {
            var sum = 0
            for (crab in crabs) {
                sum += calcCost(abs(position - crab))
            }
            sums.add(sum)
        }
        return sums.minOrNull() ?: 0
    }

    fun solvePuzzle1(input: Sequence<String>) = solvePuzzleGeneric(input) { steps -> steps }

    fun solvePuzzle2(input: Sequence<String>) = solvePuzzleGeneric(input, caching { steps -> (0..steps).sum() })

    println("result 1: ${readInput("/Day07.txt", ::solvePuzzle1)}")
    println("result 2: ${readInput("/Day07.txt", ::solvePuzzle2)}")
}
