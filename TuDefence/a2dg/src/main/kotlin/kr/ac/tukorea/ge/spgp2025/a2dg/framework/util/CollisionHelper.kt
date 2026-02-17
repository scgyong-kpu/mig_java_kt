package kr.ac.tukorea.ge.spgp2025.a2dg.framework.util

import android.graphics.RectF
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.interfaces.IBoxCollidable
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.objects.Sprite

object CollisionHelper {
    fun collides(obj1: IBoxCollidable, obj2: IBoxCollidable): Boolean {
        val r1 = obj1.getCollisionRect()
        val r2 = obj2.getCollisionRect()
        return collides(r1, r2)
    }

    fun collides(r1: RectF, r2: RectF): Boolean {
        if (r1.left > r2.right) return false
        if (r1.top > r2.bottom) return false
        if (r1.right < r2.left) return false
        if (r1.bottom < r2.top) return false
        return true
    }

    fun collidesRadius(s1: Sprite, s2: Sprite): Boolean {
        val dx = s1.x - s2.x
        val dy = s1.y - s2.y
        val dist_sq = dx * dx + dy * dy
        val sum_of_two_radii = s1.radius + s2.radius
        val sum_sq = sum_of_two_radii * sum_of_two_radii
        return dist_sq < sum_sq
    }
}

