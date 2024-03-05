package corbella83.aoc.utils

fun List<LongRange>.intersectRange(): LongRange {
    val first = maxOf { it.first }
    val last = minOf { it.last }
    if (last >= first) return LongRange(first, last)
    return LongRange.EMPTY
}

fun LongRange.intersectRange(other: LongRange): LongRange {
    val first = maxOf(this.first, other.first)
    val last = minOf(this.last, other.last)
    if (last >= first) return LongRange(first, last)
    return LongRange.EMPTY
}

fun LongRange.removeRange(other: LongRange): List<LongRange> {
    if (isEmpty()) return listOf()
    return buildList {
        if (other.first > first) add(LongRange(first, other.first - 1))
        if (other.last < last) add(LongRange(other.last + 1, last))
    }
}

fun LongRange.invert(minValue: Long, maxValue: Long): LongRange {
    return if (first == minValue && last == maxValue) {
        this
    } else if (first == minValue) {
        LongRange(last + 1, maxValue)
    } else if (last == maxValue) {
        LongRange(minValue, first - 1)
    } else {
        throw Exception()
    }
}

fun LongRange.size(): Long {
    return last - first + 1
}

fun LongRange.plusRange(number: Long): LongRange {
    if (isEmpty()) return LongRange.EMPTY
    return LongRange(first + number, last + number)
}
