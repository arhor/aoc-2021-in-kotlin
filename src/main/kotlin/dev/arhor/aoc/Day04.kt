package dev.arhor.aoc

import dev.arhor.aoc.ResourceReader.readInput
import java.util.Objects.checkIndex

fun main() {
    fun parseCardData(table: List<String>) = table.foldIndexed(BingoCard()) { row, card, data ->
        card.apply {
            for ((col, value) in data.split(" ").filter(String::isNotBlank).withIndex()) {
                this[row, col] = value.toInt()
            }
        }
    }

    fun prepareModel(input: Sequence<String>): Pair<List<Int>, MutableList<BingoCard>> {
        val lines = input.toList()

        val numbers = lines[0].split(",").map(String::trim).filter(String::isNotEmpty).map(String::toInt)
        val bingoCards = lines.subList(1, lines.size).filter(String::isNotBlank).chunked(5).map(::parseCardData)

        return numbers to bingoCards.toMutableList()
    }

    fun calcFinalScore(number: Int, card: BingoCard): Int {
        var sum = 0
        card.forEachIndexed { row, col, value ->
            if (!card.isChecked(row, col)) {
                sum += value
            }
        }
        return sum * number
    }

    fun solvePuzzle1(input: Sequence<String>): Int {
        val (numbers, bingoCards) = prepareModel(input)
        for (number in numbers) {
            for (bingoCard in bingoCards) {
                if (bingoCard.check(number) && bingoCard.wins()) {
                    return calcFinalScore(number, bingoCard)
                }
            }
        }
        return -1
    }

    fun solvePuzzle2(input: Sequence<String>): Int {
        var lastWinner: Pair<Int, BingoCard>? = null
        val (numbers, bingoCards) = prepareModel(input)
        for (number in numbers) {
            val currentWinners = ArrayList<BingoCard>()
            for (bingoCard in bingoCards) {
                if (bingoCard.check(number) && bingoCard.wins()) {
                    currentWinners.add(bingoCard)
                    lastWinner = number to bingoCard
                }
            }
            bingoCards.removeAll(currentWinners)
        }
        return lastWinner?.let { (number, bingoCard) -> calcFinalScore(number, bingoCard) }
            ?: -1
    }

    println("result 1: ${readInput("/Day04.txt", ::solvePuzzle1)}")
    println("result 2: ${readInput("/Day04.txt", ::solvePuzzle2)}")
}

class BingoCard(val rows: Int = 5, val cols: Int = 5) {

    private val numbers = IntArray(rows * cols)
    private val checked = BooleanArray(rows * cols)
    private var rowChecks = IntArray(rows)
    private var colChecks = IntArray(cols)

    operator fun get(row: Int, col: Int): Int {
        return numbers[index(row, col)]
    }

    operator fun set(row: Int, col: Int, value: Int) {
        numbers[index(row, col)] = value
    }

    inline fun forEachIndexed(action: (row: Int, col: Int, value: Int) -> Unit) {
        for (row in 0 until rows) {
            for (col in 0 until cols) {
                action(row, col, this[row, col])
            }
        }
    }

    fun check(number: Int): Boolean {
        forEachIndexed { row, col, value ->
            if (number == value) {
                checked[index(row, col)] = true
                rowChecks[row]++
                colChecks[col]++
                return true
            }
        }
        return false
    }

    fun isChecked(row: Int, col: Int) = checked[index(row, col)]

    fun wins() = rowChecks.contains(rows) || colChecks.contains(cols)

    private fun index(row: Int, col: Int) = (cols * checkIndex(row, rows)) + checkIndex(col, cols)
}
