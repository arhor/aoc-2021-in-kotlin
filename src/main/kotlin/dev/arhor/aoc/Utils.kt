package dev.arhor.aoc

operator fun Pair<String, String>.plus(other: Pair<String, String>) = Pair(first + other.first, second + other.second)

data class Point(val x: Int, val y: Int) {
    operator fun plus(other: Point) = Point(x + other.x, y + other.y)
    operator fun minus(other: Point) = Point(x - other.x, y - other.y)
}

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