import corbella83.aoc.Day
import corbella83.aoc.Log
import corbella83.aoc.ResourceLoader
import corbella83.aoc.Result
import kotlin.test.assertEquals

fun ResourceLoader.test(day: Day, answer1: Result, answer2: Result) {
    val r1 = day.part1(load(day.day, 1))
    assertEquals(r1.toString(), answer1.toString())

    val r2 = day.part2(load(day.day, 2))
    assertEquals(r2.toString(), answer2.toString())
}
