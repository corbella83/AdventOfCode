package corbella83.aoc.utils

import kotlin.math.abs

fun same(item: Int, vararg other: Int?): Boolean {
    return other.all { it == item }
}

fun Long.square(): Long {
    return this * this
}

fun Double.sameAs(other: Double): Boolean {
    return abs(other - this) < 1e10
}

fun <K : Any, V : Any> HashMap<K, V>.putAll(map: Map<K, V>, merge: (V, V) -> V) {
    map.forEach {
        merge(it.key, it.value, merge)
    }
}