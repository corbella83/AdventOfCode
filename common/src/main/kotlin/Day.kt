package corbella83.aoc

import java.io.Serializable
import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.measureTime

typealias Result = Serializable

interface Day {
    val day: Int
    fun part1(resource: Resource): Result
    fun part2(resource: Resource): Result
}

fun Day.execute(inputLoader: ResourceLoader) {
    val part1: Result
    val input1 = inputLoader.load(day, 1)
    val time1 = measureTime { part1 = part1(input1) }
    Log.print("Day $day (Part 1): $part1 in ${time1.toStr()}")

    val part2: Result
    val input2 = inputLoader.load(day, 2)
    val time2 = measureTime { part2 = part2(input2) }
    Log.print("Day $day (Part 2): $part2 in ${time2.toStr()}")

    Log.print("--------")
}

private fun Duration.toStr(): String {
    return if (inWholeSeconds == 0L) {
        this.toString(DurationUnit.MILLISECONDS, 1)
    } else {
        this.toString(DurationUnit.SECONDS, 1)
    }
}
