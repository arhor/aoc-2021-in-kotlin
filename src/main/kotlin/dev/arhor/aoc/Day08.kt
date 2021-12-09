package dev.arhor.aoc

import dev.arhor.aoc.ResourceReader.readInput

fun main() {
    fun determineMappings(signals: List<String>): Map<String, Int> {
        val pattern1 = signals.first { it.length == 2 }
        val pattern4 = signals.first { it.length == 4 }
        val pattern7 = signals.first { it.length == 3 }
        val pattern8 = signals.first { it.length == 7 }

        val segmentA = pattern7.filterNot(pattern1::contains)

        val tempA4 = pattern4 + segmentA

        val pattern9 = signals.first { it.length == 6 && it.filterNot(tempA4::contains).length == 1 }

        val segmentE = pattern8.filterNot(pattern9::contains)

        val (pattern0, pattern6) = signals.filter { it.length == 6 && it != pattern9 }.let { (a, b) ->
            if ((a + pattern1).toSet() != pattern8.toSet()) {
                a to b
            } else {
                b to a
            }
        }

        val pattern2 = signals.first { it.length == 5 && it.contains(segmentE) }

        val (pattern3, pattern5) = signals.filter { it.length == 5 && it != pattern2 }.let { (a, b) ->
            if (pattern1.all(a::contains)) {
                a to b
            } else {
                b to a
            }
        }

        return buildMap {
            put(pattern0.toList().sorted().joinToString(""), 0)
            put(pattern1.toList().sorted().joinToString(""), 1)
            put(pattern2.toList().sorted().joinToString(""), 2)
            put(pattern3.toList().sorted().joinToString(""), 3)
            put(pattern4.toList().sorted().joinToString(""), 4)
            put(pattern5.toList().sorted().joinToString(""), 5)
            put(pattern6.toList().sorted().joinToString(""), 6)
            put(pattern7.toList().sorted().joinToString(""), 7)
            put(pattern8.toList().sorted().joinToString(""), 8)
            put(pattern9.toList().sorted().joinToString(""), 9)
        }
    }

    data class DisplayEntry(val signals: List<String>, val numbers: List<String>)

    fun parseInput(input: Sequence<String>) = input.map { line ->
        line.split("|")
            .map(String::trim)
            .map { values -> values.split(" ").filter(String::isNotBlank) }
            .let { (signals, numbers) -> DisplayEntry(signals, numbers) }
    }

    fun solvePuzzle1(input: Sequence<String>) = parseInput(input).map { (_, numbers) ->
        numbers.count { it.length in setOf(2, 3, 4, 7) }
    }.sum()

    fun solvePuzzle2(input: Sequence<String>) = parseInput(input).map { (signals, numbers) ->
        val mappings = determineMappings(signals)
        val strings = numbers.map { it.toList().sorted().joinToString("") }
        strings.map { mappings[it] }.joinToString("").toInt()
    }.sum()

    println("result 1: ${readInput("/Day08.txt", ::solvePuzzle1)}")
    println("result 2: ${readInput("/Day08.txt", ::solvePuzzle2)}")
}
