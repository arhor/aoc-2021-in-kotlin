package dev.arhor.aoc

import dev.arhor.aoc.ResourceReader.readInput
import kotlin.math.absoluteValue
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

    private val data: List<MutableList<Boolean>>

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

        data = List(size = points.maxOf { it.y } + 1) {
            MutableList(size = points.maxOf { it.x } + 1) { false }
        }

        for (point in points) {
            data[point.y][point.x] = true
        }
    }

    fun calculateVisiblePoints(): Int {
        return instructions
            .fold(data, ::processInstruction)
            .also { values -> println(values.stringify { if (it) "#" else "." }) }
            .fold(0, ::countPoints)
    }
}

private fun processInstruction(data: List<List<Boolean>>, instruction: Fold): List<List<Boolean>> {
    return when (instruction) {
        is Fold.X -> {
            data.fold(ArrayList()) { result, values ->
                result.also {
                    it += fold(values, instruction.value) { one, two ->
                        List(size = max(one.size, two.size)) { i ->
                            (one[i] ?: false) or (two[i] ?: false)
                        }
                    }
                }
            }
        }

        is Fold.Y -> {
            fold(data, instruction.value) { one, two ->
                val result = ArrayList<List<Boolean>>()
                val max = one.mapNotNull { it?.size }.max()

                for ((index, line) in one.withIndex()) {
                    val booleans = two[index]
                    result += List(max) { i ->
                        (line?.get(i) ?: false) or (booleans?.get(i) ?: false)
                    }
                }
                result
            }
        }
    }
}

private fun countPoints(result: Int, values: List<Boolean>): Int {
    return result + values.count { it }
}

private inline fun <T> fold(items: List<T>, index: Int, combine: (List<T?>, List<T?>) -> List<T>): List<T> {
    var one = items.slice<T?>(0 until index)
    var two = items.slice<T?>(index + 1 until items.size).reversed()

    (one.size - two.size).let {
        when {
            it < 0 -> one = List(size = it.absoluteValue) { null } + one
            it > 0 -> two = List(size = it.absoluteValue) { null } + two
        }
    }
    return combine(one, two)
}

private fun <T> List<List<T>>.stringify(transform: (T) -> String): String {
    return joinToString(separator = "\n") { it.joinToString(separator = "", transform = transform) }
}
