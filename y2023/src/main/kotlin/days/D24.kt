package days

import corbella83.aoc.Day
import corbella83.aoc.Resource
import corbella83.aoc.Result
import corbella83.aoc.model.LongOperators.div
import corbella83.aoc.model.LongOperators.minus
import corbella83.aoc.model.LongOperators.times
import corbella83.aoc.model.XYZ
import corbella83.aoc.model.times
import corbella83.aoc.model.toBigInt
import corbella83.aoc.utils.pairs
import java.math.BigInteger

object D24 : Day {
    private const val BOUND_MIN = 200000000000000.0
    private const val BOUND_MAX = 400000000000000.0

    private data class Trajectory(val position: XYZ<Long>, val speed: XYZ<Long>) {

        fun get(after: Long) =
            XYZ(position.x + after * speed.x, position.y + after * speed.y, position.z + after * speed.z)

        fun get(after: Double) =
            XYZ(position.x + after * speed.x, position.y + after * speed.y, position.z + after * speed.z)
    }

    override val day = 24

    override fun part1(resource: Resource): Result {
        return resource.readLines { parseTrajectory(it) }
            .pairs()
            .mapNotNull { findCollisionPoint(it.first, it.second) }
            .count { it.x in BOUND_MIN..BOUND_MAX && it.y in BOUND_MIN..BOUND_MAX }
    }

    override fun part2(resource: Resource): Result {
        val trajectories = resource.readLines { parseTrajectory(it) }
        val intersections = findIntersectionTrajectories(trajectories[0], trajectories[1], trajectories[2])

        val intersection = if (intersections.size == 1) {
            intersections.single()
        } else {
            findIntersectionTrajectories(trajectories[3], trajectories[4], trajectories[5])
                .intersect(intersections.toSet())
                .single()
        }

        return with(intersection.position) { x + y + z }
    }

    private fun findCollisionPoint(t1: Trajectory, t2: Trajectory): XYZ<Double>? {
        val ux = XYZ(t1.speed.x, -t2.speed.x, t2.position.x - t1.position.x)
        val uy = XYZ(t1.speed.y, -t2.speed.y, t2.position.y - t1.position.y)
        return getKramerSolution(ux, uy)
            ?.takeIf { it.first > 0 && it.second > 0 }
            ?.let { t1.get(it.first) }
    }

    private fun getKramerSolution(v1: XYZ<Long>, v2: XYZ<Long>): Pair<Double, Double>? {
        val detS = v1.x * v2.y - v2.x * v1.y
        if (detS == 0L) return null
        val detX = v1.z * v2.y - v2.z * v1.y
        val detY = v1.x * v2.z - v2.x * v1.z
        return detX.toDouble() / detS to detY.toDouble() / detS
    }

    private fun findIntersectionTrajectories(base: Trajectory, reference1: Trajectory, reference2: Trajectory): List<Trajectory> {
        return getEquationSolutions(base, reference1, reference2).map {
            val pointB = reference1.get(it.first)
            val pointC = reference2.get(it.second)
            val vector = (pointB - pointC) / (it.first - it.second)
            val startPoint = pointB - vector * it.first
            Trajectory(startPoint, vector)
        }
    }

    private fun getEquationSolutions(A: Trajectory, B: Trajectory, C: Trajectory): List<Pair<Long, Long>> {
        // Equation come from knowing that AB and AC must have the same speed

        val vAB = (B.speed - A.speed).toBigInt()
        val vAC = (C.speed - A.speed).toBigInt()
        val pAB = (B.position - A.position).toBigInt()
        val pAC = (C.position - A.position).toBigInt()

        val VV_Z = vAB.z * vAC.x - vAB.x * vAC.z
        val VP_Z = vAB.z * pAC.x - vAB.x * pAC.z
        val PV_Z = pAB.x * vAC.z - pAB.z * vAC.x
        val PP_Z = pAB.x * pAC.z - pAB.z * pAC.x

        val VV_Y = vAB.y * vAC.x - vAB.x * vAC.y
        val VP_Y = vAB.y * pAC.x - vAB.x * pAC.y
        val PV_Y = pAB.x * vAC.y - pAB.y * vAC.x
        val PP_Y = pAB.x * pAC.y - pAB.y * pAC.x

        val a = VV_Y * PV_Z - VV_Z * PV_Y
        val b = PV_Z * VP_Y + PP_Z * VV_Y - VV_Z * PP_Y - VP_Z * PV_Y
        val c = PP_Z * VP_Y - VP_Z * PP_Y

        val t2 = if (a == BigInteger.ZERO) {
            listOf(-c / b)
        } else {
            listOf(
                (-b + (b * b - 4 * a * c).sqrt()) / (2 * a),
                (-b - (b * b - 4 * a * c).sqrt()) / (2 * a)
            )
        }

        return t2.map {
            val t1 = (it * PV_Z + PP_Z) / (it * VV_Z + VP_Z)
            t1.toLong() to it.toLong()
        }
    }

    private fun parseTrajectory(line: String): Trajectory {
        val components = line.split('@').map { vec ->
            vec.split(',').map { it.trim().toLong() }.let { XYZ(it[0], it[1], it[2]) }
        }
        return Trajectory(components[0], components[1])
    }
}
