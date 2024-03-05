package days

import corbella83.aoc.Day
import corbella83.aoc.Resource
import corbella83.aoc.Result
import corbella83.aoc.model.Direction
import corbella83.aoc.model.Index
import corbella83.aoc.model.Matrix

object D16 : Day {

    private data class Cursor(
        val index: Index,
        val direction: Direction
    )

    override val day = 16

    override fun part1(resource: Resource): Result {
        val matrix = resource.readMatrix()

        val start = Cursor(Index(1, 1), Direction.RIGHT)
        return matrix.energyAt(start)
    }

    override fun part2(resource: Resource): Result {
        val matrix = resource.readMatrix()

        val size = matrix.size()
        var maxEnergy = 0

        for (x in 1..size.first) {
            maxEnergy = maxOf(
                matrix.energyAt(Cursor(Index(1, x), Direction.DOWN)),
                matrix.energyAt(Cursor(Index(size.first, x), Direction.UP)),
                maxEnergy
            )
        }

        for (y in 1..size.second) {
            maxEnergy = maxOf(
                matrix.energyAt(Cursor(Index(y, 1), Direction.RIGHT)),
                matrix.energyAt(Cursor(Index(y, size.second), Direction.LEFT)),
                maxEnergy
            )
        }

        return maxEnergy
    }

    private fun Matrix.energyAt(start: Cursor): Int {
        return buildSet { next(start, this) }
            .distinctBy { it.index }
            .size
    }

    private fun Matrix.next(from: Cursor, result: MutableSet<Cursor>) {
        var current = from

        while (true) {
            val currentChar = get(current.index)
            result.add(current)
            val nextCursors = currentChar.getNextDirections(current.direction)
                .map { Cursor(current.index + it, it) }
                .filter { !isOutOfBounds(it.index) && !result.contains(it) }

            if (nextCursors.size == 1) {
                current = nextCursors.single()
            } else {
                nextCursors.forEach { next(it, result) }
                break
            }
        }
    }

    private fun Char.getNextDirections(from: Direction): List<Direction> {
        return when (this) {
            '/' -> {
                listOf(
                    when (from) {
                        Direction.UP -> Direction.RIGHT
                        Direction.DOWN -> Direction.LEFT
                        Direction.LEFT -> Direction.DOWN
                        Direction.RIGHT -> Direction.UP
                    }
                )
            }

            '\\' -> {
                listOf(
                    when (from) {
                        Direction.UP -> Direction.LEFT
                        Direction.DOWN -> Direction.RIGHT
                        Direction.LEFT -> Direction.UP
                        Direction.RIGHT -> Direction.DOWN
                    }
                )
            }

            '|' -> {
                when (from) {
                    Direction.LEFT, Direction.RIGHT -> listOf(Direction.UP, Direction.DOWN)
                    else -> listOf(from)
                }
            }

            '-' -> {
                when (from) {
                    Direction.UP, Direction.DOWN -> listOf(Direction.LEFT, Direction.RIGHT)
                    else -> listOf(from)
                }
            }

            else -> {
                listOf(from)
            }
        }
    }
}
