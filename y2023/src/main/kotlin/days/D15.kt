package days

import corbella83.aoc.Day
import corbella83.aoc.Resource
import corbella83.aoc.Result

object D15 : Day {

    private val matcher = Regex("([a-z]+)([=-])(\\d*)")

    override val day = 15

    override fun part1(resource: Resource): Result {
        return resource.single()
            .split(',')
            .sumOf { it.encrypt() }
    }

    override fun part2(resource: Resource): Result {
        val book = Array<LinkedHashMap<String, Int>>(256) { LinkedHashMap() }

        val input = resource.single()
        matcher.findAll(input).forEach {
            val code = it.groupValues[1]
            val operation = it.groupValues[2]

            val elements = book[code.encrypt()]
            if (operation == "-") {
                elements.remove(code)
            } else if (operation == "=") {
                val value = it.groupValues[3].toInt()
                if (elements.contains(code)) {
                    elements.replace(code, value)
                } else {
                    elements[code] = value
                }
            } else {
                throw Exception()
            }
        }

        val number = book.mapIndexed { bookIndex, bookElements ->
            bookElements.toList().mapIndexed { index, element ->
                (bookIndex + 1) * (index + 1) * element.second
            }
                .sum()
        }.sum()

        return number
    }

    private fun String.encrypt(): Int {
        var currentValue = 0
        toCharArray().forEach {
            currentValue += it.code
            currentValue *= 17
            currentValue %= 256
        }

        return currentValue
    }
}
