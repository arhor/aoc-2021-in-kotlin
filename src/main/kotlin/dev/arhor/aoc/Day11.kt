package dev.arhor.aoc

import dev.arhor.aoc.ResourceReader.readInput

fun main() {
    fun solvePuzzle1(input: Sequence<String>): String {
        val model = OctopusesStateModel(input)

        repeat(100) {
            model.incrementEnergy()

            var empoweredOctopuses: List<Point>
            while (model.findEmpoweredOctopuses().also { empoweredOctopuses = it }.isNotEmpty()) {
                for (octopus in empoweredOctopuses) {
                    model.flash(octopus)
                }
            }

            model.resetEnergy()
        }

        return model.counter.toString()
    }

    println("result 1: ${readInput("/Day11.txt", ::solvePuzzle1)}")
}

class OctopusesStateModel(input: Sequence<String>) : MatrixModel(input) {

    var counter = 0

    fun incrementEnergy() {
        for (row in data) {
            for (index in row.indices) {
                row[index]++
            }
        }
    }

    fun resetEnergy() {
        for (row in data) {
            for ((index, value) in row.withIndex()) {
                if (value < 0 || value > 9) {
                    row[index] = 0
                }
            }
        }
    }

    fun findEmpoweredOctopuses(): List<Point> {
        val points = ArrayList<Point>()
        for ((row, line) in data.withIndex()) {
            for ((col, energy) in line.withIndex()) {
                if (energy > 9) {
                    points += Point(col, row)
                }
            }
        }
        return points
    }

    fun flash(point: Point) {
        data[point.y][point.x] = -1
        counter++
        findAdjacentCoordinates(point.x, point.y, data, diagonal = true).forEach {
            if (data[it] != -1) {
                data[it.y][it.x]++
            }
        }
    }
}
