package corbella83.aoc.utils

fun List<Long>.diff(): List<Long> {
    return zipWithNext { c, n -> n - c }
}

fun <T> List<T>.pairs(): List<Pair<T, T>> {
    val result = arrayListOf<Pair<T, T>>()
    for (i in 0..<size - 1) {
        for (j in i + 1..<size) {
            result.add(this[i] to this[j])
        }
    }
    return result
}

fun <T, S, R> combine(list1: List<T>, list2: List<S>, merger: (T, S) -> R): List<R> {
    return list1.mapIndexed { index, current ->
        merger(current, list2[index])
    }
}

fun Collection<Long>.multiply(): Long {
    return reduce(Long::times)
}

fun CharArray.replace(origin: Char, destination: Char): CharArray {
    return map { if (it == origin) destination else it }.toCharArray()
}

fun <T, S, R> merge(list1: List<T>, list2: List<S>, mapper: (T, S) -> R): List<R> {
    return list1.mapIndexed { index, t -> mapper(t, list2[index]) }
}

fun CharArray.swap(index1: Int, index2: Int) {
    if (index1 == index2) return
    with(this[index1]) {
        this@swap[index1] = this@swap[index2]
        this@swap[index2] = this
    }
}
