package days

import corbella83.aoc.Day
import corbella83.aoc.Resource
import corbella83.aoc.Result
import corbella83.aoc.model.Index
import corbella83.aoc.model.Matrix

object D10 : Day {

    private val possibleStarts = listOf(
        Index.UP to listOf('|', 'J'),
        Index.DOWN to listOf('|', 'F'),
        Index.LEFT to listOf('-', 'L'),
        Index.RIGHT to listOf('-', '7')
    )

    override val day = 10

    override fun part1(resource: Resource): Result {
        val data = resource.readMatrix()
        val startPoint = data.findAll('S').single()

        return data.getPath(startPoint)
            .size
            .div(2)
    }

    override fun part2(resource: Resource): Result {
        val data = resource.readMatrix()
        val startPoint = data.findAll('S').single()

        return data.getPath(startPoint)
            .let { data.areaOf(it) }
    }

    private fun Matrix.getPath(startPoint: Index): List<Index> {
        fun Matrix.guessNext(startPoint: Index) = possibleStarts.first { pair ->
            val digit = getOrNull(startPoint + pair.first)
            pair.second.any { it == digit }
        }.first

        var previous = startPoint
        var current = startPoint + guessNext(startPoint)
        val path = arrayListOf<Index>()
        path.add(current)
        while (true) {
            if (current == startPoint) break
            val tmp = next(previous, current)
            previous = current
            current = tmp

            path.add(tmp)
        }

        return path
    }

    private fun Matrix.next(previous: Index, current: Index): Index {
        val direction = current - previous
        return when (get(current)) {
            '|' -> current + direction
            '-' -> current + direction
            'L' -> current + direction.flip()
            'J' -> current - direction.flip()
            '7' -> current + direction.flip()
            'F' -> current - direction.flip()
            else -> throw Exception()
        }
    }
}
