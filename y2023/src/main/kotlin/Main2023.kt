import corbella83.aoc.*
import days.*

fun main(args: Array<String>) {
    test()

    val resources = RealResourceLoader(2023)
    val all = listOf(D1, D2, D3, D4, D5, D6, D7, D8, D9, D10, D11, D12, D13, D14, D15, D16, D17, D18, D19, D20, D21, D22, D23, D24, D25)
    all.forEach { it.execute(resources) }
}

private fun test() {

    val resources = TestResourceLoader()

    val results = mapOf<Day, Pair<Result, Result>>(
        D1 to (142 to 281),
        D2 to (8 to 2286),
        D3 to (4361 to 467835),
        D4 to (13 to 30),
        D5 to (35 to 46),
        D6 to (288 to 71503),
        D7 to (6440 to 5905),
        D8 to (6 to 6),
        D9 to (114 to 2),
        D10 to (8 to 10),
        D11 to (374 to 82000210),
        D12 to (21 to 525152),
        D13 to (405 to 400),
        D14 to (136 to 64),
        D15 to (1320 to 145),
        D16 to (46 to 51),
        D17 to (102 to 94),
        D18 to (62 to 952408144115),
        D19 to (19114 to 167409079868000),
        D20 to (11687500 to 0),
        D21 to (42 to 359867603030804),
        D22 to (5 to 7),
        D23 to (94 to 154),
        D24 to (0 to 47),
        D25 to (54 to 0)
    )

    results.forEach { (d, r) ->
        val r1 = d.part1(resources.load(d.day, 1))
        if (r1.toString() != r.first.toString()) Log.print("Day ${d.day} Part 1 failed ($r1 vs ${r.first})")

        val r2 = d.part2(resources.load(d.day, 2))
        if (r2.toString() != r.second.toString()) Log.print("Day ${d.day} Part 2 failed ($r2 vs ${r.second})")

        Log.print("Finish Test")
    }
}
