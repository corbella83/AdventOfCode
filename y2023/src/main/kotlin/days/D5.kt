package days

import corbella83.aoc.Day
import corbella83.aoc.Resource
import corbella83.aoc.Result
import corbella83.aoc.utils.intersectRange
import corbella83.aoc.utils.plusRange
import corbella83.aoc.utils.removeRange

object D5 : Day {

    private data class Relation(
        val range: LongRange,
        val offset: Long
    )

    override val day = 5

    override fun part1(resource: Resource): Result {
        val blocks = resource.readBlocks { it }
        val seeds = parseSeedsAsSingles(blocks.first().single())
        val steps = blocks.drop(1)
            .map { res -> res.readLines(1) { parseRelation(it) } }

        var current = seeds
        steps.forEach { current = current.transform(it) }
        return current.minOf { it.first }
    }

    override fun part2(resource: Resource): Result {
        val blocks = resource.readBlocks { it }
        val seeds = parseSeedsAsRanges(blocks.first().single())
        val steps = blocks.drop(1)
            .map { res -> res.readLines(1) { parseRelation(it) } }

        var current = seeds
        steps.forEach { current = current.transform(it) }
        return current.minOf { it.first }
    }

    private fun List<LongRange>.transform(relations: List<Relation>): List<LongRange> {
        return map { it.transform(relations) }.flatten()
    }

    private fun LongRange.transform(relations: List<Relation>): List<LongRange> {
        val inside = relations.map { intersectRange(it.range) to it.offset }.filterNot { it.first.isEmpty() }
        val outside = removeRange(inside.map { it.first })
        return outside + inside.map { it.first.plusRange(it.second) }
    }

    private fun LongRange.removeRange(other: List<LongRange>): List<LongRange> {
        var result = listOf(this)
        other.forEach { o -> result = result.map { it.removeRange(o) }.flatten() }
        return result
    }

    private fun parseSeedsAsSingles(seeds: String): List<LongRange> {
        return seeds.substringAfterLast(":")
            .trim()
            .split(' ')
            .map { it.toLong() }
            .map { LongRange(it, it) }
    }

    private fun parseSeedsAsRanges(seeds: String): List<LongRange> {
        return seeds.substringAfterLast(":")
            .trim()
            .split(' ')
            .map { it.toLong() }
            .chunked(2)
            .map { LongRange(it[0], it[0] + it[1] - 1) }
    }

    private fun parseRelation(relations: String): Relation {
        val items = relations.split(' ').map { it.toLong() }
        return Relation(LongRange(items[1], items[1] + items[2] - 1), items[0] - items[1])
    }
}
