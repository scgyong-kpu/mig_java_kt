package kr.ac.tukorea.ge.spgp2025.a2dg.framework.objects

import android.graphics.Canvas
import android.graphics.Rect

class SheetSprite(mipmapResId: Int, fps: Float) : AnimSprite(mipmapResId, fps) {
    var srcRects: Array<Rect>? = null

    override fun draw(canvas: Canvas) {
        val now = System.currentTimeMillis()
        val time = (now - createdOn) / 1000.0f
        val index = (kotlin.math.round(time * fps).toInt()) % srcRects!!.size
        canvas.drawBitmap(bitmap!!, srcRects!![index], dstRect, null)
    }
}

