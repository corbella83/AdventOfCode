package corbella83.aoc.model

import java.math.BigInteger

data class XYZ<T : Number>(val x: T, val y: T, val z: T)

fun XYZ<Long>.toDouble() =
    XYZ(x.toDouble(), y.toDouble(), z.toDouble())

fun XYZ<Long>.toBigInt() =
    XYZ(BigInteger.valueOf(x), BigInteger.valueOf(y), BigInteger.valueOf(z))

operator fun Int.times(other: BigInteger) = other.times(BigInteger.valueOf(toLong()))

// Operators for Long
object LongOperators {
    operator fun XYZ<Long>.plus(other: XYZ<Long>) = XYZ(this.x + other.x, this.y + other.y, this.z + other.z)
    operator fun XYZ<Long>.minus(other: XYZ<Long>) = XYZ(this.x - other.x, this.y - other.y, this.z - other.z)

    operator fun XYZ<Long>.plus(other: Long) = XYZ(this.x + other, this.y + other, this.z + other)
    operator fun XYZ<Long>.minus(other: Long) = XYZ(this.x - other, this.y - other, this.z - other)
    operator fun XYZ<Long>.times(other: Long) = XYZ(this.x * other, this.y * other, this.z * other)
    operator fun XYZ<Long>.div(other: Long) = XYZ(this.x / other, this.y / other, this.z / other)
}

// Operators for BigInteger
object BigIntegerOperators {
    operator fun XYZ<BigInteger>.plus(other: XYZ<BigInteger>) = XYZ(this.x + other.x, this.y + other.y, this.z + other.z)
    operator fun XYZ<BigInteger>.minus(other: XYZ<BigInteger>) = XYZ(this.x - other.x, this.y - other.y, this.z - other.z)

    operator fun XYZ<BigInteger>.plus(other: BigInteger) = XYZ(this.x + other, this.y + other, this.z + other)
    operator fun XYZ<BigInteger>.minus(other: BigInteger) = XYZ(this.x - other, this.y - other, this.z - other)
    operator fun XYZ<BigInteger>.times(other: BigInteger) = XYZ(this.x * other, this.y * other, this.z * other)
    operator fun XYZ<BigInteger>.div(other: BigInteger) = XYZ(this.x / other, this.y / other, this.z / other)
}

// Operators for Double
object DoubleOperators {
    operator fun XYZ<Double>.plus(other: XYZ<Double>) = XYZ(this.x + other.x, this.y + other.y, this.z + other.z)
    operator fun XYZ<Double>.minus(other: XYZ<Double>) = XYZ(this.x - other.x, this.y - other.y, this.z - other.z)

    operator fun XYZ<Double>.plus(other: Double) = XYZ(this.x + other, this.y + other, this.z + other)
    operator fun XYZ<Double>.minus(other: Double) = XYZ(this.x - other, this.y - other, this.z - other)
    operator fun XYZ<Double>.times(other: Double) = XYZ(this.x * other, this.y * other, this.z * other)
    operator fun XYZ<Double>.div(other: Double) = XYZ(this.x / other, this.y / other, this.z / other)
}
