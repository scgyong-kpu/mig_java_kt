package kr.ac.tukorea.ge.spgp2025.a2dg.framework.objects

import android.graphics.Canvas
import android.graphics.Rect

open class AnimSprite(mipmapId: Int, var fps: Float) : Sprite(mipmapId) {
    var frameCount = 0
    var frameWidth = 0
    var frameHeight = 0
    var createdOn = System.currentTimeMillis()

    constructor(mipmapId: Int, fps: Float, frameCount: Int) : this(mipmapId, fps) {
        this.fps = fps
        srcRect = Rect()
        createdOn = System.currentTimeMillis()
        if (bitmap != null) {
            setFrameInfo(frameCount)
        }
    }

    private fun setFrameInfo(frameCount: Int) {
        val imageWidth = bitmap!!.width
        val imageHeight = bitmap!!.height
        if (frameCount == 0) {
            frameWidth = imageHeight
            frameHeight = imageHeight
            this.frameCount = imageWidth / imageHeight
        } else {
            frameWidth = imageWidth / frameCount
            frameHeight = imageHeight
            this.frameCount = frameCount
        }
    }

    fun setImageResourceId(mipmapId: Int, fps: Float) {
        setImageResourceId(mipmapId, fps, 0)
    }

    fun setImageResourceId(mipmapId: Int, fps: Float, frameCount: Int) {
        super.setImageResourceId(mipmapId)
        this.fps = fps
        setFrameInfo(frameCount)
    }

    override fun draw(canvas: Canvas) {
        val now = System.currentTimeMillis()
        val time = (now - createdOn) / 1000.0f
        val frameIndex = (kotlin.math.round(time * fps).toInt()) % frameCount
        srcRect!!.set(frameIndex * frameWidth, 0, (frameIndex + 1) * frameWidth, frameHeight)
        canvas.drawBitmap(bitmap!!, srcRect, dstRect, null)
    }
}

