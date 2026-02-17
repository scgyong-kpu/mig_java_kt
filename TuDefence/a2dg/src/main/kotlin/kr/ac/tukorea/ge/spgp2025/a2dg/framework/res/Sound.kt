package kr.ac.tukorea.ge.spgp2025.a2dg.framework.res

import android.media.AudioAttributes
import android.media.MediaPlayer
import android.media.SoundPool
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.view.GameView

object Sound {
    private var mediaPlayer: MediaPlayer? = null
    private var soundPool: SoundPool? = null
    private val soundIdMap = mutableMapOf<Int, Int>()

    fun playMusic(resId: Int) {
        mediaPlayer?.stop()
        mediaPlayer = MediaPlayer.create(GameView.view.context, resId)
        mediaPlayer?.isLooping = true
        mediaPlayer?.start()
    }

    fun stopMusic() {
        mediaPlayer?.stop()
        mediaPlayer = null
    }

    fun pauseMusic() {
        mediaPlayer?.pause()
    }

    fun resumeMusic() {
        mediaPlayer?.start()
    }

    fun playEffect(resId: Int) {
        val pool = getSoundPool()
        var soundId: Int
        if (soundIdMap.containsKey(resId)) {
            soundId = soundIdMap[resId] ?: 0
        } else {
            soundId = pool.load(GameView.view.context, resId, 1)
            soundIdMap[resId] = soundId
        }
        pool.play(soundId, 1f, 1f, 1, 0, 1f)
    }

    private fun getSoundPool(): SoundPool {
        if (soundPool != null) return soundPool!!

        val attrs = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_GAME)
            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
            .build()
        soundPool = SoundPool.Builder()
            .setAudioAttributes(attrs)
            .setMaxStreams(3)
            .build()
        return soundPool!!
    }
}

