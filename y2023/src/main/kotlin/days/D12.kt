package days

import corbella83.aoc.Day
import corbella83.aoc.Resource
import corbella83.aoc.Result

object D12 : Day {

    data class Status(val cursor: Int, val group: Int)

    private data class Spring(
        val pattern: String,
        val damaged: List<Int>
    )

    data class Cursor(val input: String, var position: Int = 0) {

        fun next() =
            if (position < input.length) input[position++] else null

        fun skip(vararg chars: Char): Char? {
            var last = next() ?: return null
            while (last in chars) last = next() ?: return null
            return last
        }

        fun skip(max: Int, vararg chars: Char): Char? {
            var last: Char? = null
            repeat(max) { last = next()?.takeIf { it in chars } ?: return null }
            return last
        }
    }

    override val day = 12

    override fun part1(resource: Resource): Result {
        return resource.readLines { it.parse() }
            .sumOf { getArrangements(Cursor(it.pattern), 0, it.damaged, hashMapOf()) }
    }

    override fun part2(resource: Resource): Result {
        return resource.readLines { it.parse() }
            .map { Spring(it.pattern.repeat(5), it.damaged.repeat(5)) }
            .sumOf { getArrangements(Cursor(it.pattern), 0, it.damaged, hashMapOf()) }
    }

    private fun getArrangements(cursor: Cursor, group: Int, damage: List<Int>, cache: HashMap<Status, Long>): Long {
        val status = Status(cursor.position, group)
        cache[status]?.also { return it }

        var arrangements = 0L

        val last = cursor.skip('.') ?: return 0

        if (last == '#' || last == '?') {
            val newCursor = cursor.copy()

            if (isValidGroup(newCursor, damage[group])) {
                if (group == damage.size - 1) {
                    arrangements += if (newCursor.skip('.', '?') == null) 1 else 0
                } else {
                    arrangements += getArrangements(newCursor, group + 1, damage, cache)
                }
            }
        }

        if (last == '?') {
            arrangements += getArrangements(cursor, group, damage, cache)
        }

        cache[status] = arrangements
        return arrangements
    }

    private fun isValidGroup(cursor: Cursor, groupLen: Int): Boolean {
        if (groupLen > 1) cursor.skip(groupLen - 1, '?', '#') ?: return false
        return with(cursor.next()) { this == '.' || this == '?' || this == null }
    }

    private fun String.repeat(number: Int) =
        Array(number) { this }.joinToString("?")

    private fun <T> List<T>.repeat(number: Int) =
        Array(number) { this }.toList().flatten()

    private fun String.parse(): Spring {
        val split = this.split(' ')
        return Spring(
            split[0],
            split[1].split(',').map { it.toInt() }
        )
    }
}
