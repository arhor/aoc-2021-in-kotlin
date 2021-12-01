package dev.arhor.aoc

fun main() {
    ResourceReader.readInput("/Day01_1.txt") { lines ->
        var result = 0

        var a: Int?
        var b: Int? = null
        var c: Int? = null

        lines.map(String::toInt).mapNotNull { curr ->
            a = b
            b = c
            c = curr

            if (a != null && b != null && c != null) {
                a!! + b!! + c!!
            } else {
                null
            }
        }.reduce { prev, next ->
            if (next > prev) {
                result++
            }
            next
        }

        println("result: $result")
    }
}
