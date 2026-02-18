package kr.ac.tukorea.ge.scgyong.tudefence.game.scene.main

import android.graphics.Canvas
import android.graphics.Path
import android.graphics.PathMeasure
import android.graphics.Rect
import android.util.Log
import androidx.core.graphics.PathParser
import kr.ac.tukorea.ge.scgyong.tudefence.R
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.interfaces.IRecyclable
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.objects.SheetSprite
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.scene.Scene
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.util.Gauge
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.util.drawGauge
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.view.GameView
import java.util.Random
import kotlin.math.sqrt

class Fly : SheetSprite(R.mipmap.galaga_flies, 2.0f), IRecyclable {
    companion object {
        private const val TAG = "Fly"
        private val rand = Random()
        private val path: Path
        private val pm: PathMeasure
        private val pathLength: Float
        private var rects_array: Array<Array<Rect>>? = null

        init {
            path = PathParser.createPathFromPathData(
                "M -128,1817.6\n" +
                "C 198.4,1785.6 384,1558.4 384,1280\n" +
                "C 384,1001.6 86.4,956.8 89.6,656\n" +
                "C 92.8,355.2 332.8,54.4 640,51.2\n" +
                "C 947.2,48 1177.6,339.2 1184,649.6\n" +
                "C 1190.4,960 988.8,924.8 992,1251.2\n" +
                "C 995.2,1577.6 1440,1692.8 1609.6,1692.8\n" +
                "C 1779.2,1692.8 2217.6,1516.8 2220.8,1264\n" +
                "C 2224,1011.2 1993.6,940.8 1993.6,662.4\n" +
                "C 1993.6,384 2236.8,83.2 2576,86.4\n" +
                "C 2915.2,89.6 3120,419.2 3110.4,675.2\n" +
                "C 3100.8,931.2 2816,1078.4 2819.2,1340.8\n" +
                "C 2822.4,1603.2 3155.2,1782.4 3360,1766.4\n"
            )
            pm = PathMeasure(path, false)
            pathLength = pm.length
        }

        fun get(boss: Boolean, speedRatio: Float): Fly {
            val type = if (boss) Type.BOSS else Type.random()
            val size = rand.nextFloat() * 100 + 150
            val finalSize = if (boss) size * 1.5f else size
            val speed = speedRatio * (rand.nextFloat() * 50 + 75)
            return Scene.top()!!.getRecyclable(Fly::class.java)!!.init(type, finalSize, speed)
        }
    }

    enum class Type(val maxHealth: Float) {
        BOSS(150f), RED(50f), BLUE(30f), CYAN(20f), DRAGON(10f);

        companion object {
            private val POSSIBILITIES = intArrayOf(0, 10, 20, 30, 40)
            private val POSSIBILITY_SUM = POSSIBILITIES.sum()

            fun random(): Type {
                val value = rand.nextInt(POSSIBILITY_SUM)
                var rv = value
                for (i in POSSIBILITIES.indices) {
                    rv -= POSSIBILITIES[i]
                    if (rv < 0) return values()[i]
                }
                return DRAGON
            }
        }
    }

    var distance = 0f
    var speed = 0f
    var angle = 0f
    var life = 0f
    var maxLife = 0f
    var displayLife = 0f
    private val pos = FloatArray(2)
    private val tan = FloatArray(2)
    private var gauge: Gauge? = null

    init {
        if (rects_array == null) {
            val typeCount = Type.values().size
            val h = bitmap!!.height
            rects_array = Array(typeCount) { Array(2) { Rect() } }
            var x = 0
            for (i in 0 until typeCount) {
                for (j in 0..1) {
                    rects_array!![i][j] = Rect(x, 0, x + h, h)
                    x += h
                }
            }
        }
        setPosition(0f, 0f, 200f, 200f)
    }

    fun init(type: Type, size: Float, speed: Float): Fly {
        srcRects = rects_array!![type.ordinal]
        setPosition(0f, 0f, size, size)
        distance = 0f
        velocity.set(0f, 0f)
        this.speed = speed
        life = type.maxHealth * (0.9f + rand.nextFloat() * 0.2f)
        maxLife = life
        displayLife = life
        update()
        return this
    }

    fun decreaseLife(power: Float): Boolean {
        life -= power
        return life <= 0
    }

    fun score(): Int = (maxLife / 10).toInt() * 10

    override fun update() {
        if (life != displayLife) {
            val step = maxLife / 50
            val diff = life - displayLife
            when {
                diff < -step -> displayLife -= step
                diff > step -> displayLife += step
                else -> displayLife = life
            }
        }
        distance += speed * GameView.frameTime
        if (distance > pathLength) {
            Scene.top()?.remove(this)
            return
        }
        val maxDiff = width / 5
        var newDx = dx + (2 * maxDiff * rand.nextFloat() - maxDiff) * GameView.frameTime
        if (newDx < -maxDiff) newDx = -maxDiff
        else if (newDx > maxDiff) newDx = maxDiff
        var newDy = dy + (2 * maxDiff * rand.nextFloat() - maxDiff) * GameView.frameTime
        if (newDy < -maxDiff) newDy = -maxDiff
        else if (newDy > maxDiff) newDy = maxDiff
        velocity.set(newDx, newDy)

        pm.getPosTan(distance, pos, tan)
        setPosition(pos[0] + dx, pos[1] + dy)
        val tanY = tan[1].toDouble()
        val tanX = tan[0].toDouble()
        angle = Math.toDegrees(Math.atan2(tanY, tanX)).toFloat()
    }

    override fun draw(canvas: Canvas) {
        canvas.save()
        canvas.rotate(angle, x, y)
        super.draw(canvas)
        canvas.restore()
        val barSize = width * 2 / 3
        if (gauge == null) {
            gauge = Gauge(0.2f, R.color.fly_health_fg, R.color.fly_health_bg)
        }
        canvas.drawGauge(gauge!!, x - barSize / 2, y + barSize / 2, barSize, displayLife / maxLife)
    }

    override fun onRecycle() {
    }
}

