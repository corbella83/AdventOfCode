package days

import corbella83.aoc.Day
import corbella83.aoc.Resource
import corbella83.aoc.Result

object D2 : Day {

    private data class Cubes(
        val red: Int,
        val green: Int,
        val blue: Int
    )

    private data class Game(
        val id: Int,
        val cubes: List<Cubes>
    )

    override val day = 2

    override fun part1(resource: Resource): Result {
        val bag = Cubes(12, 13, 14)

        return resource.readLines { parseGame(it) }
            .filter { game -> game.cubes.all { it.red <= bag.red && it.green <= bag.green && it.blue <= bag.blue } }
            .sumOf { it.id }
    }

    override fun part2(resource: Resource): Result {
        return resource.readLines { parseGame(it) }
            .map { it.cubes }
            .map { cubes -> Cubes(cubes.maxOf { it.red }, cubes.maxOf { it.green }, cubes.maxOf { it.blue }) }
            .sumOf { it.red * it.green * it.blue }
    }

    private fun parseGame(game: String): Game {
        val split = game.split(':')
        return Game(
            split[0].substringAfterLast(' ').toInt(),
            split[1].split(';').map { parseCheck(it) }
        )
    }

    private fun parseCheck(check: String): Cubes {
        val map = check.split(',')
            .associate {
                val split = it.trim().split(' ')
                split[1] to split[0].toInt()
            }

        return Cubes(map["red"] ?: 0, map["green"] ?: 0, map["blue"] ?: 0)
    }
}
