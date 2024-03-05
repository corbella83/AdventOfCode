package days

import corbella83.aoc.Day
import corbella83.aoc.Resource
import corbella83.aoc.Result
import corbella83.aoc.utils.diff

object D9 : Day {

    override val day = 9

    override fun part1(resource: Resource): Result {
        return resource.readLines { parseHistory(it) }
            .sumOf { it.getPrediction() }
    }

    override fun part2(resource: Resource): Result {
        return resource.readLines { parseHistory(it) }
            .sumOf { it.reversed().getPrediction() }
    }

    private fun parseHistory(history: String): List<Long> {
        return history.split(' ').map { it.toLong() }
    }

    private fun List<Long>.getPrediction(): Long {
        var current = this
        var cumSum = this.last()
        while (true) {
            current = current.diff()
            cumSum += current.last()
            if (current.distinct().size == 1) return cumSum
        }
    }
}
