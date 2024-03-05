package days

import corbella83.aoc.Day
import corbella83.aoc.Resource
import corbella83.aoc.Result
import corbella83.aoc.model.Direction
import corbella83.aoc.model.Index
import corbella83.aoc.model.Matrix
import java.util.*

object D23 : Day {

    data class Node(val index: Index) {
        val relations = hashMapOf<Node, Int>()
    }

    override val day = 23

    override fun part1(resource: Resource): Result {
        val matrix = resource.readMatrix()
        val startNode = Node(Index(1, 2))
        val endNode = Node(Index(matrix.size().first, matrix.size().second - 1))

        matrix.calculateNodes(startNode, true)
        return startNode.maxDistanceTo(endNode)
    }

    override fun part2(resource: Resource): Result {
        val matrix = resource.readMatrix()
        val startNode = Node(Index(1, 2))
        val endNode = Node(Index(matrix.size().first, matrix.size().second - 1))

        matrix.calculateNodes(startNode, false)
        return startNode.maxDistanceTo(endNode)
    }

    private fun Matrix.calculateNodes(node: Node, slopes: Boolean) {
        val pending = LinkedList<Pair<Node, Index>>()
        pending.push(node to node.index + Direction.DOWN)

        val visited = hashSetOf(node)
        while (pending.isNotEmpty()) {
            val current = pending.pop()
            val (nextNode, nextIndex) = travel(current.first, current.second, slopes, visited)

            if (nextNode !in visited) {
                visited.add(nextNode)
                nextIndex.forEach { pending.push(nextNode to it) }
            }
        }
    }

    private fun Matrix.travel(from: Node, next: Index, slopes: Boolean, nodes: HashSet<Node>): Pair<Node, List<Index>> {
        var last = from.index
        var now = next
        var distance = 0
        while (true) {
            val options = neighbour(now, slopes).filterNot { it == last }
            if (options.size == 1) {
                last = now
                now = options.single()
                distance++
            } else {
                distance++
                val node = nodes.singleOrNull { it.index == now } ?: Node(now)
                from.relations[node] = distance
                if (!slopes) node.relations[from] = distance
                return node to options
            }
        }
    }

    private fun Node.maxDistanceTo(to: Node, visited: ArrayList<Node> = arrayListOf(), cumulativeDistance: Int = 0): Int {
        visited.add(this)
        val value = relations.filter { it.key !in visited }
            .maxOfOrNull {
                if (it.key == to) {
                    cumulativeDistance + it.value
                } else {
                    it.key.maxDistanceTo(to, visited, cumulativeDistance + it.value)
                }
            } ?: 0

        visited.remove(this)
        return value
    }

    private fun Matrix.neighbour(index: Index, slopes: Boolean): List<Index> {
        val directions = if (slopes) {
            when (get(index)) {
                '>' -> listOf(Direction.RIGHT)
                '<' -> listOf(Direction.LEFT)
                'v' -> listOf(Direction.DOWN)
                '^' -> listOf(Direction.UP)
                '.' -> Direction.entries
                else -> throw Exception("Character not valid")
            }
        } else {
            Direction.entries
        }

        return directions
            .map { index + it }
            .filter { !isOutOfBounds(it) && get(it) != '#' }
    }
}
