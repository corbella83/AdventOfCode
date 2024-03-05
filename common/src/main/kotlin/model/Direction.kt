package corbella83.aoc.model

enum class Direction {
    UP,
    DOWN,
    LEFT,
    RIGHT
}

fun Direction.invert() = when (this) {
    Direction.UP -> Direction.DOWN
    Direction.DOWN -> Direction.UP
    Direction.LEFT -> Direction.RIGHT
    Direction.RIGHT -> Direction.LEFT
}
