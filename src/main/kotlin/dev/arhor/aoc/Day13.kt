package dev.arhor.aoc

import dev.arhor.aoc.ResourceReader.readInput
import kotlin.math.max

private val COORDINATES_PATTERN = Regex("([0-9]+),([0-9]+)")
private val INSTRUCTION_PATTERN = Regex("fold along ([xy])=([0-9]+)")

fun main() {
    fun solvePuzzle1(input: Sequence<String>) = DataModel(input).calculateVisiblePoints()

    println("result 1: ${readInput("/Day13.txt", ::solvePuzzle1)}")
}

private sealed interface Fold {
    @JvmInline
    value class X(val value: Int) : Fold

    @JvmInline
    value class Y(val value: Int) : Fold

    companion object {
        fun by(name: String, value: String) = when (name) {
            "x" -> X(value.toInt())
            "y" -> Y(value.toInt())
            else -> throw IllegalArgumentException("Unsupported coordinate: '$name'")
        }
    }
}

private class DataModel(input: Sequence<String>) {

    private val data: Array<IntArray>

    private val points: List<Point>
    private val instructions: List<Fold>

    init {
        val tempPoints = ArrayList<Point>().also { points = it }
        val tempInstructions = ArrayList<Fold>().also { instructions = it }

        for (line in input) {
            run { // allows to simulate 'continue' inside the 'let' function
                COORDINATES_PATTERN.find(line)?.let { result ->
                    tempPoints += result.destructured.let { (x, y) ->
                        Point(
                            x = x.toInt(),
                            y = y.toInt(),
                        )
                    }
                    return@run // 'continue' alternative to use inside the 'let' function
                }
                INSTRUCTION_PATTERN.find(line)?.let { result ->
                    tempInstructions += result.destructured.let { (name, value) ->
                        Fold.by(name, value)
                    }
                    return@run // 'continue' alternative to use inside the 'let' function
                }
            }
        }

        data = Array(size = points.maxOf { it.y } + 1) {
            IntArray(size = points.maxOf { it.x } + 1)
        }

        for (point in points) {
            data[point.y][point.x] = 1
        }
    }

    fun calculateVisiblePoints(): Int {
        var array: Array<IntArray> = data

        for (instruction in instructions) {
            val result = ArrayList<IntArray>()

            when (instruction) {
                is Fold.X -> {
                    for (it in array) {
                        val (one, two) = split(it.toTypedArray(), instruction.value)

                        result += IntArray(max(one.size, two.size)) { i ->
                            (one[i] ?: 0) or (two[i] ?: 0)
                        }
                    }
                }

                is Fold.Y -> {
                    val (one, two) = split(array, instruction.value)
                    val max = one.mapNotNull { it?.size }.max()

                    for ((index, line) in one.withIndex()) {
                        result += IntArray(max) { i ->
                            (line?.get(i) ?: 0) or (two[index]?.get(i) ?: 0)
                        }
                    }
                }
            }
            array = result.toTypedArray()
        }

        println(
            array.joinToString(separator = "\n") { line ->
                line.joinToString(separator = "") { point ->
                    if (point >= 1) {
                        "#"
                    } else {
                        "."
                    }
                }
            }
        )

        return array.fold(0) { acc, points -> acc + points.sum() }
    }
}

private inline fun <reified T> split(it: Array<T>, value: Int): Pair<Array<T?>, Array<T?>> {
    val one: MutableList<T?> = it.slice(0 until value).toMutableList()
    val two: MutableList<T?> = it.slice(value + 1 until it.size).reversed().toMutableList()

    val diff = one.size - two.size
    if (diff > 0) {
        repeat(diff) {
            two.add(0, null)
        }
    } else if (diff < 0) {
        repeat(-diff) {
            one.add(0, null)
        }
    }

    return one.toTypedArray() to two.toTypedArray()
}
