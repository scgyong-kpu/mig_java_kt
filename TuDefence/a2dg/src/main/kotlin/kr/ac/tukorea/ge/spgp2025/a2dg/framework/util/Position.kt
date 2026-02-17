package kr.ac.tukorea.ge.spgp2025.a2dg.framework.util

data class Position(var x: Float = 0f, var y: Float = 0f) {
    fun set(x: Float, y: Float): Position {
        this.x = x
        this.y = y
        return this
    }

    fun set(other: Position): Position = set(other.x, other.y)

    fun add(other: Position): Position = set(x + other.x, y + other.y)

    fun add(dx: Float, dy: Float): Position = set(x + dx, y + dy)

    fun scale(factor: Float): Position = set(x * factor, y * factor)
}
