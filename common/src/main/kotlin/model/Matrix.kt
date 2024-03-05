package corbella83.aoc.model

import kotlin.math.ceil

class Matrix private constructor(private val table: Array<CharArray>) {

    constructor(rows: Int, columns: Int, none: Char = '.') : this(Array(rows) { CharArray(columns) { none } })

    fun size() = table.size to table.first().size

    fun bounds() = Region(Index(1, 1), Index(table.size, table.first().size))

    fun edges() = with(bounds()) {
        listOf(tl, tr, br, bl)
    }

    fun cardinals() = with(size()) {
        val halfY = ceil(first / 2f).toInt()
        val halfX = ceil(second / 2f).toInt()
        listOf(Index(1, halfX), Index(halfY, 1), Index(halfY, second), Index(first, halfX))
    }

    fun distinct(): Set<Char> {
        val set = hashSetOf<Char>()
        for (i in table.indices) {
            val line = table[i]
            for (j in line.indices) {
                set.add(line[j])
            }
        }
        return set
    }

    fun count(char: Char): Long {
        return count { it == char }
    }

    fun count(condition: (Char) -> Boolean): Long {
        var count = 0L
        for (i in table.indices) {
            val line = table[i]
            for (j in line.indices) {
                if (condition(line[j])) count++
            }
        }
        return count
    }

    fun get(index: Index): Char {
        return table[index.y - 1][index.x - 1]
    }

    fun getOrNull(index: Index): Char? {
        return try {
            table[index.y - 1][index.x - 1]
        } catch (e: Exception) {
            null
        }
    }

    fun getRows(): List<CharArray> {
        return table.toList()
    }

    fun getColumns(): List<CharArray> {
        return transpose().getRows()
    }

    fun get(region: Region): Matrix {
        val matrix = table.copyOfRange(region.tl.y - 1, region.br.y).map { it.copyOfRange(region.tl.x - 1, region.br.x) }.toTypedArray()
        return Matrix(matrix)
    }

    fun get(segment: Segment): String {
        return when (segment) {
            is Segment.Horizontal -> String(table[segment.position.y - 1].copyOfRange(segment.position.x - 1, segment.position.x + segment.length - 1))
            is Segment.Vertical -> String(table.copyOfRange(segment.position.y - 1, segment.position.y + segment.length - 1).map { it[segment.position.x - 1] }.toCharArray())
        }
    }

    fun get(index: List<Index>): List<Char> {
        return index.map { get(it) }
    }

    fun set(index: Index, char: Char) {
        table[index.y - 1][index.x - 1] = char
    }

    fun set(indexes: List<Index>, char: Char) {
        indexes.forEach { set(it, char) }
    }

    fun swap(index1: Index, index2: Index) {
        if (index1 == index2) return
        with(get(index1)) {
            set(index1, get(index2))
            set(index2, this)
        }
    }

    fun findContiguousH(condition: (Char) -> Boolean): List<Segment> {
        val result = arrayListOf<Segment>()

        for (i in table.indices) {
            val line = table[i]

            var j = 0
            while (j < line.size) {
                if (condition(line[j])) {
                    val startNumber = j
                    while (j < line.size && condition(line[j])) j++
                    val position = Index(i + 1, startNumber + 1)
                    result.add(Segment.Horizontal(position, j - startNumber))
                } else {
                    j++
                }
            }
        }

        return result
    }

    fun findContiguousV(condition: (Char) -> Boolean): List<Segment> {
        TODO()
    }

    fun findAll(character: Char): List<Index> {
        val result = arrayListOf<Index>()
        forEach {
            if (character == get(it)) result.add(it)
        }
        return result
    }

    fun findAll(condition: (Char) -> Boolean): List<Index> {
        val result = arrayListOf<Index>()
        forEach {
            if (condition(get(it))) result.add(it)
        }
        return result
    }

    fun forEach(code: (Index) -> Unit) {
        for (i in table.indices) {
            val line = table[i]
            for (j in line.indices) {
                code(Index(i + 1, j + 1))
            }
        }
    }

    fun forEachRow(y: Int, code: (Int) -> Unit) {
        val line = table[y - 1]
        for (j in line.indices) {
            code(j + 1)
        }
    }

    fun getRowDiff(row1: Int, row2: Int, maxDiff: Int = 0): Int {
        var diff = 0
        for (j in 0..<size().second) {
            if (table[row1 - 1][j] != table[row2 - 1][j]) diff++
            if (diff > maxDiff) return maxDiff + 1
        }
        return diff
    }

    fun getColumnDiff(column1: Int, column2: Int, maxDiff: Int = 0): Int {
        var diff = 0
        for (i in 0..<size().first) {
            if (table[i][column1 - 1] != table[i][column2 - 1]) diff++
            if (diff > maxDiff) return maxDiff + 1
        }
        return diff
    }

    fun transpose(): Matrix {
        val list = arrayListOf<CharArray>()
        val sz = size()

        for (i in 1..sz.second) {
            val tmp = arrayListOf<Char>()
            for (j in 1..sz.first) {
                tmp.add(get(Index(j, i)))
            }
            list.add(tmp.toCharArray())
        }
        return Matrix(list.toTypedArray())
    }

    override fun toString(): String {
        return table.joinToString("\n") { it.joinToString(" ") }
    }

    fun isOutOfBounds(index: Index): Boolean {
        val lastIndex = size()
        if (index.x < 1 || index.x > lastIndex.second) return true
        if (index.y < 1 || index.y > lastIndex.first) return true
        return false
    }

    fun copy(): Matrix {
        return Matrix(table.map { it.copyOf() }.toTypedArray())
    }

    fun areaOf(path: List<Index>): Int {
        var area = 0
        val size = size()
        for (y in 0 until size.first) {
            var inside = false
            val line = table[y]
            for (j in line.indices) {
                val now = Index(y + 1, j + 1)
                val contains = path.indexOf(now)
                if (contains >= 0) {
                    val previous = path[(path.size + contains - 1) % path.size]
                    val next = path[(contains + 1) % path.size]

                    val tmp1 = now - next
                    val tmp2 = now - previous
                    if (tmp1.y == 1 || tmp2.y == 1) inside = !inside
                } else {
                    if (inside) area++
                }
            }
        }
        return area
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Matrix

        return table.contentDeepEquals(other.table)
    }

    override fun hashCode(): Int {
        return table.contentDeepHashCode()
    }

    companion object {
        fun fromRows(rows: List<CharArray>): Matrix {
            return Matrix(rows.toTypedArray())
        }

        fun fromColumns(columns: List<CharArray>): Matrix {
            return Matrix(columns.toTypedArray()).transpose()
        }
    }
}
