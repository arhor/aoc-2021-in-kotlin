package dev.arhor.aoc

import dev.arhor.aoc.ResourceReader.readInput

fun main() {
    class HeightMap(input: Sequence<String>) {
        private val data: List<IntArray>
        private val xMax: Int
        private val xMin: Int
        private val yMax: Int
        private val yMin: Int

        init {
            data = input.map { it.map(Char::digitToInt).toIntArray() }.toList()
            xMin = 0
            xMax = (if (data.isNotEmpty()) data[0].size else 0) - 1
            yMin = 0
            yMax = data.size - 1
        }

        fun determineRiskLevel() = findLowestPoints().sumOf { data[it] + 1 }

        fun findBasin() = findLowestPoints().asSequence()
            .map(::calcBasin)
            .map { it.size }
            .sortedDescending()
            .chunked(3)
            .first()
            .fold(1) { a, b -> a * b }

        private fun calcBasin(point: Point, processedPoints: MutableSet<Point> = hashSetOf(point)): Set<Point> {
            return findAdjacentLocations(point.x, point.y)
                .filter { it !in processedPoints }
                .map {
                    if (data[it] == 9) {
                        processedPoints
                    } else {
                        calcBasin(it, processedPoints.apply { add(it) })
                    }
                }
                .flatten()
                .toSet()
        }

        private fun findAdjacentLocations(x: Int, y: Int): List<Point> {
            return buildList {
                if (x > xMin) add(Point(x - 1, y))
                if (x < xMax) add(Point(x + 1, y))
                if (y > yMin) add(Point(x, y - 1))
                if (y < yMax) add(Point(x, y + 1))
            }
        }

        private fun findLowestPoints(): List<Point> {
            val points = ArrayList<Point>()
            for ((y, line) in data.withIndex()) {
                for ((x, height) in line.withIndex()) {
                    if (findAdjacentLocations(x, y).all { data[it] > height }) {
                        points += Point(x, y)
                    }
                }
            }
            return points
        }

        private operator fun List<IntArray>.get(point: Point): Int = this[point.y][point.x]
    }

    fun solvePuzzle1(input: Sequence<String>) = HeightMap(input).determineRiskLevel()

    fun solvePuzzle2(input: Sequence<String>) = HeightMap(input).findBasin()

    println("result 1: ${readInput("/Day09.txt", ::solvePuzzle1)}")
    println("result 2: ${readInput("/Day09.txt", ::solvePuzzle2)}")
}
