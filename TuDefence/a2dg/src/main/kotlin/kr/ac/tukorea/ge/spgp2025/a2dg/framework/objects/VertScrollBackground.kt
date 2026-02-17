package kr.ac.tukorea.ge.spgp2025.a2dg.framework.objects

import android.graphics.Canvas
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.view.GameView
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.view.Metrics

class VertScrollBackground(bitmapResId: Int, private val speed: Float) : Sprite(bitmapResId) {
    private val bgHeight: Float

    init {
        bgHeight = bitmap!!.height * Metrics.width / bitmap!!.width
        setPosition(Metrics.width / 2, Metrics.height / 2, Metrics.width, bgHeight)
    }

    override fun update() {
        y += speed * GameView.frameTime
    }

    override fun draw(canvas: Canvas) {
        var curr = y % bgHeight
        if (curr > 0) curr -= bgHeight
        while (curr < Metrics.height) {
            dstRect.set(0f, curr, Metrics.width, curr + bgHeight)
            canvas.drawBitmap(bitmap!!, null, dstRect, null)
            curr += bgHeight
        }
    }
}

