package days

import corbella83.aoc.Day
import corbella83.aoc.Resource
import corbella83.aoc.Result
import kotlin.math.pow

object D4 : Day {

    private data class Game(
        val name: String,
        val playNumbers: List<Long>,
        val winingNumbers: List<Long>
    )

    override val day = 4

    override fun part1(resource: Resource): Result {
        return resource.readLines { parseGame(it) }
            .map { it.winingNumbers.intersect(it.playNumbers.toSet()).size }
            .sumOf { 2.0.pow(it.toDouble() - 1).toLong() }
    }

    override fun part2(resource: Resource): Result {
        val games = resource.readLines { parseGame(it) }
        val results = games.map { it.winingNumbers.intersect(it.playNumbers.toSet()).size }

        val array = Array(results.size) { 1L }
        results.forEachIndexed { index, win ->
            val number = array[index]
            for (i in 1..win) array[index + i] += number
        }

        return array.sum()
    }

    private fun parseGame(game: String): Game {
        val parts = game.split(':', '|')
        val name = parts[0]
        val winning = parts[1].split(' ').filter { it.isNotEmpty() }.map { it.toLong() }
        val plays = parts[2].split(' ').filter { it.isNotEmpty() }.map { it.toLong() }
        return Game(name, plays, winning)
    }
}
