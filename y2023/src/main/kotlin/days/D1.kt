package days

import corbella83.aoc.Day
import corbella83.aoc.Resource
import corbella83.aoc.Result

object D1 : Day {

    override val day = 1

    private data class Digit(
        val symbol: Char,
        val representation: List<String>
    )

    private val digits = listOf(
        Digit('1', listOf("one", "1")),
        Digit('2', listOf("two", "2")),
        Digit('3', listOf("three", "3")),
        Digit('4', listOf("four", "4")),
        Digit('5', listOf("five", "5")),
        Digit('6', listOf("six", "6")),
        Digit('7', listOf("seven", "7")),
        Digit('8', listOf("eight", "8")),
        Digit('9', listOf("nine", "9")),
    )

    override fun part1(resource: Resource): Result {
        return resource.readLines { line ->
            val first = line.first { it.isDigit() }
            val last = line.last { it.isDigit() }
            "$first$last".toLong()
        }.sum()
    }

    override fun part2(resource: Resource): Result {
        return resource.readLines { line ->
            val first = line.firstDigit()
            val last = line.lastDigit()
            "$first$last".toLong()
        }.sum()
    }

    private fun String.firstDigit(): Char {
        var index = 0
        while (index < length) {
            val substring = substring(index)
            digits.forEach { digit ->
                if (digit.representation.any { substring.startsWith(it) }) return digit.symbol
            }
            index++
        }

        throw Exception("First digit not found")
    }

    private fun String.lastDigit(): Char {
        var index = 0
        while (index < length) {
            val substring = substring(0, length - index)
            digits.forEach { digit ->
                if (digit.representation.any { substring.endsWith(it) }) return digit.symbol
            }
            index++
        }

        throw Exception("Last digit not found")
    }
}
