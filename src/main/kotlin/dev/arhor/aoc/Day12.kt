package dev.arhor.aoc

import dev.arhor.aoc.ResourceReader.readInput

private const val SEPARATOR = "-"

private const val PATH_ALPHA = "start"
private const val PATH_OMEGA = "end"

private val SMALL_CAVE_PATTERN = Regex("[a-z]+")
private val LARGE_CAVE_PATTERN = Regex("[A-Z]+")

fun main() {
    fun solvePuzzle1(input: Sequence<String>): Number {
        val data = HashMap<String, MutableList<String>>()
        for ((from, to) in input.map { it.split(SEPARATOR) }) {
            data.link(from, to)
            data.link(to, from)
        }

        fun process(node: String = PATH_ALPHA, path: List<String> = listOf(node)): List<List<String>> {
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
                        if (direction !in path) {
                            result += process(direction, path + direction)
                        }
                    }

                    Type.LARGE -> {
                        result += process(direction, path + direction)
                    }
                }
            }
            return result
        }
        return process().count()
    }

    println("result 1: ${readInput("/Day12.txt", ::solvePuzzle1)}")
}

fun MutableMap<String, MutableList<String>>.link(a: String, b: String) {
    computeIfAbsent(a) { ArrayList() } += b
}

enum class Type {
    ALPHA, OMEGA, SMALL, LARGE
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
