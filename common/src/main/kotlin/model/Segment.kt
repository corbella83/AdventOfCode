package corbella83.aoc.model

sealed class Segment {

    data class Horizontal(val position: Index, val length: Int) : Segment()

    data class Vertical(val position: Index, val length: Int) : Segment()

    fun asRegion() = when (this) {
        is Horizontal -> Region(position, Index(position.y, position.x + length - 1))
        is Vertical -> Region(position, Index(position.y + length - 1, position.x))
    }
}
