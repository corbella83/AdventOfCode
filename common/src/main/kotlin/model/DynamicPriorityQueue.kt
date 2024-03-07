package corbella83.aoc.model

class DynamicPriorityQueue<T : Comparable<T>>(vararg elements: T) {
    private val set = hashSetOf(*elements)

    fun isEmpty() = set.isEmpty()

    fun push(item: T) = set.add(item)

    fun pop() = set.min().also { set.remove(it) }
}
