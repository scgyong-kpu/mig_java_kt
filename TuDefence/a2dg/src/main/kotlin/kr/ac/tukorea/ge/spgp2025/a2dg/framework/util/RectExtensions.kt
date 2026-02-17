package kr.ac.tukorea.ge.spgp2025.a2dg.framework.util

import android.graphics.RectF

/**
 * 중심 좌표 + 반지름으로 새 RectF 생성
 */
fun rectF(x: Float, y: Float, radius: Float): RectF {
    return RectF(x - radius, y - radius, x + radius, y + radius)
}

/**
 * 중심 좌표 + 폭/높이로 새 RectF 생성
 */
fun rectF(x: Float, y: Float, width: Float, height: Float): RectF {
    val half_width = width / 2
    val half_height = height / 2
    return RectF(x - half_width, y - half_height, x + half_width, y + half_height)
}

/**
 * RectF 중심 좌표 + 반지름으로 설정
 */
fun RectF.setRect(x: Float, y: Float, radius: Float) {
    set(x - radius, y - radius, x + radius, y + radius)
}

/**
 * RectF 중심 좌표 + 폭/높이로 설정
 */
fun RectF.setRect(x: Float, y: Float, width: Float, height: Float) {
    val half_width = width / 2
    val half_height = height / 2
    set(x - half_width, y - half_height, x + half_width, y + half_height)
}
