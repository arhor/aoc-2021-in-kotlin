package dev.arhor.aoc

import dev.arhor.aoc.ResourceReader.readInput

private const val SEPARATOR = "-"

private const val PATH_ALPHA = "start"
private const val PATH_OMEGA = "end"

private const val SMALL_CAVES_THRESHOLD = 2

private val SMALL_CAVE_PATTERN = Regex("[a-z]+")
private val LARGE_CAVE_PATTERN = Regex("[A-Z]+")

fun main() {
    fun solvePuzzle1(input: Sequence<String>) =
        CavesModel(input)
            .computeUniquePaths(smallCaveRule = ::allowSmallCaveToBeVisitedOnce)
            .count()

    fun solvePuzzle2(input: Sequence<String>) =
        CavesModel(input)
            .computeUniquePaths(smallCaveRule = ::allowFirstSmallCaveToBeVisitedTwice)
            .count()

    println("result 1: ${readInput("/Day12.txt", ::solvePuzzle1)}")
    println("result 2: ${readInput("/Day12.txt", ::solvePuzzle2)}")
}

class CavesModel(input: Sequence<String>) {
    private val data = computeRelations(input)

    fun computeUniquePaths(
        node: String = PATH_ALPHA,
        path: List<String> = listOf(node),
        smallCaveRule: (String, List<String>) -> Boolean,
    ): List<List<String>> {
        val directions = data[node]
        val result = ArrayList<List<String>>()

        for (direction in directions!!) {
            when (determineCaveType(direction)) {
                Type.ALPHA -> {
                    continue
                }

                Type.OMEGA -> {
                    result += (path + direction)
                }

                Type.SMALL -> {
                    if (smallCaveRule(direction, path)) {
                        result += computeUniquePaths(direction, path + direction, smallCaveRule)
                    }
                }

                Type.LARGE -> {
                    result += computeUniquePaths(direction, path + direction, smallCaveRule)
                }
            }
        }
        return result
    }

}

enum class Type {
    ALPHA, OMEGA, SMALL, LARGE
}

fun link(data: MutableMap<String, MutableList<String>>, a: String, b: String) {
    data.computeIfAbsent(a) { ArrayList() } += b
}

fun computeRelations(input: Sequence<String>): Map<String, List<String>> {
    val data = HashMap<String, MutableList<String>>()
    for ((from, to) in input.map { it.split(SEPARATOR) }) {
        link(data, from, to)
        link(data, to, from)
    }
    return data
}

fun determineCaveType(input: String): Type {
    return when {
        input == PATH_ALPHA -> Type.ALPHA
        input == PATH_OMEGA -> Type.OMEGA
        input.matches(SMALL_CAVE_PATTERN) -> Type.SMALL
        input.matches(LARGE_CAVE_PATTERN) -> Type.LARGE
        else -> throw IllegalArgumentException("Unsupported cave type: $input")
    }
}

fun allowSmallCaveToBeVisitedOnce(direction: String, path: List<String>): Boolean {
    return direction !in path
}

fun allowFirstSmallCaveToBeVisitedTwice(direction: String, path: List<String>): Boolean {
    val count = path.groupingBy { it }.eachCount()
    val value = count[direction]

    return if (value == null) {
        true
    } else {
        (value < SMALL_CAVES_THRESHOLD) && hasNoMoreDuplicates(count, direction)
    }
}

fun hasNoMoreDuplicates(data: Map<String, Int>, direction: String): Boolean =
    data.filterValues { it >= SMALL_CAVES_THRESHOLD }
        .filterKeys { it != direction && determineCaveType(it) == Type.SMALL }
        .isEmpty()
