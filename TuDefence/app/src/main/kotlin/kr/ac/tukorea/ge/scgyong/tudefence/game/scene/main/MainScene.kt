package kr.ac.tukorea.ge.scgyong.tudefence.game.scene.main

import android.view.MotionEvent
import kr.ac.tukorea.ge.scgyong.tudefence.R
import kr.ac.tukorea.ge.scgyong.tudefence.game.scene.pause.PauseScene
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.objects.Score
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.scene.Scene
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.view.Metrics

class MainScene : Scene() {
    companion object {
        private const val TAG = "MainScene"
    }

    protected val tiledBg: DesertMapBg
    protected val mapSelector: MapSelector
    protected val score: Score

    enum class Layer {
        bg, enemy, cannon, shell, explosion, score, selection, controller
    }

    init {
        initLayers(Layer.values().size)
        tiledBg = DesertMapBg()
        add(Layer.bg, tiledBg)
        add(Layer.selection, MapSelector(this).also { mapSelector = it })
        add(Layer.controller, WaveGen(this))

        score = Score(R.mipmap.gold_number, Metrics.width - 50, 50f, 100f)
        score.setScore(30)
        add(Layer.score, score)
    }

    override fun onBackPressed(): Boolean {
        PauseScene().push()
        return true
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val action = event.action
        val pts = Metrics.fromScreen(event.x, event.y)
        return mapSelector.onTouch(action, pts[0], pts[1])
    }
}

