package kr.ac.tukorea.ge.spgp2025.a2dg.framework.objects

import android.graphics.Canvas
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.view.GameView
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.view.Metrics

class HorzScrollBackground(bitmapResId: Int, private val speed: Float) : Sprite(bitmapResId) {
    init {
        width = bitmap!!.width * Metrics.height / bitmap!!.height
        setPosition(Metrics.width / 2, Metrics.height / 2, width, Metrics.height)
    }

    override fun update() {
        x += speed * GameView.frameTime
    }

    override fun draw(canvas: Canvas) {
        var curr = x % width
        if (curr > 0) curr -= width
        while (curr < Metrics.width) {
            dstRect.set(curr, 0f, curr + width, Metrics.height)
            canvas.drawBitmap(bitmap!!, null, dstRect, null)
            curr += width
        }
    }
}

