package kr.ac.tukorea.ge.spgp2025.a2dg.framework.activity

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.view.GameView

open class GameActivity : AppCompatActivity() {
    private lateinit var gameView: GameView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        gameView = GameView(this)
        setContentView(gameView)

        setFullScreen()

        gameView.setEmptyStackListener {
            finish()
        }
        onBackPressedDispatcher.addCallback(onBackPressedCallback)
    }

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            gameView.onBackPressed()
        }
    }

    override fun onPause() {
        gameView.pauseGame()
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        gameView.resumeGame()
    }

    override fun onDestroy() {
        Log.i(TAG, "onDestroy")
        gameView.destroyGame()
        super.onDestroy()
    }

    @Suppress("DEPRECATION")
    private fun setFullScreen() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // API 30 이상: 최신 방식
            val insetsController = window.insetsController
            if (insetsController != null) {
                insetsController.setSystemBarsBehavior(
                    WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
                )
                insetsController.hide(WindowInsets.Type.systemBars())
            }
        } else {
            // API 29 이하: 기존 방식
            val flags = (View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                    View.SYSTEM_UI_FLAG_FULLSCREEN or
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)
            gameView.systemUiVisibility = flags
        }
    }

    companion object {
        private const val TAG = "GameActivity"
    }
}

