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

        fun findBasin(): Int {
            val lowestPoints = listOf(Point(86, 1)) // findLowestPoints()
            val basins = ArrayList<Set<Point>>()
            for (point in lowestPoints) {
                print("processing point: $point")
                if (basins.any { point in it }) {
                    println(" - already in basin, skip")
                    continue
                }
                val basin = calcBasin(point)
                basins += basin
                println()
            }
            val first = basins.map { it.size }.sortedDescending().chunked(3).first()
            return first.let { (a, b, c) -> a * b * c }


//            return findLowestPoints().parallelStream()
//                .map {
//                    println("${Thread.currentThread().name} - processing point: $it")
//                    calcBasin(it)
//                }
//                .map { it.size }
//                .toList()
//                .also { println("found basins: ${it.size}") }
//                .sortedDescending()
//                .chunked(3)
//                .first()
//                .let { (a, b, c) -> a * b * c }
        }

        var alreadyProcessed = 0

        private fun calcBasin(point: Point, processedPoints: Set<Point> = hashSetOf(point)): Set<Point> {
            val points = HashSet<Point>()
            for (adjacentLocation in findAdjacentLocationsWithoutDuplicates(point.x, point.y, processedPoints)) {
                if (adjacentLocation in processedPoints) {
                    println("alreadyProcessed: ${++alreadyProcessed}")
                    continue
                }
                val pointSet = if (data[adjacentLocation] == 9) {
                    processedPoints
                } else {
                    calcBasin(adjacentLocation, processedPoints + adjacentLocation)
                }
                points.addAll(pointSet)
            }
            return points

//            return findAdjacentLocations(point.x, point.y)
//                .filter { it !in processedPoints }
//                .map {
//                    if (data[it] == 9) {
//                        processedPoints
//                    } else {
//                        calcBasin(it, processedPoints + it)
//                    }
//                }
//                .flatten()
//                .toSet()
        }

        private fun findAdjacentLocations(x: Int, y: Int): List<Point> {
            return buildList {
                if (x > xMin) add(Point(x - 1, y))
                if (x < xMax) add(Point(x + 1, y))
                if (y > yMin) add(Point(x, y - 1))
                if (y < yMax) add(Point(x, y + 1))
            }
        }

        private fun findAdjacentLocationsWithoutDuplicates(x: Int, y: Int, processed: Set<Point>): List<Point> {
            return buildList {
                if (x > xMin) Point(x - 1, y).takeIf { it !in processed }?.let(::add)
                if (x < xMax) Point(x + 1, y).takeIf { it !in processed }?.let(::add)
                if (y > yMin) Point(x, y - 1).takeIf { it !in processed }?.let(::add)
                if (y < yMax) Point(x, y + 1).takeIf { it !in processed }?.let(::add)
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
