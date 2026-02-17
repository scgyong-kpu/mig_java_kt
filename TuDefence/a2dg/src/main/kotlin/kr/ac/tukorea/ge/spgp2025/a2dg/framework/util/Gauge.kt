package kr.ac.tukorea.ge.spgp2025.a2dg.framework.util

import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Paint
import androidx.core.content.res.ResourcesCompat
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.view.GameView

class Gauge(width: Float, fgColorResId: Int, bgColorResId: Int) {
    private val fgPaint = Paint()
    private val bgPaint = Paint()

    init {
        val res: Resources = GameView.view.resources
        bgPaint.style = Paint.Style.STROKE
        bgPaint.strokeWidth = width
        bgPaint.color = ResourcesCompat.getColor(res, bgColorResId, null)
        bgPaint.strokeCap = Paint.Cap.ROUND
        fgPaint.style = Paint.Style.STROKE
        fgPaint.strokeWidth = width / 2
        fgPaint.color = ResourcesCompat.getColor(res, fgColorResId, null)
        fgPaint.strokeCap = Paint.Cap.ROUND
    }

    fun draw(canvas: Canvas, x: Float, y: Float, scale: Float, value: Float) {
        canvas.save()
        canvas.translate(x, y)
        canvas.scale(scale, scale)
        draw(canvas, value)
        canvas.restore()
    }

    fun draw(canvas: Canvas, progress: Float) {
        canvas.drawLine(0f, 0f, 1.0f, 0f, bgPaint)
        if (progress > 0) {
            canvas.drawLine(0f, 0f, progress, 0f, fgPaint)
        }
    }
}

