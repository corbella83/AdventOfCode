package corbella83.aoc.algorithms

import corbella83.aoc.model.BufferList
import corbella83.aoc.model.DynamicPriorityQueue
import corbella83.aoc.utils.putAll

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
        var bestCut = minimumCutPhase()
        while (nodes.size > 1) {
            val cut = minimumCutPhase()
            if (cut > bestCut) bestCut = cut
        }
        return bestCut.vertex.items to vertices.toMutableSet().apply { removeAll(bestCut.vertex.items) }
    }

    private fun minimumCutPhase(): Connection<T> {
        val trigger = Connection(nodes.first())

        val queue = DynamicPriorityQueue(trigger)
        val cache = hashMapOf(trigger.vertex to trigger)

        val last = BufferList<Connection<T>>()
        while (!queue.isEmpty()) {
            val current = queue.pop().also { last.put(it) }

            current.vertex.connections
                .forEach { (vertex, weight) ->
                    val connection = cache.getOrPut(vertex) { Connection(vertex).also { queue.push(it) } }
                    connection.weight += weight
                }
        }

        return with(last.get()) {
            mergeVertices(first.vertex, second.vertex)
            second
        }
    }

    private fun mergeVertices(node1: Node<T>, node2: Node<T>) {
        val new = Node(node1.items + node2.items)
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

    private data class Node<T>(val items: Set<T>) {
        val connections = hashMapOf<Node<T>, Double>()
    }

    private data class Connection<T>(val vertex: Node<T>) : Comparable<Connection<T>> {
        var weight: Double = 0.0

        override fun compareTo(other: Connection<T>): Int {
            return other.weight.compareTo(weight)
        }
    }
}
