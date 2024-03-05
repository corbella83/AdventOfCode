package days

import corbella83.aoc.Day
import corbella83.aoc.Resource
import corbella83.aoc.Result
import corbella83.aoc.model.Index
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

object D22 : Day {

    private data class Brick(
        val cubes: Set<Index>,
        val height: Int,
        var stack: Int
    )

    override val day = 22

    override fun part1(resource: Resource): Result {
        val bricks = resource.readLines { parseBrick(it) }
            .sortedBy { it.stack }
            .apply { fall(false) }

        return bricks.count {
            ArrayList(bricks)
                .apply { remove(it) }
                .fall(true) == 0
        }
    }

    override fun part2(resource: Resource): Result {
        val bricks = resource.readLines { parseBrick(it) }
            .sortedBy { it.stack }
            .apply { fall(false) }

        return bricks.sumOf {
            ArrayList(bricks)
                .apply { remove(it) }
                .fall(true)
        }
    }

    private fun List<Brick>.fall(simulate: Boolean): Int {
        val currentStack = hashMapOf<Index, Int>()

        var fall = 0
        forEach { current ->
            val oldStack = current.stack
            val newStack = current.cubes.maxOf { currentStack[it] ?: 0 } + 1

            if (!simulate) current.stack = newStack
            current.cubes.forEach { currentStack[it] = newStack + current.height - 1 }
            if (oldStack != newStack) fall++
        }
        return fall
    }

    private fun parseBrick(brick: String): Brick {
        return brick.split('~')
            .map { c -> c.split(',').map { it.toInt() } }
            .zipWithNext { a, b -> Brick(parseCubes(a[0], a[1], b[0], b[1]), abs(a[2] - b[2]) + 1, min(a[2], b[2])) }
            .single()
    }

    private fun parseCubes(px: Int, py: Int, qx: Int, qy: Int): Set<Index> {
        return buildSet {
            for (x in min(px, qx)..max(px, qx)) {
                for (y in min(py, qy)..max(py, qy)) {
                    add(Index(x, y))
                }
            }
        }
    }
}
