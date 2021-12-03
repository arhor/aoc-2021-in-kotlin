package dev.arhor.aoc

import dev.arhor.aoc.ResourceReader.readInput

fun main() {
    fun solvePuzzle1(input: Sequence<String>): Int {
        fun countBits(bitsCounter: MutableList<Int>, line: String): MutableList<Int> {
            for ((bitIndex, c) in line.withIndex()) {
                if (bitIndex >= bitsCounter.size) {
                    bitsCounter.add(0)
                }
                if (c.digitToInt() == 1) {
                    bitsCounter[bitIndex]++
                } else {
                    bitsCounter[bitIndex]--
                }
            }
            return bitsCounter
        }

        return input.fold(ArrayList(), ::countBits)
            .map { if (it > 0) "1" to "0" else "0" to "1" }
            .reduce { prev, next -> prev + next }
            .let { (gammaRate, epsilonRate) -> gammaRate.toInt(2) * epsilonRate.toInt(2) }
    }

    fun solvePuzzle2(input: Sequence<String>): Int {
        tailrec fun findRate(input: List<String>, comparator: (Int, Int) -> Boolean, bitIndex: Int = 0): Int {
            return if (input.size == 1) {
                input.first().toInt(2)
            } else {
                val filteredInput = input.partition { it[bitIndex] == '1' }.let { (leadOne, leadZero) ->
                    if (comparator(leadOne.size, leadZero.size)) {
                        leadOne
                    } else {
                        leadZero
                    }
                }
                findRate(filteredInput, comparator, bitIndex + 1)
            }
        }

        val lines = input.toList()

        val oxygenGeneratorRating = findRate(lines, { left, right -> left >= right })
        val co2ScrubberRating = findRate(lines, { left, right -> left < right })

        return oxygenGeneratorRating * co2ScrubberRating
    }

    println("result 1: ${readInput("/Day03.txt", ::solvePuzzle1)}")
    println("result 2: ${readInput("/Day03.txt", ::solvePuzzle2)}")
}
