package kr.ac.tukorea.ge.spgp2025.a2dg.framework.objects

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.RectF
import android.util.Log
import androidx.annotation.NonNull
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.graphics.Position
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.graphics.Velocity
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.interfaces.IGameObject
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.res.BitmapPool
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.util.RectUtil
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.view.GameView

open class Sprite(mipmapId: Int) : IGameObject {
    protected var bitmap: Bitmap? = null
    protected var srcRect: Rect? = null
    val dstRect = RectF()
    var position = Position()
    var velocity = Velocity()
    var width = 0f
    var height = 0f
    var radius = 0f
    
    // 직접 접근 가능 (copy 없음)
    var x: Float
        get() = position.x
        set(value) {
            position.x = value
        }
    var y: Float
        get() = position.y
        set(value) {
            position.y = value
        }
    var dx: Float
        get() = velocity.dx
        set(value) {
            velocity.dx = value
        }
    var dy: Float
        get() = velocity.dy
        set(value) {
            velocity.dy = value
        }

    init {
        if (mipmapId != 0) {
            bitmap = BitmapPool.get(mipmapId)
        }
        Log.v(TAG, "Created ${this.javaClass.simpleName}@${System.identityHashCode(this)}")
    }

    constructor(mipmapId: Int, x: Float, y: Float, width: Float, height: Float) : this(mipmapId) {
        setPosition(x, y, width, height)
    }

    fun setImageResourceId(mipmapId: Int) {
        bitmap = BitmapPool.get(mipmapId)
    }

    fun setPosition(x: Float, y: Float, radius: Float) {
        position = Position(x, y)
        width = 2 * radius
        height = width
        this.radius = radius
        RectUtil.setRect(dstRect, x, y, radius)
    }

    fun setPosition(x: Float, y: Float) {
        position = Position(x, y)
        RectUtil.setRect(dstRect, x, y, width, height)
    }

    fun setPosition(x: Float, y: Float, width: Float, height: Float) {
        position = Position(x, y)
        this.width = width
        this.height = height
        radius = kotlin.math.min(width, height) / 2
        RectUtil.setRect(dstRect, x, y, width, height)
    }


    fun getPropotionalHeight(width: Float): Float {
        return width / bitmap!!.width * bitmap!!.height
    }

    override fun update() {
        val timedDx = velocity.dx * GameView.frameTime
        val timedDy = velocity.dy * GameView.frameTime
        position.add(timedDx, timedDy)
        dstRect.offset(timedDx, timedDy)
    }

    override fun draw(canvas: Canvas) {
        canvas.drawBitmap(bitmap!!, srcRect, dstRect, null)
    }

    @NonNull
    override fun toString(): String {
        return "${javaClass.simpleName}@${System.identityHashCode(this)}(${width.toInt()}x${height.toInt()})"
    }

    companion object {
        private const val TAG = "Sprite"
    }
}

