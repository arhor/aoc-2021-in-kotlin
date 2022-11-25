package dev.arhor.aoc

import dev.arhor.aoc.ResourceReader.readInput

private val INSERTION_RULE_PATTERN = Regex("^([A-Z]{2}) -> ([A-Z])$")

fun main() {
    println("result 1: ${readInput("/Day14.txt", ::solvePuzzle1)}")
}

fun solvePuzzle1(input: Sequence<String>): Any {
    var template = ""
    val insertions = HashMap<String, Array<String>>()

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
                    insertions[pair] = arrayOf(pair[0] + insertion, insertion + pair[1])
                }
            }
        }
    }

    var values = template.windowed(2).groupingBy { it }.eachCount().mapValues { it.value.toLong() }

    repeat(40) {
        values = transform(values, insertions)
    }
    return countChars(template[0], values).values.stream().mapToLong { it }.summaryStatistics().let { it.max - it.min }
}

private fun transform(record: Map<String, Long>, insertions: Map<String, Array<String>>): Map<String, Long> {
    val newRecord = HashMap<String, Long>()

    for ((key, countKey) in record) {
        val insertion = insertions[key]

        if (insertion != null) {
            val (insert1, insert2) = insertion

            val count1 = newRecord.getOrDefault(insert1, 0L)
            val count2 = newRecord.getOrDefault(insert2, 0L)

            newRecord[insert1] = count1 + countKey
            newRecord[insert2] = count2 + countKey
        } else {
            newRecord[key] = countKey
        }
    }
    return newRecord
}

private fun countChars(first: Char, templateMap: Map<String, Long>): Map<Char, Long> {
    val charMap = HashMap<Char, Long>().withDefault { if (it == first) 1 else 0 }

    for ((pair, count) in templateMap) {
        val char = pair[1]
        val numb = charMap.getValue(char)

        charMap[char] = numb + count
    }
    return charMap
}
