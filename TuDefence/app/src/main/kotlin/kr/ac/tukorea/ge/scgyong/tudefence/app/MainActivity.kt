package kr.ac.tukorea.ge.scgyong.tudefence.app

import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import androidx.activity.EdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.Insets
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kr.ac.tukorea.ge.scgyong.tudefence.BuildConfig
import kr.ac.tukorea.ge.scgyong.tudefence.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (BuildConfig.DEBUG) {
            startGameActivity()
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            startGameActivity()
        }
        return super.onTouchEvent(event)
    }

    private fun startGameActivity() {
        val intent = Intent(this, MainGameActivity::class.java)
        startActivity(intent)
    }
}

