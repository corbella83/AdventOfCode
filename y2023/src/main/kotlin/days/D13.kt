package days

import corbella83.aoc.Day
import corbella83.aoc.Resource
import corbella83.aoc.Result
import corbella83.aoc.model.Matrix

object D13 : Day {

    private enum class Direction { HORIZONTAL, VERTICAL }

    private data class Mirror(
        val direction: Direction,
        val index: Int
    )

    override val day = 13

    override fun part1(resource: Resource): Result {
        return resource.readBlocks { it.readMatrix() }
            .map { it.findMirror(0) }
            .sumOf { it.weight() }
    }

    override fun part2(resource: Resource): Result {
        return resource.readBlocks { it.readMatrix() }
            .map { it.findMirror(1) }
            .sumOf { it.weight() }
    }

    private fun Matrix.findMirror(errors: Int): Mirror {
        val sz = this.size()

        for (i in 1..<sz.first) {
            val current = Mirror(Direction.HORIZONTAL, i)
            val maxDifferences = getDifferencesAt(current, errors)
            if (maxDifferences == 0) return current
        }

        for (j in 1..<sz.second) {
            val current = Mirror(Direction.VERTICAL, j)
            val maxDifferences = getDifferencesAt(current, errors)
            if (maxDifferences == 0) return current
        }

        throw Exception("No mirror have been found")
    }

    private fun Matrix.getDifferencesAt(mirror: Mirror, errors: Int): Int {
        var maxDifferences = errors

        val max = when (mirror.direction) {
            Direction.HORIZONTAL -> size().first
            Direction.VERTICAL -> size().second
        }

        var from = mirror.index
        var to = mirror.index + 1

        while (from >= 1 && to <= max) {
            maxDifferences -= when (mirror.direction) {
                Direction.HORIZONTAL -> getRowDiff(from, to, maxDifferences)
                Direction.VERTICAL -> getColumnDiff(from, to, maxDifferences)
            }

            if (maxDifferences < 0) break
            from--
            to++
        }

        return maxDifferences
    }

    private fun Mirror.weight() = when (direction) {
        Direction.HORIZONTAL -> 100 * index
        Direction.VERTICAL -> index
    }
}
