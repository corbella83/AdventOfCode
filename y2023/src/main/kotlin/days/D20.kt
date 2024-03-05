package days

import corbella83.aoc.Day
import corbella83.aoc.Resource
import corbella83.aoc.Result
import corbella83.aoc.utils.lcm

object D20 : Day {

    private val matcher = Regex("([%&]?)(\\w.*) -> (\\w.*)")

    private class OperationFoundException : Exception()

    private enum class Pulse { LOW, HIGH }

    private data class Operation(
        val fromModule: String,
        val toModule: String,
        val pulse: Pulse
    )

    private data class Statistics(
        var low: Long,
        var high: Long
    )

    private sealed interface Module {
        val destinations: List<String>

        data class Broadcast(override val destinations: List<String>) : Module
        data class FlipFlop(override val destinations: List<String>, var on: Boolean) : Module
        data class Conjunction(override val destinations: List<String>, var states: HashMap<String, Pulse>) : Module
    }

    override val day = 20

    override fun part1(resource: Resource): Result {
        val modules = resource.readLines(matcher) { parseModule(it) }.toMap()
        modules.reset()

        val result = Statistics(0, 0)
        repeat(1_000) { modules.pressButton(result) }
        return result.low * result.high
    }

    override fun part2(resource: Resource): Result {
        val modules = resource.readLines(matcher) { parseModule(it) }.toMap()

        return modules.findModulesWithDestination("rx")
            .map { modules.findModulesWithDestination(it) }
            .flatten()
            .map { modules.stepsTo(it, Pulse.LOW) }
            .lcm()
    }

    private fun Map<String, Module>.stepsTo(module: String, pulse: Pulse): Long {
        reset()

        var numberPress = 0L
        while (true) {
            try {
                pressButton(Statistics(0, 0)) { it.toModule == module && it.pulse == pulse }
                numberPress++
            } catch (e: OperationFoundException) {
                return numberPress + 1
            }
        }
    }

    private fun Map<String, Module>.pressButton(statistics: Statistics, until: (Operation) -> Boolean = { false }) {
        var next = listOf(Operation("button", "broadcaster", Pulse.LOW))

        while (next.isNotEmpty()) {
            next = next.map {
                if (it.pulse == Pulse.LOW) statistics.low++ else statistics.high++
                if (until(it)) throw OperationFoundException()
                sendPulse(it)
            }.flatten()
        }
    }

    private fun Map<String, Module>.sendPulse(operation: Operation): List<Operation> {
        return when (val current = this[operation.toModule] ?: return emptyList()) {
            is Module.Broadcast -> {
                current.destinations.map { Operation(operation.toModule, it, operation.pulse) }
            }

            is Module.FlipFlop -> {
                if (operation.pulse == Pulse.LOW) {
                    if (current.on) {
                        current.on = false
                        current.destinations.map { Operation(operation.toModule, it, Pulse.LOW) }
                    } else {
                        current.on = true
                        current.destinations.map { Operation(operation.toModule, it, Pulse.HIGH) }
                    }
                } else {
                    listOf()
                }
            }

            is Module.Conjunction -> {
                current.states[operation.fromModule] = operation.pulse

                if (current.states.all { it.value == Pulse.HIGH }) {
                    current.destinations.map { Operation(operation.toModule, it, Pulse.LOW) }
                } else {
                    current.destinations.map { Operation(operation.toModule, it, Pulse.HIGH) }
                }
            }
        }
    }

    private fun Map<String, Module>.reset() {
        val conjunctions = filter { it.value is Module.Conjunction }.mapValues { it.value as Module.Conjunction }
        val flipFlops = filter { it.value is Module.FlipFlop }.mapValues { it.value as Module.FlipFlop }

        flipFlops.forEach { it.value.on = false }

        conjunctions.forEach { current ->
            flipFlops.findModulesWithDestination(current.key)
                .forEach { current.value.states[it] = Pulse.LOW }
        }
    }

    private fun Map<String, Module>.findModulesWithDestination(target: String): Set<String> {
        return filter { it.value.destinations.contains(target) }
            .keys
    }

    private fun parseModule(module: List<String>): Pair<String, Module> {
        val type = module[1]
        val name = module[2]
        val destination = module[3].split(", ")
        return if (type.isEmpty()) {
            name to Module.Broadcast(destination)
        } else if (type == "%") {
            name to Module.FlipFlop(destination, false)
        } else if (type == "&") {
            name to Module.Conjunction(destination, hashMapOf())
        } else {
            throw Exception("Not valid module")
        }
    }
}
