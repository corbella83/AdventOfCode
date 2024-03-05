package days

import corbella83.aoc.Day
import corbella83.aoc.Resource
import corbella83.aoc.Result
import corbella83.aoc.model.Direction
import corbella83.aoc.model.Index
import corbella83.aoc.model.area

object D18 : Day {

    private val matcher = Regex("(\\w) (\\d+).*#(\\w+)")

    private data class Rule(
        val direction: Direction,
        val meters: Int
    )

    override val day = 18

    override fun part1(resource: Resource): Result {
        val rules = resource.readLines(matcher) {
            Rule(it[1].toDirection(), it[2].toInt())
        }

        return rules.getEdges()
            .area()
    }

    override fun part2(resource: Resource): Result {
        val rules = resource.readLines(matcher) {
            with(it[3]) {
                Rule(last().toDirection(), dropLast(1).toInt(16))
            }
        }

        return rules.getEdges()
            .area()
    }

    private fun List<Rule>.getEdges(): List<Index> {
        var indexY = 1
        var indexX = 1
        val result = arrayListOf<Index>()
        forEach {
            when (it.direction) {
                Direction.UP -> indexY -= it.meters
                Direction.DOWN -> indexY += it.meters
                Direction.LEFT -> indexX -= it.meters
                Direction.RIGHT -> indexX += it.meters
            }
            result.add(Index(indexY, indexX))
        }

        return result
    }

    private fun String.toDirection(): Direction {
        return when (this) {
            "U" -> Direction.UP
            "D" -> Direction.DOWN
            "L" -> Direction.LEFT
            "R" -> Direction.RIGHT
            else -> throw Exception()
        }
    }

    private fun Char.toDirection(): Direction {
        return when (this) {
            '0' -> Direction.RIGHT
            '1' -> Direction.DOWN
            '2' -> Direction.LEFT
            '3' -> Direction.UP
            else -> throw Exception()
        }
    }
}
