package kr.ac.tukorea.ge.spgp2025.a2dg.framework.util

data class Velocity(var dx: Float = 0f, var dy: Float = 0f) {
    fun set(dx: Float, dy: Float): Velocity {
        this.dx = dx
        this.dy = dy
        return this
    }

    fun set(other: Velocity): Velocity = set(other.dx, other.dy)

    fun scale(factor: Float): Velocity = set(dx * factor, dy * factor)
}
