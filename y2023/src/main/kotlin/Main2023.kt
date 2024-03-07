import corbella83.aoc.RealResourceLoader
import corbella83.aoc.execute
import days.*

fun main(args: Array<String>) {
    val resources = RealResourceLoader(2023)
    val all = listOf(D1, D2, D3, D4, D5, D6, D7, D8, D9, D10, D11, D12, D13, D14, D15, D16, D17, D18, D19, D20, D21, D22, D23, D24, D25)
    all.forEach { it.execute(resources) }
}
