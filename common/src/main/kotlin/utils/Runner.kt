package corbella83.aoc.utils

fun smartRepeat(number: Int, id: (Unit) -> Int, code: (Unit) -> Unit) {
    return Runner(number, id, code).run(Unit)
}

fun <T> smartRepeat(number: Int, init: T, code: (T) -> T): T {
    return Runner(number, { it.hashCode() }, code).run(init)
}

private class Runner<T>(
    private val number: Int,
    private val id: (T) -> Int,
    private val code: (T) -> T,
) {
    private val cache = hashMapOf<Int, Int>()

    fun run(init: T): T {
        var current = init
        for (cycle in 0..<number) {
            val id = id(current)
            val index = cache[id]
            if (index != null) {
                val length = cycle - index
                val remainingCycles = (number - cycle) % length
                repeat(remainingCycles) { current = code(current) }
                break
            } else {
                current = code(current)
                cache[id] = cycle
            }
        }
        return current
    }
}
