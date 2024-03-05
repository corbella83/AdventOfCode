package days

import corbella83.aoc.Day
import corbella83.aoc.Resource
import corbella83.aoc.Result
import corbella83.aoc.utils.intersectRange
import corbella83.aoc.utils.invert

object D19 : Day {
    private const val MIN_VALUE = 1L
    private const val MAX_VALUE = 4000L
    private val regex = Regex("(\\w+)\\{(.*)\\}")

    private enum class Variable { X, M, A, S }

    private data class Condition(
        val variable: Variable,
        val value: LongRange
    )

    private data class Rule(
        val condition: Condition?,
        val result: String
    )

    override val day = 19

    override fun part1(resource: Resource): Result {
        val blocks = resource.readBlocks { it }

        val approves = blocks.first()
            .readLines(regex) { matcher -> matcher[1] to matcher[2].split(',').map { parseRule(it) } }
            .toMap()
            .getApproveRanges("in")

        return blocks.last()
            .readLines { parsePart(it) }
            .filter { approves.matches(it) }
            .sumOf { it.values.sum() }
    }

    override fun part2(resource: Resource): Result {
        val blocks = resource.readBlocks { it }

        val approves = blocks.first()
            .readLines(regex) { matcher -> matcher[1] to matcher[2].split(',').map { parseRule(it) } }
            .toMap()
            .getApproveRanges("in")

        return approves.sumOf { conditions ->
            Variable.entries
                .map { conditions[it]?.run { last - first + 1 } ?: MAX_VALUE }
                .reduce(Long::times)
        }
    }

    private fun Map<String, List<Rule>>.getApproveRanges(start: String): List<Map<Variable, LongRange>> {
        val conditions = ArrayList<List<Condition>>()
        parseApproveConditions(start, arrayListOf(), conditions)
        return conditions.map { condition ->
            condition.groupBy { it.variable }
                .mapValues { map -> map.value.map { it.value }.intersectRange() }
        }
    }

    private fun Map<String, List<Rule>>.parseApproveConditions(name: String, previous: List<Condition>, result: ArrayList<List<Condition>>) {
        val used = arrayListOf<Condition>()
        this[name]!!.forEach { rule ->
            if (rule.result == "A") {
                val next = previous + used + rule.condition
                result.add(next.filterNotNull())
            } else if (rule.result != "R") {
                val next = previous + used + rule.condition
                parseApproveConditions(rule.result, next.filterNotNull(), result)
            }

            rule.condition?.invert()?.also { used.add(it) }
        }
    }

    private fun parseRule(rule: String): Rule {
        val split = rule.split('>', '<', ':')
        val variable = split[0]
        return if (rule.contains('<')) {
            val value = split[1].toLong()
            val result = split[2]
            val condition = Condition(parseVariable(variable), LongRange(MIN_VALUE, value - 1))
            Rule(condition, result)
        } else if (rule.contains('>')) {
            val value = split[1].toLong()
            val result = split[2]
            val condition = Condition(parseVariable(variable), LongRange(value + 1, MAX_VALUE))
            Rule(condition, result)
        } else {
            Rule(null, variable)
        }
    }

    private fun parseVariable(variable: String): Variable {
        return when (variable) {
            "x" -> Variable.X
            "m" -> Variable.M
            "a" -> Variable.A
            "s" -> Variable.S
            else -> throw Exception()
        }
    }

    private fun parsePart(part: String): Map<Variable, Long> {
        return part.substring(1, part.length - 1)
            .split(',')
            .associate { it.split('=').run { parseVariable(get(0)) to get(1).toLong() } }
    }

    private fun Condition.invert(): Condition {
        return Condition(this.variable, this.value.invert(MIN_VALUE, MAX_VALUE))
    }

    private fun List<Map<Variable, LongRange>>.matches(part: Map<Variable, Long>): Boolean {
        return this.any { map ->
            Variable.entries.all { map[it]?.contains(part[it]) ?: true }
        }
    }
}
