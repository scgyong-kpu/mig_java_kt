package kr.ac.tukorea.ge.scgyong.tudefence.game.scene.main

import android.graphics.Rect
import android.util.Log
import java.util.ArrayList
import kr.ac.tukorea.ge.scgyong.tudefence.R
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.interfaces.IGameObject
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.interfaces.IRecyclable
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.objects.Sprite
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.scene.Scene
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.util.CollisionHelper
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.view.Metrics
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin

class Shell : Sprite(R.mipmap.shells, 0f, 0f, 50f, 50f), IRecyclable {
    companion object {
        private const val TAG = "Shell"

        fun get(cannon: Cannon, target: Fly): Shell {
            return Scene.top()!!.getRecyclable(Shell::class.java)!!.init(cannon, target)
        }
    }

    var power = 0f
    var splashes = false

    init {
        srcRect = Rect()
    }

    private fun init(cannon: Cannon, target: Fly): Shell {
        val w = bitmap!!.width
        val h = bitmap!!.height
        val maxLevel = w / h
        var level = cannon.level
        if (level < 1) level = 1
        if (level > maxLevel) level = maxLevel
        srcRect!!.set(h * (level - 1), 0, h * level, h)

        val angleInDegrees = cannon.angle.toDouble()
        val radian = Math.toRadians(angleInDegrees)
        val speed = (level + 10) * 100
        dx = (speed * cos(radian)).toFloat()
        dy = (speed * sin(radian)).toFloat()
        power = (10 * 1.2.pow((level - 1).toDouble())).toFloat()
        splashes = level >= 4
        radius = 20f + level * 2f
        setPosition(cannon.x, cannon.y, radius)

        return this
    }

    override fun update() {
        super.update()
        val scene = Scene.top() as? MainScene ?: return
        if (x < -radius || x > Metrics.width + radius ||
            y < -radius || y > Metrics.height + radius) {
            scene.remove(MainScene.Layer.shell, this)
            return
        }

        val flies = scene.objectsAt(MainScene.Layer.enemy)
        for (index in flies.size - 1 downTo 0) {
            val fly = flies[index] as? Fly ?: continue
            val collides = CollisionHelper.collidesRadius(this, fly)
            if (collides) {
                scene.remove(MainScene.Layer.shell, this)
                hit(fly, power, scene)
                if (splashes) {
                    explode(scene, fly, flies)
                }
                break
            }
        }
    }

    private fun hit(fly: Fly, damage: Float, scene: MainScene) {
        Log.d(TAG, "Hit: $damage to: $fly")
        val dead = fly.decreaseLife(damage)
        if (dead) {
            scene.remove(MainScene.Layer.enemy, fly)
            scene.score.add(fly.score())
        }
    }

    private fun explode(scene: MainScene, flyHit: Fly, flies: MutableList<IGameObject>) {
        val fx = flyHit.x
        val fy = flyHit.y
        val explosion_radius = 60 + 3 * power
        val ex = Explosion.get(fx, fy, explosion_radius)
        scene.add(MainScene.Layer.explosion, ex)
        val radius_sq = explosion_radius * explosion_radius
        Log.v(TAG, "[Explosion")
        for (index in flies.size - 1 downTo 0) {
            val fly = flies[index] as? Fly ?: continue
            if (fly == flyHit) continue
            val dx = fx - fly.x
            val dy = fy - fly.y
            val dist_sq = (dx * dx + dy * dy).toDouble()
            if (dist_sq < radius_sq) {
                fly.decreaseLife(power / 2)
            }
        }
        Log.v(TAG, "]")
    }

    override fun onRecycle() {
    }
}

