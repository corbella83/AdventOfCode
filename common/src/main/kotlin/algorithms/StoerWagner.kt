package corbella83.aoc.algorithms

import corbella83.aoc.utils.putAll
import java.util.*

class StoerWagner<T>(relations: List<Pair<T, T>>) {
    private val vertices = relations.map { listOf(it.first, it.second) }.flatten().toSet()
    private val nodes: HashSet<Node<T>>

    init {
        val tmp = hashMapOf<T, Node<T>>()
        relations.forEach { (v1, v2) ->
            val node1 = tmp.getOrPut(v1) { Node(setOf(v1)) }
            val node2 = tmp.getOrPut(v2) { Node(setOf(v2)) }
            node1.connections[node2] = 1.0
            node2.connections[node1] = 1.0
        }
        nodes = tmp.values.toHashSet()
    }

    fun minCut(): Pair<Set<T>, Set<T>> {
        val bestCut = BestCut()
        while (nodes.size > 1) bestCut.minimumCutPhase()
        return bestCut.vertex to vertices.toMutableSet().apply { removeAll(bestCut.vertex) }
    }

    private fun BestCut.minimumCutPhase() {
        val trigger = nodes.first()

        val queue = PriorityQueue<Connection>()
        val cache = HashMap<Node<T>, Connection>()

        nodes.filter { it != trigger }
            .map { vertex -> vertex.connections[trigger]?.let { Connection(vertex, it, true) } ?: Connection(vertex) }
            .forEach {
                queue.add(it)
                cache[it.vertex] = it
            }

        var last = trigger
        var beforeLast: Node<T>? = null
        while (!queue.isEmpty()) {
            val current = queue.poll().vertex
            cache.remove(current)
            beforeLast = last
            last = current

            current.connections
                .forEach { (vertex, weight) ->
                    val connection = cache[vertex]
                    if (connection != null) {
                        queue.remove(connection)
                        connection.active = true
                        connection.weight += weight
                        queue.add(connection)
                    }
                }
        }

        val lastWeight = last.connections.values.sumOf { it }
        if (lastWeight < weight) {
            weight = lastWeight
            vertex = last.vertex
        }

        mergeVertices(beforeLast!!, last)
    }

    private fun mergeVertices(node1: Node<T>, node2: Node<T>) {
        val new = Node(node1.vertex + node2.vertex)
        node1.connections.remove(node2)
        node2.connections.remove(node1)

        with(new.connections) {
            putAll(node1.connections) { o, n -> o + n }
            putAll(node2.connections) { o, n -> o + n }
            forEach { (vertex, weight) ->
                vertex.connections.remove(node1)
                vertex.connections.remove(node2)
                vertex.connections[new] = weight
            }
        }

        nodes.remove(node2)
        nodes.remove(node1)
        nodes.add(new)
    }

    private inner class Connection(
        val vertex: Node<T>,
        var weight: Double = 1.0,
        var active: Boolean = false
    ) : Comparable<Connection> {
        override fun compareTo(other: Connection): Int {
            if (active && other.active) {
                return -weight.compareTo(other.weight)
            }
            if (active) return -1
            return if (other.active) 1 else 0
        }
    }

    inner class BestCut {
        var weight: Double = Double.POSITIVE_INFINITY
        var vertex = setOf<T>()
    }

    private data class Node<T>(val vertex: Set<T>) {
        val connections = hashMapOf<Node<T>, Double>()
    }
}