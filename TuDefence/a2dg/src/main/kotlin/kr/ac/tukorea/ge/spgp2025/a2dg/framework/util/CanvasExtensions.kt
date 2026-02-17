package kr.ac.tukorea.ge.spgp2025.a2dg.framework.util

import android.graphics.Canvas
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.util.Gauge

/**
 * Canvas 확장: Gauge 그리기 (위치, 스케일, 진행도)
 */
fun Canvas.drawGauge(gauge: Gauge, x: Float, y: Float, scale: Float, progress: Float) {
    gauge.draw(this, x, y, scale, progress)
}

/**
 * Canvas 확장: Gauge 그리기 (진행도만)
 */
fun Canvas.drawGauge(gauge: Gauge, progress: Float) {
    gauge.draw(this, progress)
}
