package kr.ac.tukorea.ge.spgp2025.a2dg.framework.interfaces

import android.view.MotionEvent

interface ITouchable {
    fun onTouchEvent(e: MotionEvent): Boolean
}

