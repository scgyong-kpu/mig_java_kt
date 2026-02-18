package kr.ac.tukorea.ge.spgp2025.a2dg.framework.objects

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.RectF
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.interfaces.IGameObject
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.res.BitmapPool

class Score(mipmapId: Int, val right: Float, val top: Float, val width: Float) : IGameObject {
    override var layerIndex: Int = -1
    private val bitmap = BitmapPool.get(mipmapId)
    private val dstCharWidth = width
    private val srcCharWidth = bitmap.width / 10
    private val srcCharHeight = bitmap.height
    private val dstCharHeight = dstCharWidth * srcCharHeight / srcCharWidth
    private val srcRect = Rect()
    private val dstRect = RectF()

    var score = 0
        set(value) {
            field = value
            displayScore = value
        }
    var displayScore = 0

    fun add(amount: Int) {
        score += amount
    }

    override fun update() {
        val diff = score - displayScore
        when {
            diff == 0 -> return
            diff in -9..(-1) -> displayScore--
            diff in 1..9 -> displayScore++
            else -> displayScore += diff / 10
        }
    }

    override fun draw(canvas: Canvas) {
        var value = displayScore
        var x = right
        while (value > 0) {
            val digit = value % 10
            srcRect.set(digit * srcCharWidth, 0, (digit + 1) * srcCharWidth, srcCharHeight)
            x -= dstCharWidth
            dstRect.set(x, top, x + dstCharWidth, top + dstCharHeight)
            canvas.drawBitmap(bitmap, srcRect, dstRect, null)
            value /= 10
        }
    }
}

