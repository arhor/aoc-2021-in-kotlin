package dev.arhor.aoc

import dev.arhor.aoc.ResourceReader.readInput

private val COORDINATES_PATTERN = Regex("([0-9]+),([0-9]+)")
private val INSTRUCTION_PATTERN = Regex("fold along ([xy])=([0-9]+)")

fun main() {
    fun solvePuzzle1(input: Sequence<String>) = DataModel(input)

    println("result 1: ${readInput("/Day13.txt", ::solvePuzzle1)}")
}

private enum class Coordinate { X, Y }

private data class Folding(val coordinate: Coordinate, val value: Int)

@Suppress("VARIABLE_WITH_REDUNDANT_INITIALIZER")
private class DataModel(input: Sequence<String>) {

    private val coordinates: List<Point>
    private val instruction: List<Folding>

    init {
        val tempCoordinates = ArrayList<Point>()
        val tempInstruction = ArrayList<Folding>()
        for (line in input) {
            run { // allows to simulate 'continue' inside the 'let' function
                COORDINATES_PATTERN.find(line)?.let { result ->
                    tempCoordinates += result.destructured.let {
                        Point(
                            x = it.component1().toInt(),
                            y = it.component2().toInt(),
                        )
                    }
                    return@run // 'continue' alternative to use inside the 'let' function
                }
                INSTRUCTION_PATTERN.find(line)?.let { result ->
                    tempInstruction += result.destructured.let {
                        Folding(
                            coordinate = Coordinate.valueOf(it.component1().uppercase()),
                            value = it.component2().toInt(),
                        )
                    }
                    return@run // 'continue' alternative to use inside the 'let' function
                }
            }
        }
        coordinates = tempCoordinates
        instruction = tempInstruction
    }
}
