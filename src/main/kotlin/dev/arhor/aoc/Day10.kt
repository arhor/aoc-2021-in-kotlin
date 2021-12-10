package dev.arhor.aoc

import dev.arhor.aoc.ResourceReader.readInput
import java.util.*

fun main() {
    val mappings = mapOf(
        '(' to ')',
        '[' to ']',
        '{' to '}',
        '<' to '>',
    )

    fun calculateScores(input: Sequence<String>, onIncorrect: (Char) -> Long? = { null }, onMissing: (Collection<Char>) -> Long = { 0 }, ): List<Long> {
        val scores = ArrayList<Long?>()
        outer@ for (line in input) {
            val braces = LinkedList<Char>()
            for (brace in line) {
                when (brace) {
                    in mappings.keys -> {
                        braces.push(brace)
                    }
                    in mappings.values -> {
                        if (brace != mappings[braces.pop()]) {
                            scores += onIncorrect(brace)
                            continue@outer
                        }
                    }
                }
            }
            scores += braces.mapNotNull(mappings::get).let(onMissing)
        }
        return scores.filterNotNull()
    }

    fun solvePuzzle1(input: Sequence<String>) = calculateScores(input, onIncorrect = {
        when (it) {
            ')'  -> 3
            ']'  -> 57
            '}'  -> 1197
            '>'  -> 25137
            else -> 0
        }
    }).sum()

    fun solvePuzzle2(input: Sequence<String>) = calculateScores(input, onMissing = {
        it.fold(0) { score, brace ->
            (score * 5) + when (brace) {
                ')'  -> 1
                ']'  -> 2
                '}'  -> 3
                '>'  -> 4
                else -> 0
            }
        }
    }).sorted().let { it[it.size / 2] }

    println("result 1: ${readInput("/Day10.txt", ::solvePuzzle1)}")
    println("result 2: ${readInput("/Day10.txt", ::solvePuzzle2)}")
}
