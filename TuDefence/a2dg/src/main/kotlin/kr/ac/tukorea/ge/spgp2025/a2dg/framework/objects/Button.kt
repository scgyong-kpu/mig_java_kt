package kr.ac.tukorea.ge.spgp2025.a2dg.framework.objects

import android.view.MotionEvent
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.interfaces.ITouchable
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.view.Metrics

class Button(
    bitmapResId: Int,
    cx: Float,
    cy: Float,
    width: Float,
    height: Float,
    val listener: OnTouchListener
) : Sprite(bitmapResId, cx, cy, width, height), ITouchable {

    fun interface OnTouchListener {
        fun onTouch(pressed: Boolean): Boolean
    }

    private var captures = false

    override fun onTouchEvent(e: MotionEvent): Boolean {
        return when (e.action) {
            MotionEvent.ACTION_DOWN -> {
                val pts = Metrics.fromScreen(e.x, e.y)
                val x = pts[0]
                val y = pts[1]
                if (!dstRect.contains(x, y)) {
                    return false
                }
                captures = true
                listener.onTouch(true)
            }
            MotionEvent.ACTION_UP -> {
                captures = false
                listener.onTouch(false)
            }
            else -> captures
        }
    }
}

