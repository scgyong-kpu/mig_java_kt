package kr.ac.tukorea.ge.scgyong.tudefence.game.scene.main

import kr.ac.tukorea.ge.scgyong.tudefence.R
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.interfaces.IRecyclable
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.objects.AnimSprite
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.scene.Scene

class Explosion(x: Float, y: Float, radius: Float) : AnimSprite(R.mipmap.explosion, 10f), IRecyclable {
    companion object {
        fun get(x: Float, y: Float, radius: Float): Explosion {
            return Scene.top()!!.getRecyclable(Explosion::class.java)!!.also {
                it.setPosition(x, y, radius)
            }
        }
    }

    override fun onRecycle() {
    }
}

