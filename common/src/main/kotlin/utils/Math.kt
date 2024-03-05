package corbella83.aoc.utils

import java.math.BigInteger

fun Int.factorial(): BigInteger {
    var factorial = BigInteger.ONE
    for (i in 1L..this) {
        factorial = factorial.multiply(BigInteger.valueOf(i))
    }
    return factorial
}

fun Long.summation(): Long {
    var summation = 0L
    for (i in 1L..this) {
        summation += i
    }
    return summation
}

fun sqrt(number: Long): Double {
    return kotlin.math.sqrt(number.toDouble())
}

fun List<Long>.lcm(): Long {
    if (this.isEmpty()) return 0L

    var result = this[0]

    for (i in 1..<this.size) {
        result = lcm(result, this[i])
    }

    return result
}

private fun lcm(a: Long, b: Long): Long {
    return if (a == 0L || b == 0L) 0L else (a * b) / gcd(a, b)
}

private fun gcd(a: Long, b: Long): Long {
    return if (b == 0L) a else gcd(b, a % b)
}
