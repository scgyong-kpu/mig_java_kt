package kr.ac.tukorea.ge.spgp2025.a2dg.framework.view

import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.RectF
import android.util.Log

object Metrics {
    private const val TAG = "Metrics"
    var width: Float = 900f
        set(value) {
            field = value
            borderRect.right = value
        }
    var height: Float = 1600f
        set(value) {
            field = value
            borderRect.bottom = value
        }
    const val GRID_UNIT = 100f
    val borderRect = RectF(0f, 0f, width, height)
    val screenRect = RectF()
    private val transformMatrix = Matrix()
    private val invertedMatrix = Matrix()
    private val pointsBuffer = FloatArray(2)


    fun onSize(w: Int, h: Int) {
        val view_ratio = w.toFloat() / h.toFloat()
        val game_ratio = width / height

        if (view_ratio > game_ratio) {
            val scale = h / height
            transformMatrix.setTranslate((w - h * game_ratio) / 2, 0f)
            transformMatrix.preScale(scale, scale)
        } else {
            val scale = w / width
            transformMatrix.setTranslate(0f, (h - w / game_ratio) / 2)
            transformMatrix.preScale(scale, scale)
        }
        transformMatrix.invert(invertedMatrix)

        screenRect.set(0f, 0f, w.toFloat(), h.toFloat())
        invertedMatrix.mapRect(screenRect)
        Log.d(TAG, "Screen Rect = $screenRect")
    }

    fun fromScreen(x: Float, y: Float): FloatArray {
        pointsBuffer[0] = x
        pointsBuffer[1] = y
        invertedMatrix.mapPoints(pointsBuffer)
        return pointsBuffer
    }

    fun toScreen(x: Float, y: Float): FloatArray {
        pointsBuffer[0] = x
        pointsBuffer[1] = y
        transformMatrix.mapPoints(pointsBuffer)
        return pointsBuffer
    }

    fun concat(canvas: Canvas) {
        canvas.concat(transformMatrix)
    }
}

