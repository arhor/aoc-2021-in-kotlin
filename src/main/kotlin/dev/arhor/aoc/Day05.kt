package dev.arhor.aoc

import dev.arhor.aoc.ResourceReader.readInput

fun main() {
    data class Point(val x: Int, val y: Int)

    fun line(a: Point, b: Point): List<Point> {
        val points = ArrayList<Point>()
        var (x1, y1) = a
        val (x2, y2) = b
        while (true) {
            points.add(Point(x1, y1))
            if (x1 == x2 && y1 == y2) {
                break
            }
            if (x1 < x2) {
                x1++
            } else if (x1 > x2) {
                x1--
            }
            if (y1 < y2) {
                y1++
            } else if (y1 > y2) {
                y1--
            }
        }
        return points
    }

    fun parse(input: String) = input.split(",").map(String::trim).map(String::toInt).let { (x, y) -> Point(x, y) }

    fun markPoint(dict: MutableMap<Int, MutableMap<Int, Int>>, x: Int, y: Int) {
        val data = dict.computeIfAbsent(x, ::HashMap)
        val currVal = data.computeIfAbsent(y) { 0 }
        data[y] = currVal + 1
    }

    fun genericSolution(input: Sequence<String>, lineCheck: (Point, Point) -> Boolean = { _, _ -> true }): Int {
        val dict: MutableMap<Int, MutableMap<Int, Int>> = HashMap()
        input.map { it.split("->") }.map { (a, b) -> parse(a) to parse(b) }.forEach { (a, b) ->
            if (lineCheck(a, b)) {
                for ((x, y) in line(a, b)) {
                    markPoint(dict, x, y)
                }
            }
        }
        return dict.values.sumOf { it.values.count { points -> points > 1 } }
    }

    fun solvePuzzle1(input: Sequence<String>) = genericSolution(input) { (x1, y1), (x2, y2) -> x1 == x2 || y1 == y2 }

    fun solvePuzzle2(input: Sequence<String>) = genericSolution(input)

    println("result 1: ${readInput("/Day05.txt", ::solvePuzzle1)}")
    println("result 2: ${readInput("/Day05.txt", ::solvePuzzle2)}")
}
