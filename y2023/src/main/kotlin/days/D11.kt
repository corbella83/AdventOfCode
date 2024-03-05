package days

import corbella83.aoc.Day
import corbella83.aoc.Resource
import corbella83.aoc.Result
import corbella83.aoc.model.Index
import corbella83.aoc.utils.pairs
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

object D11 : Day {

    override val day = 11

    override fun part1(resource: Resource): Result {
        val data = resource.readMatrix()
        val galaxies = data.findAll('#')

        return galaxies.getDistances(2, data.size())
            .sum()
    }

    override fun part2(resource: Resource): Result {
        val data = resource.readMatrix()
        val galaxies = data.findAll('#')

        return galaxies.getDistances(1_000_000, data.size())
            .sum()
    }

    private fun List<Index>.getDistances(expansion: Long, size: Pair<Int, Int>): List<Long> {
        val emptyColumns = (1..size.first).toHashSet().apply { removeAll(this@getDistances.map { it.x }.toSet()) }
        val emptyRows = (1..size.second).toHashSet().apply { removeAll(this@getDistances.map { it.y }.toSet()) }

        return pairs().map {
            val h = min(it.first.x, it.second.x)..max(it.first.x, it.second.x)
            val increaseH = (expansion - 1) * h.intersect(emptyColumns).size

            val v = min(it.first.y, it.second.y)..max(it.first.y, it.second.y)
            val increaseV = (expansion - 1) * v.intersect(emptyRows).size

            abs(it.first.x - it.second.x) + increaseH + abs(it.first.y - it.second.y) + increaseV
        }
    }
}
