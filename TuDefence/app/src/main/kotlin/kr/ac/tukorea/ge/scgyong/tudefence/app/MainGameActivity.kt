package kr.ac.tukorea.ge.scgyong.tudefence.app

import android.os.Bundle
import kr.ac.tukorea.ge.scgyong.tudefence.BuildConfig
import kr.ac.tukorea.ge.scgyong.tudefence.game.scene.main.MainScene
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.activity.GameActivity
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.view.GameView
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.view.Metrics

class MainGameActivity : GameActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        GameView.drawsDebugStuffs = BuildConfig.DEBUG
        Metrics.setGameSize(3200f, 1800f)
        MainScene().push()
    }
}

