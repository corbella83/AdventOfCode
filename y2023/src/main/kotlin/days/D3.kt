package days

import corbella83.aoc.Day
import corbella83.aoc.Resource
import corbella83.aoc.Result
import corbella83.aoc.model.Index
import corbella83.aoc.utils.multiply

object D3 : Day {

    override val day = 3

    override fun part1(resource: Resource): Result {
        val matrix = resource.readMatrix()
        val numbers = matrix.findContiguousH { it.isDigit() }

        val engines = numbers.filter { segment ->
            val region = segment.asRegion().inflate(matrix.bounds())
            matrix.get(region).count { !it.isDigit() && it != '.' } > 0
        }
        return engines.sumOf { matrix.get(it).toInt() }
    }

    override fun part2(resource: Resource): Result {
        val matrix = resource.readMatrix()
        val numbers = matrix.findContiguousH { it.isDigit() }

        val engines = matrix.findAll('*')

        val map = hashMapOf<Index, ArrayList<Long>>()
        numbers.forEach { segment ->
            val number = matrix.get(segment).toLong()
            val region = segment.asRegion().inflate(matrix.bounds())

            engines.filter { region.contains(it) }
                .forEach { map.getOrPut(it) { arrayListOf() }.add(number) }
        }

        return map.values.filter { it.size == 2 }.sumOf { it.multiply() }
    }
}
