package kr.ac.tukorea.ge.scgyong.tudefence.game.scene.main

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.interfaces.IGameObject
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.view.GameView
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.view.Metrics

class WaveGen(val mainScene: MainScene) : IGameObject {
    override var layerIndex: Int = -1
    
    companion object {
        private const val TAG = "WaveGen"
    }

    private var waveTime = 0f
    private var waveCount = 0
    private var spawnedCount = 0

    init {
        startWave()
    }

    private fun startWave() {
        waveCount++
        spawnedCount = 0
        waveTime = 0f
    }

    override fun update() {
        waveTime += GameView.frameTime
        val spawnInterval = 1.5f - (waveCount * 0.05f)
        if (waveTime > spawnInterval && spawnedCount < 10 + waveCount * 2) {
            val fly = Fly.get(spawnedCount == 0, 1f + waveCount * 0.1f)
            mainScene.add(MainScene.Layer.enemy, fly)
            spawnedCount++
            waveTime = 0f
        }
    }

    override fun draw(canvas: Canvas) {
    }
}

