package corbella83.aoc

import corbella83.aoc.model.Matrix

class Resource(private val lines: List<String>) {

    fun single(): String {
        return lines.toList().single()
    }

    fun <T : Any> readLines(from: Int = 0, transformer: (String) -> T): List<T> {
        return lines.drop(from)
            .map(transformer)
    }

    fun <T : Any> readLines(regex: Regex, from: Int = 0, transformer: (List<String>) -> T): List<T> {
        return readLines(from) { line ->
            regex.find(line)?.let { transformer(it.groupValues) }!!
        }
    }

    fun <T : Any> readBlocks(transformer: (Resource) -> T): List<T> {
        val cumulative = arrayListOf<String>()
        val result = arrayListOf<T>()

        lines.forEach { line ->
            if (line.isEmpty()) {
                val res = Resource(ArrayList(cumulative))
                result.add(transformer(res))
                cumulative.clear()
            } else {
                cumulative.add(line)
            }
        }

        val res = Resource(ArrayList(cumulative))
        result.add(transformer(res))

        return result
    }

    fun readMatrix(): Matrix {
        return lines.map { it.toCharArray() }
            .let { Matrix.fromRows(it) }
    }
}
