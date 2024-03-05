package days

import corbella83.aoc.Day
import corbella83.aoc.Resource
import corbella83.aoc.Result
import corbella83.aoc.utils.merge
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.sqrt

object D6 : Day {

    data class Race(
        val time: Long,
        val distance: Long
    )

    override val day = 6

    override fun part1(resource: Resource): Result {
        val lines = resource.readLines { it }

        val time = lines.first()
            .substringAfter(':')
            .split(' ')
            .filter { it.isNotEmpty() }
            .map { it.toLong() }

        val distance = lines.last()
            .substringAfter(':')
            .split(' ')
            .filter { it.isNotEmpty() }
            .map { it.toLong() }

        return merge(time, distance) { t, d -> Race(t, d) }
            .map { it.numberOfWins() }.reduce(Long::times)
    }

    override fun part2(resource: Resource): Result {
        val lines = resource.readLines { it }

        val time = lines.first()
            .substringAfter(':')
            .filter { it != ' ' }
            .toLong()

        val distance = lines.last()
            .substringAfter(':')
            .filter { it != ' ' }
            .toLong()

        return Race(time, distance).numberOfWins()
    }

    private fun Race.numberOfWins(): Long {
        // (t-x)*x-d=0 -> x^2 + x*t + d = 0
        return with(sqrt(time * time - 4.0 * distance)) {
            val ox1 = floor(time.minus(this).div(2).plus(1)).toLong()
            val ox2 = ceil(time.plus(this).div(2).minus(1)).toLong()
            ox2 - ox1 + 1
        }
    }
}