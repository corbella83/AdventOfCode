package days

import corbella83.aoc.Day
import corbella83.aoc.Resource
import corbella83.aoc.Result
import corbella83.aoc.algorithms.StoerWagner

object D25 : Day {

    override val day = 25

    override fun part1(resource: Resource): Result {
        return resource.readLines { parseRelation(it) }
            .flatten()
            .let { StoerWagner(it) }
            .minCut()
            .run { first.size * second.size }
    }

    override fun part2(resource: Resource): Result {
        return 0L
    }

    private fun parseRelation(relation: String): List<Pair<String, String>> {
        val split = relation.split(':')
        val list = split[1].trim().split(' ')
        return list.map { split[0] to it }
    }
}
