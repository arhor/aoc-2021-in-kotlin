package dev.arhor.aoc

import dev.arhor.aoc.ResourceReader.readInput

fun main() {
    class HeightMap(input: Sequence<String>) : MatrixModel(input) {

        val riskLevel
            get() = findLowestPoints().sumOf { data[it] + 1 }

        val largestBasinsFactor
            get() = findLowestPoints().asSequence()
                .map(::calcBasin)
                .map { it.size }
                .sortedDescending()
                .chunked(3)
                .first()
                .reduce { a, b -> a * b }

        private fun calcBasin(point: Point, processedPoints: MutableSet<Point> = hashSetOf(point)): Set<Point> {
            return findAdjacentCoordinates(point.x, point.y, data)
                .filter { it !in processedPoints }
                .map { if (data[it] == 9) processedPoints else calcBasin(it, processedPoints.apply { add(it) }) }
                .flatten()
                .toSet()
        }

        private fun findLowestPoints(): List<Point> {
            val points = ArrayList<Point>()
            for ((row, line) in data.withIndex()) {
                for ((col, height) in line.withIndex()) {
                    if (findAdjacentCoordinates(col, row, data).all { data[it] > height }) {
                        points += Point(col, row)
                    }
                }
            }
            return points
        }
    }

    fun solvePuzzle1(input: Sequence<String>) = HeightMap(input).riskLevel

    fun solvePuzzle2(input: Sequence<String>) = HeightMap(input).largestBasinsFactor

    println("result 1: ${readInput("/Day09.txt", ::solvePuzzle1)}")
    println("result 2: ${readInput("/Day09.txt", ::solvePuzzle2)}")
}
