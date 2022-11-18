package dev.arhor.aoc

operator fun Pair<String, String>.plus(other: Pair<String, String>) = Pair(first + other.first, second + other.second)

data class Point(val x: Int, val y: Int) {
    operator fun plus(other: Point) = Point(x + other.x, y + other.y)
    operator fun minus(other: Point) = Point(x - other.x, y - other.y)
}

operator fun List<IntArray>.get(point: Point): Int = this[point.y][point.x]

fun  List<IntArray>.allValuesAre(value: Int): Boolean = all { row -> row.all { it == value } }

/**
 * Converts passed function to the function memorizing its result according to the arguments used.
 *
 * @param T      input type
 * @param R      output type
 * @param cache  map object to use as calculation cache, by default represented as [HashMap] instance
 * @param action function to wrap, gives read-access to the current cache via 'this' reference
 */
fun <T, R> caching(cache: MutableMap<T, R> = HashMap(), action: Map<T, R>.(T) -> R): (T) -> R {
    return { arg -> cache[arg] ?: cache.action(arg).also { cache[arg] = it } }
}

fun findAdjacentCoordinates(col: Int, row: Int, data: List<IntArray>, diagonal: Boolean = false) = buildList {
    val maxRow = data.size - 1
    val maxCol = (if (row <= maxRow) data[row].size else 0) - 1

    if (col > 0) add(Point(col - 1, row))
    if (row > 0) add(Point(col, row - 1))
    if (col < maxCol) add(Point(col + 1, row))
    if (row < maxRow) add(Point(col, row + 1))

    if (diagonal) {
        if (col > 0) {
            if (row > 0) add(Point(col - 1, row - 1))
            if (row < maxRow) add(Point(col - 1, row + 1))
        }
        if (col < maxCol) {
            if (row > 0) add(Point(col + 1, row - 1))
            if (row < maxRow) add(Point(col + 1, row + 1))
        }
    }
}

open class MatrixModel(input: Sequence<String>) {
    val data: List<IntArray> = input.map { it.map(Char::digitToInt).toIntArray() }.toList()
}