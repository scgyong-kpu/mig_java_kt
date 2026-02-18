package kr.ac.tukorea.ge.spgp2025.a2dg.framework.interfaces

import android.graphics.Canvas

interface IGameObject {
    var layerIndex: Int
    fun update()
    fun draw(canvas: Canvas)
}

