package dev.arhor.aoc

import dev.arhor.aoc.ResourceReader.readInput

private val INSERTION_RULE_PATTERN = Regex("^([A-Z]{2}) -> ([A-Z])$")

fun main() {
    fun solvePuzzle1(input: Sequence<String>): Any? {
        var template = ""
        val rules = HashMap<String, String>()
        for ((index, line) in input.withIndex()) {
            when (index) {
                0 -> {
                    template = line
                }

                1 -> {
                    continue
                }

                else -> {
                    INSERTION_RULE_PATTERN.findAll(line).map { it.destructured }.forEach { (pair, insertion) ->
                        rules[pair] = insertion
                    }
                }
            }
        }
        repeat(10) {
            template = template.windowed(2).withIndex().joinToString(separator = "") { (i, pair) ->
                val rule = rules[pair]

                if (rule != null) {
                    if (i == 0) {
                        pair[0] + rule + pair[1]
                    } else {
                        rule + pair[1]
                    }
                } else {
                    pair
                }
            }
        }
        return template.groupingBy { it }.eachCount().values.let { it.max() - it.min() }
    }

    println("result 1: ${readInput("/Day14.txt", ::solvePuzzle1)}")
}
