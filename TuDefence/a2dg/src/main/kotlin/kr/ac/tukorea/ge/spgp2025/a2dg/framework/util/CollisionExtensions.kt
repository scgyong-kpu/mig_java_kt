package kr.ac.tukorea.ge.spgp2025.a2dg.framework.util

import android.graphics.RectF
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.interfaces.IBoxCollidable
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.objects.Sprite

/**
 * RectF끼리 AABB 충돌 체크
 */
fun RectF.collides(other: RectF): Boolean {
    if (left > other.right) return false
    if (top > other.bottom) return false
    if (right < other.left) return false
    if (bottom < other.top) return false
    return true
}

/**
 * Box Collidable 객체들 충돌 체크
 */
fun IBoxCollidable.collides(other: IBoxCollidable): Boolean {
    return getCollisionRect().collides(other.getCollisionRect())
}

/**
 * Sprite 반지름 충돌 체크
 */
fun Sprite.collidesRadius(other: Sprite): Boolean {
    val dx = x - other.x
    val dy = y - other.y
    val dist_sq = dx * dx + dy * dy
    val sum_of_two_radii = radius + other.radius
    val sum_sq = sum_of_two_radii * sum_of_two_radii
    return dist_sq < sum_sq
}
