package dev.arhor.aoc

import dev.arhor.aoc.ResourceReader.readInput

fun main() {
    fun calculateFishProduction(currFishTimer: Int, daysTotal: Int, cache: MutableMap<Int, Long> = HashMap()): Long {
        if (daysTotal <= currFishTimer) {
            return 0
        }
        val daysLeft = daysTotal - (currFishTimer + 1)
        val currFishChildren = daysLeft / 7 + 1L
        return (daysLeft downTo 1 step 7).fold(currFishChildren) { count, date ->
            count + (cache[date] ?: calculateFishProduction(8, date, cache).also { cache[date] = it })
        }
    }

    fun solvePuzzleGeneric(input: Sequence<String>, days: Int): Long {
        return input.first().split(",").map(String::toInt).fold(0L) { acc, initial ->
            acc + calculateFishProduction(initial, days) + 1L
        }
    }

    fun solvePuzzle1(input: Sequence<String>) = solvePuzzleGeneric(input, 80)

    fun solvePuzzle2(input: Sequence<String>) = solvePuzzleGeneric(input, 256)

    println("result 1: ${readInput("/Day06.txt", ::solvePuzzle1)}")
    println("result 2: ${readInput("/Day06.txt", ::solvePuzzle2)}")
}
