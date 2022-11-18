package dev.arhor.aoc

import dev.arhor.aoc.ResourceReader.readInput

fun main() {
    fun solvePuzzle1(input: Sequence<String>): String {
        val model = OctopusesStateModel(input)

        repeat(100) {
            model.makeAStep()
        }
        return model.flashes.toString()
    }

    println("result 1: ${readInput("/Day11.txt", ::solvePuzzle1)}")
}

class OctopusesStateModel(input: Sequence<String>) : MatrixModel(input) {

    var flashes = 0
        private set

    fun makeAStep() {
        incrementEnergy()
        flashes += flashOctopuses()
        resetEnergy()
    }

    private tailrec fun flashOctopuses(counter: Int = 0): Int {
        findEmpoweredOctopuses().let {
            return if (it.isEmpty()) {
                counter
            } else {
                flashOctopuses(counter + it.onEach(::flash).count())
            }
        }
    }

    private fun incrementEnergy() {
        for (row in data) {
            for (index in row.indices) {
                row[index]++
            }
        }
    }

    private fun resetEnergy() {
        for (row in data) {
            for ((index, value) in row.withIndex()) {
                if (value < 0 || value > 9) {
                    row[index] = 0
                }
            }
        }
    }

    private fun findEmpoweredOctopuses(): List<Point> {
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

    private fun flash(point: Point) {
        data[point.y][point.x] = -1
        findAdjacentCoordinates(point.x, point.y, data, diagonal = true).forEach {
            if (data[it] != -1) {
                data[it.y][it.x]++
            }
        }
    }
}
