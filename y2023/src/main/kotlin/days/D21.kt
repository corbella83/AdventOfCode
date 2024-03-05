package days

import corbella83.aoc.Day
import corbella83.aoc.Resource
import corbella83.aoc.Result
import corbella83.aoc.model.Direction
import corbella83.aoc.model.Index
import corbella83.aoc.model.Matrix
import corbella83.aoc.utils.square

object D21 : Day {

    override val day = 21

    override fun part1(resource: Resource): Result {
        val ground = resource.readMatrix()
        val start = ground.findAll('S').single()
        return ground.travel(start, 64).first.toLong()
    }

    override fun part2(resource: Resource): Result {
        val ground = resource.readMatrix()
        val start = ground.findAll('S').single()
        return ground.travelLarge(start, 26_501_365)
    }

    private fun Matrix.travelLarge(start: Index, steps: Long): Long {
        val boards = (steps - start.y + 1) / size().first
        val chunk = start.x - 1

        val complete = travel(start, 2 * chunk).let {
            val odd = boards.dec().square()
            val even = boards.square()
            odd * it.second + even * it.first
        }

        val partialBounds = cardinals()
            .sumOf { travel(it, 2 * chunk).first }

        val partialEdges = edges()
            .sumOf { boards * travel(it, chunk - 1).first + boards.dec() * travel(it, 3 * chunk).first }

        return complete + partialEdges + partialBounds
    }

    private fun Matrix.travel(start: Index, steps: Int): Pair<Int, Int> {
        val state1 = hashSetOf<Index>()
        val state2 = hashSetOf<Index>()
        var isState1 = true

        var current = setOf(start)
        state1.add(start)
        repeat(steps) {
            val target = if (isState1) state2 else state1
            current = current.map { getSurroundingGarden(it) }
                .flatten()
                .filter { it !in target }
                .toSet()
                .also { target.addAll(it) }

            isState1 = !isState1
        }

        return if (isState1) state1.size to state2.size else state2.size to state1.size
    }

    private fun Matrix.getSurroundingGarden(index: Index): List<Index> {
        return Direction.entries
            .map { index + it }
            .filterNot { isOutOfBounds(it) }
            .filter { get(it) != '#' }
    }
}
