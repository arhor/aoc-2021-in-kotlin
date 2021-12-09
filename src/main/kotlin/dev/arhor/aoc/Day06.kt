package dev.arhor.aoc

import dev.arhor.aoc.ResourceReader.readInput

fun main() {
    fun calculateFishProduction(currFishTimer: Int, days: Int, cache: MutableMap<Int, Long> = HashMap()): Long {
        val firstBornDate = currFishTimer + 1
        if (days < firstBornDate) {
            return 0
        }
        val currFishChildren = (days - firstBornDate) / 7 + 1L
        return (firstBornDate until days step 7).fold(currFishChildren) { count, date ->
            val key = days - date
            count + (cache[key] ?: calculateFishProduction(8, key, cache).also { cache[key] = it })
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
