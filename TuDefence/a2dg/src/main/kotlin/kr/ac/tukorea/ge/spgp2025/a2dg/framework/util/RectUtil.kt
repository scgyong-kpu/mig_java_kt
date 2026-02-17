package kr.ac.tukorea.ge.spgp2025.a2dg.framework.util

import android.graphics.RectF

object RectUtil {
    fun newRectF(x: Float, y: Float, radius: Float): RectF {
        return RectF(x - radius, y - radius, x + radius, y + radius)
    }

    fun setRect(rect: RectF, x: Float, y: Float, radius: Float) {
        rect.set(x - radius, y - radius, x + radius, y + radius)
    }

    fun newRectF(x: Float, y: Float, width: Float, height: Float): RectF {
        val half_width = width / 2
        val half_height = height / 2
        return RectF(x - half_width, y - half_height, x + half_width, y + half_height)
    }

    fun setRect(rect: RectF, x: Float, y: Float, width: Float, height: Float) {
        val half_width = width / 2
        val half_height = height / 2
        rect.set(x - half_width, y - half_height, x + half_width, y + half_height)
    }
}

