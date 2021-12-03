package dev.arhor.aoc

operator fun Pair<String, String>.plus(other: Pair<String, String>) = Pair(first + other.first, second + other.second)