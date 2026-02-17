package kr.ac.tukorea.ge.spgp2025.a2dg.framework.objects

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.RectF
import android.util.Log
import android.view.MotionEvent
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin
import kotlin.math.sqrt
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.view.Metrics
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.util.rectF
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.util.setRect
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.res.BitmapPool
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.interfaces.IGameObject

class JoyStick(
    bgBmpId: Int,
    thumbBmpId: Int,
    xPos: Float,
    yPos: Float,
    val bg_radius: Float,
    val thumb_radius: Float,
    val move_radius: Float
) : IGameObject {
    private val bgBitmap = BitmapPool.get(bgBmpId)
    private val thumbBitmap = BitmapPool.get(thumbBmpId)
    private var x = xPos
    private var y = yPos
    private val bgRect = rectF(x, y, bg_radius)
    private val thumbRect = rectF(x, y, thumb_radius)

    var visible = false
    private var startX = 0f
    private var startY = 0f
    var power = 0f
    var angle_radian = 0.0

    override fun update() {
    }

    override fun draw(canvas: Canvas) {
        if (!visible) return
        canvas.drawBitmap(bgBitmap, null, bgRect, null)
        canvas.drawBitmap(thumbBitmap, null, thumbRect, null)
    }

    fun onTouch(event: MotionEvent): Boolean {
        return when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                visible = true
                val pts = Metrics.fromScreen(event.x, event.y)
                startX = pts[0]
                startY = pts[1]
                thumbRect.setRect(x, y, thumb_radius)
                power = 0f
                true
            }
            MotionEvent.ACTION_MOVE -> {
                val pts = Metrics.fromScreen(event.x, event.y)
                var dx = min(pts[0] - startX, bg_radius)
                dx = kotlin.math.max(-bg_radius, dx)
                var dy = min(pts[1] - startY, bg_radius)
                dy = kotlin.math.max(-bg_radius, dy)
                val radius = sqrt(dx * dx + dy * dy)
                angle_radian = atan2(dy.toDouble(), dx.toDouble())

                if (radius > move_radius) {
                    dx = (move_radius * cos(angle_radian)).toFloat()
                    dy = (move_radius * sin(angle_radian)).toFloat()
                }
                power = (radius / move_radius).toFloat()
                val cx = x + dx
                val cy = y + dy
                Log.d(TAG, "angle=${Math.toDegrees(angle_radian).toInt()}° power=${String.format("%.2f", power)}")
                thumbRect.setRect(cx, cy, thumb_radius)
                false
            }
            MotionEvent.ACTION_UP -> {
                visible = false
                power = 0f
                true
            }
            else -> false
        }
    }

    companion object {
        private const val TAG = "JoyStick"
    }
}

