package corbella83.aoc.model

import kotlin.math.abs

data class Index(val y: Int, val x: Int) : Comparable<Index> {

    operator fun plus(other: Int) = Index(y + other, x + other)

    operator fun plus(other: Index) = Index(y + other.y, x + other.x)

    operator fun plus(other: Direction) = this + when (other) {
        Direction.UP -> UP
        Direction.DOWN -> DOWN
        Direction.LEFT -> LEFT
        Direction.RIGHT -> RIGHT
    }

    operator fun minus(other: Int) = Index(y - other, x - other)

    operator fun minus(other: Index) = Index(y - other.y, x - other.x)

    operator fun minus(other: Direction) = this - when (other) {
        Direction.UP -> UP
        Direction.DOWN -> DOWN
        Direction.LEFT -> LEFT
        Direction.RIGHT -> RIGHT
    }

    operator fun div(other: Int) = Index(y / other, x / other)

    operator fun times(other: Int) = Index(y * other, x * other)

    fun flip() = Index(x, y)

    fun asRegion() = Region(this, this)

    override operator fun compareTo(other: Index): Int {
        return if (this.y == other.y && this.x == other.x) {
            0
        } else if (this.y < other.y && this.x < other.x) {
            -1
        } else {
            1
        }
    }

    companion object {
        val UP = Index(-1, 0)
        val DOWN = Index(1, 0)
        val LEFT = Index(0, -1)
        val RIGHT = Index(0, 1)
    }
}

fun List<Index>.area(): Long {
    var area = 0L
    var b = 0L
    for (i in indices) {
        val (x1, y1) = this[i].minus(1).run { x.toLong() to y.toLong() }
        val (x2, y2) = this[(i + 1) % size].minus(1).run { x.toLong() to y.toLong() }
        area += x1 * y2 - x2 * y1
        b += abs(x1 - x2) + abs(y1 - y2)
    }
    return (abs(area) + b) / 2 + 1
}
