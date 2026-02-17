package kr.ac.tukorea.ge.scgyong.tudefence.game.scene.pause

import android.widget.Toast
import kr.ac.tukorea.ge.scgyong.tudefence.R
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.objects.Button
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.objects.Sprite
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.scene.Scene
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.view.GameView
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.view.Metrics

class PauseScene : Scene() {
    enum class PauseLayer {
        bg, ui, COUNT
    }

    private val createdOn: Long = System.currentTimeMillis()
    private val toast: Toast

    init {
        initLayers(PauseLayer.values().size)
        val bg = Sprite(R.mipmap.trans_50b)
        val w = Metrics.width
        val h = Metrics.height
        bg.setPosition(w / 2, h / 2, w, h)
        add(PauseLayer.bg, bg)

        val btn_y1 = h / 2 - 120
        val btn_y2 = h / 2 + 120
        add(PauseLayer.ui, Button(R.mipmap.btn_resume_n, w / 2, btn_y1, 512f, 192f) { pressed ->
            pop()
            false
        })
        add(PauseLayer.ui, Button(R.mipmap.btn_exit_n, w / 2, btn_y2, 512f, 192f) { pressed ->
            popAll()
            true
        })

        toast = Toast.makeText(GameView.view!!.context, R.string.back_press_msg, Toast.LENGTH_SHORT)
        toast.show()
    }

    override fun onBackPressed(): Boolean {
        val now = System.currentTimeMillis()
        if (now - createdOn < 500) {
            popAll()
            toast.cancel()
            return true
        }
        return super.onBackPressed()
    }

    override fun isTransparent(): Boolean = true

    override fun getTouchLayerIndex(): Int = PauseLayer.ui.ordinal
}

