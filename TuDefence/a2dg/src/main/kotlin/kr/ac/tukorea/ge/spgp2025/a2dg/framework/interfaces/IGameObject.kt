package kr.ac.tukorea.ge.spgp2025.a2dg.framework.interfaces

import android.graphics.Canvas

interface IGameObject {
    fun update()
    fun draw(canvas: Canvas)
}

