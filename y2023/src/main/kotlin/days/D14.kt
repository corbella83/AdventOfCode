package days

import corbella83.aoc.Day
import corbella83.aoc.Resource
import corbella83.aoc.Result
import corbella83.aoc.model.Direction
import corbella83.aoc.model.Matrix
import corbella83.aoc.utils.smartRepeat
import corbella83.aoc.utils.swap

object D14 : Day {

    override val day = 14

    override fun part1(resource: Resource): Result {
        return resource.readMatrix()
            .tile(Direction.UP)
            .load()
    }

    override fun part2(resource: Resource): Result {
        val matrix = resource.readMatrix()

        val result = smartRepeat(1_000_000_000, matrix) {
            it.tile(Direction.UP)
                .tile(Direction.LEFT)
                .tile(Direction.DOWN)
                .tile(Direction.RIGHT)
        }

        return result.load()
    }

    private fun Matrix.tile(direction: Direction): Matrix {
        return when (direction) {
            Direction.UP -> {
                val range = IntProgression.fromClosedRange(0, size().first - 1, 1)
                getColumns()
                    .apply { move(range) }
                    .let { Matrix.fromColumns(it) }
            }

            Direction.DOWN -> {
                val range = IntProgression.fromClosedRange(size().first - 1, 0, -1)
                getColumns()
                    .apply { move(range) }
                    .let { Matrix.fromColumns(it) }
            }

            Direction.LEFT -> {
                val range = IntProgression.fromClosedRange(0, size().second - 1, 1)
                getRows()
                    .apply { move(range) }
                    .let { Matrix.fromRows(it) }
            }

            Direction.RIGHT -> {
                val range = IntProgression.fromClosedRange(size().second - 1, 0, -1)
                getRows()
                    .apply { move(range) }
                    .let { Matrix.fromRows(it) }
            }
        }
    }

    private fun List<CharArray>.move(range: IntProgression) {
        forEach { dimension ->
            var current = range.first
            range.forEach {
                val c = dimension[it]
                if (c == 'O') {
                    dimension.swap(it, current)
                    current += range.step
                } else if (c == '#') {
                    current = it + range.step
                }
            }
        }
    }

    private fun Matrix.load(): Int {
        val amount = size().first
        return findAll('O')
            .sumOf { amount - (it.y - 1) }
    }
}
