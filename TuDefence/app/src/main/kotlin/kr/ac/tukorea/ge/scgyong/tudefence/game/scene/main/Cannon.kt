package kr.ac.tukorea.ge.scgyong.tudefence.game.scene.main

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.DashPathEffect
import android.graphics.Paint
import android.graphics.RectF
import android.util.Log
import androidx.annotation.NonNull
import java.util.ArrayList
import kr.ac.tukorea.ge.scgyong.tudefence.R
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.interfaces.IGameObject
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.objects.Sprite
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.res.BitmapPool
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.scene.Scene
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.view.GameView
import kotlin.math.abs
import kotlin.math.atan2

class Cannon(level: Int, x: Float, y: Float) : Sprite(0) {
    companion object {
        private val BITMAP_IDS = intArrayOf(
            R.mipmap.f_01_01, R.mipmap.f_02_01, R.mipmap.f_03_01, R.mipmap.f_04_01, R.mipmap.f_05_01,
            R.mipmap.f_06_01, R.mipmap.f_07_01, R.mipmap.f_08_01, R.mipmap.f_09_01, R.mipmap.f_10_01,
        )
        private val COSTS = intArrayOf(
            10, 100, 300, 700, 1500, 3000, 7000, 15000, 40000, 100000, 100000000
        )

        fun getInstallationCost(level: Int) = COSTS[level - 1]
        fun getUpgradeCost(level: Int) = ((COSTS[level] - COSTS[level - 1]) * 1.1f).toInt()
        fun getSellPrice(level: Int) = COSTS[level - 1] / 2

        private var rangePaint: Paint? = null
    }

    var level = 0
        set(value) {
            field = value
            bitmap = BitmapPool.get(BITMAP_IDS[value - 1])
            range = (200 + (value * 200)).toFloat()
            interval = 5.5f - value / 2.0f
            barrelRect.set(dstRect)
            val barrelSize = 50f + value * 10f
            barrelRect.inset(-barrelSize, -barrelSize)
        }
    var range = 0f
    var interval = 0f
    val barrelBitmap = BitmapPool.get(R.mipmap.tank_barrel)
    val barrelRect = RectF()
    var angle = -90f
    var time = 0f

    init {
        setPosition(x, y, 200f, 200f)
        this.level = level
    }


    override fun update() {
        super.update()
        val fly = findNearestFly()
        if (fly != null) {
            angle = Math.toDegrees(atan2(fly.y - y, fly.x - x).toDouble()).toFloat()
        }
        time += GameView.frameTime
        if (time > interval && fly != null) {
            val shell = Shell.get(this, fly)
            Scene.top()?.add(MainScene.Layer.shell, shell)
            time = 0f
        }
    }

    private fun findNearestFly(): Fly? {
        var nearest_dist_sq = range * range
        var nearest: Fly? = null
        val scene = Scene.top() as? MainScene ?: return null
        val flies = scene.objectsAt(MainScene.Layer.enemy)
        for (gameObject in flies) {
            if (gameObject !is Fly) continue
            val fly = gameObject
            val fx = fly.x
            val fy = fly.y
            val dx = x - fx
            val dx_sq = dx * dx
            if (dx_sq > nearest_dist_sq) continue
            val dy = y - fy
            val dy_sq = dy * dy
            if (dy_sq > nearest_dist_sq) continue
            val dist_sq = dx_sq + dy_sq
            if (nearest_dist_sq > dist_sq) {
                nearest_dist_sq = dist_sq
                nearest = fly
            }
        }
        return nearest
    }

    fun drawRange(canvas: Canvas) {
        if (rangePaint == null) {
            rangePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                style = Paint.Style.STROKE
                strokeWidth = 10f
                pathEffect = DashPathEffect(floatArrayOf(10f, 20f), 0f)
                color = 0x7F7F0000.toInt()
            }
        }
        canvas.drawCircle(x, y, range, rangePaint!!)
    }

    override fun draw(canvas: Canvas) {
        super.draw(canvas)
        canvas.save()
        canvas.rotate(angle, x, y)
        canvas.drawBitmap(barrelBitmap, null, barrelRect, null)
        canvas.restore()
    }

    fun containsPoint(x: Float, y: Float): Boolean = dstRect.contains(x, y)

    fun intersectsIfInstalledAt(x: Float, y: Float): Boolean {
        val dx = abs(x - this.x)
        val dy = abs(y - this.y)
        return dx <= radius && dy <= radius
    }

    fun upgrade(): Boolean {
        if (level == BITMAP_IDS.size) {
            uninstall()
            return false
        }
        level = level + 1
        return true
    }

    private fun uninstall() {
        Scene.top()?.remove(MainScene.Layer.cannon, this)
    }

    fun getUpgradeCost() = getUpgradeCost(level)
    fun getSellPrice() = getSellPrice(level)

    @NonNull
    override fun toString(): String {
        return "Cannon<$level>(${(x / 100).toInt()},${(y / 100).toInt()})@${System.identityHashCode(this)}"
    }
}

