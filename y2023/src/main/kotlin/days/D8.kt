package days

import corbella83.aoc.Day
import corbella83.aoc.Resource
import corbella83.aoc.Result
import corbella83.aoc.utils.lcm

object D8 : Day {

    private val regex = Regex("(\\w{3}) = \\((\\w{3}), (\\w{3})\\)")
    private val regex2 = Regex("\\w{2}Z")

    private class Condition(
        val left: String,
        val right: String
    )

    override val day = 8

    override fun part1(resource: Resource): Result {
        val blocks = resource.readBlocks { it }

        val ordering = blocks.first().single().toCharArray()
        val nodes = blocks.last()
            .readLines(regex) { it[1] to Condition(it[2], it[3]) }
            .toMap()

        return nodes.steps("AAA", Regex("ZZZ"), ordering)
    }

    override fun part2(resource: Resource): Result {
        val blocks = resource.readBlocks { it }

        val ordering = blocks.first().single().toCharArray()
        val nodes = blocks.last()
            .readLines(regex) { it[1] to Condition(it[2], it[3]) }
            .toMap()

        return nodes.keys
            .filter { it.endsWith("A") }
            .map { nodes.steps(it, regex2, ordering) }
            .lcm()
    }

    private fun Map<String, Condition>.steps(startNode: String, endNode: Regex, ordering: CharArray): Long {
        var currentNode = startNode

        var steps = 0
        while (true) {
            val isRight = ordering[steps % ordering.size] == 'R'
            currentNode = this[currentNode]!!.run { if (isRight) right else left }

            steps++
            if (currentNode.matches(endNode)) break
        }
        return steps.toLong()
    }
}
