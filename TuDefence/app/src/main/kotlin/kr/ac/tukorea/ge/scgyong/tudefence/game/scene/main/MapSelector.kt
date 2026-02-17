package kr.ac.tukorea.ge.scgyong.tudefence.game.scene.main

import android.view.MotionEvent
import kr.ac.tukorea.ge.scgyong.tudefence.R
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.interfaces.IGameObject
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.objects.Sprite
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.view.Metrics

class MapSelector(val mainScene: MainScene) : Sprite(0) {
    private val tiles = mutableListOf<Sprite>()
    private var selectedTile: Sprite? = null

    init {
//        val bg = mainScene.tiledBg
//        val tw = bg.tileWidth
//        val th = bg.tileHeight
//        for (x in 0 until 32) {
//            for (y in 0 until 18) {
//                if (bg.canInstallAt(x, y)) {
//                    val tile = Sprite(R.mipmap.tile_select)
//                    tile.setPosition(x * tw + tw / 2, y * th + th / 2, tw * 0.8f, th * 0.8f)
//                    tiles.add(tile)
//                    mainScene.add(MainScene.Layer.selection, tile)
//                }
//            }
//        }
    }

    fun onTouch(action: Int, x: Float, y: Float): Boolean {
        when (action) {
            MotionEvent.ACTION_DOWN -> {
                selectedTile = tiles.find { it.dstRect.contains(x, y) }
                return selectedTile != null
            }
            MotionEvent.ACTION_UP -> {
                if (selectedTile != null) {
                    val cannon = Cannon(1, selectedTile!!.x, selectedTile!!.y)
                    mainScene.add(MainScene.Layer.cannon, cannon)
                    selectedTile = null
                    return true
                }
            }
        }
        return false
    }

    override fun update() {
    }

    override fun draw(canvas: android.graphics.Canvas) {
    }
}

