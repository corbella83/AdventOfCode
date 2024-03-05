package corbella83.aoc.model

data class Region(val tl: Index, val br: Index) {

    val tr by lazy { Index(tl.y, br.x) }

    val bl by lazy { Index(br.y, tl.x) }

    fun inflate(bounds: Region): Region {
        val tl = Index(
            tl.y.minus(1).coerceAtLeast(bounds.tl.y),
            tl.x.minus(1).coerceAtLeast(bounds.tl.y)
        )
        val br = Index(
            br.y.plus(1).coerceAtMost(bounds.br.y),
            br.x.plus(1).coerceAtMost(bounds.br.y)
        )
        return Region(tl, br)
    }

    fun contains(index: Index): Boolean {
        return (index.y >= tl.y && index.y <= br.y && index.x >= tl.x && index.x <= br.x)
    }
}
