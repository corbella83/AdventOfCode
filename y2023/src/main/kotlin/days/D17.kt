package days

import corbella83.aoc.Day
import corbella83.aoc.Resource
import corbella83.aoc.Result
import corbella83.aoc.model.Direction
import corbella83.aoc.model.Index
import corbella83.aoc.model.Matrix
import corbella83.aoc.model.invert
import java.util.*

object D17 : Day {

    private data class Tail(val direction: Direction, val line: Int)

    private data class Node(val index: Index, val tail: Tail?) : Comparable<Node> {
        var weight: Int = Int.MAX_VALUE

        override fun compareTo(other: Node) = weight.compareTo(other.weight)
    }

    override val day = 17

    override fun part1(resource: Resource): Result {
        val matrix = resource.readMatrix()
        val bounds = matrix.bounds()
        return matrix.dijkstra(bounds.tl, bounds.br, 0 to 3)
    }

    override fun part2(resource: Resource): Result {
        val matrix = resource.readMatrix()
        val bounds = matrix.bounds()
        return matrix.dijkstra(bounds.tl, bounds.br, 4 to 10)
    }

    private fun Matrix.dijkstra(start: Index, end: Index, straight: Pair<Int, Int>): Int {
        val visited = hashSetOf<Node>()

        val startNode = Node(start, null).apply { weight = 0 }

        val pending = PriorityQueue(listOf(startNode))

        while (pending.isNotEmpty()) {
            val current = pending.remove()
            if (current.index == end) return current.weight

            current.neighbour(straight.first, straight.second)
                .filterNot { isOutOfBounds(it.index) }
                .filter { it !in visited }
                .forEach {
                    it.weight = current.weight + get(it.index).digitToInt()
                    pending.add(it)
                    visited.add(it)
                }
        }

        throw Exception("Path not found")
    }

    private fun Node.neighbour(minStraight: Int, maxStraight: Int): List<Node> {
        val validDirections = if (tail == null) {
            Direction.entries
        } else if (tail.line < minStraight) {
            listOf(tail.direction)
        } else if (tail.line < maxStraight) {
            Direction.entries.filter { it != tail.direction.invert() }
        } else {
            Direction.entries.filterNot { it != tail.direction.invert() && tail.direction == it }
        }

        return validDirections.map {
            if (it == tail?.direction) {
                Node(index + it, Tail(it, tail.line + 1))
            } else {
                Node(index + it, Tail(it, 1))
            }
        }
    }
}
