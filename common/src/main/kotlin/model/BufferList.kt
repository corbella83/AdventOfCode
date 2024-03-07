package corbella83.aoc.model

class BufferList<T>(vararg elements: T) {
    private var item1: T? = null
    private var item2: T? = null
    private var next1 = true

    init {
        elements.forEach { put(it) }
    }

    fun put(item: T) {
        if (next1) {
            item1 = item
            next1 = false
        } else {
            item2 = item
            next1 = true
        }
    }

    fun get(): Pair<T, T> {
        return if (next1) {
            item1!! to item2!!
        } else {
            item2!! to item1!!
        }
    }
}
