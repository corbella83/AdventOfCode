import corbella83.aoc.TestResourceLoader
import days.*
import kotlin.test.Test

class Test2023 {
    private val resources = TestResourceLoader()

    @Test
    fun day1() = resources.test(D1, 142, 281)

    @Test
    fun day2() = resources.test(D2, 8, 2286)

    @Test
    fun day3() = resources.test(D3, 4361, 467835)

    @Test
    fun day4() = resources.test(D4, 13, 30)

    @Test
    fun day5() = resources.test(D5, 35, 46)

    @Test
    fun day6() = resources.test(D6, 288, 71503)

    @Test
    fun day7() = resources.test(D7, 6440, 5905)

    @Test
    fun day8() = resources.test(D8, 6, 6)

    @Test
    fun day9() = resources.test(D9, 114, 2)

    @Test
    fun day10() = resources.test(D10, 8, 10)

    @Test
    fun day11() = resources.test(D11, 374, 82000210)

    @Test
    fun day12() = resources.test(D12, 21, 525152)

    @Test
    fun day13() = resources.test(D13, 405, 400)

    @Test
    fun day14() = resources.test(D14, 136, 64)

    @Test
    fun day15() = resources.test(D15, 1320, 145)

    @Test
    fun day16() = resources.test(D16, 46, 51)

    @Test
    fun day17() = resources.test(D17, 102, 94)

    @Test
    fun day18() = resources.test(D18, 62, 952408144115)

    @Test
    fun day19() = resources.test(D19, 19114, 167409079868000)

    @Test
    fun day20() = resources.test(D20, 11687500, 0)

    @Test
    fun day21() = resources.test(D21, 42, 359867603030804)

    @Test
    fun day22() = resources.test(D22, 5, 7)

    @Test
    fun day23() = resources.test(D23, 94, 154)

    @Test
    fun day24() = resources.test(D24, 0, 47)

    @Test
    fun day25() = resources.test(D25, 54, 0)
}
