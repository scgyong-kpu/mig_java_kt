package kr.ac.tukorea.ge.scgyong.tudefence.game.scene.main

import kr.ac.tukorea.ge.spgp2025.a2dg.framework.objects.TiledBackground

class DesertMapBg : TiledBackground("map/desert.tmj", 100f, 100f) {
    companion object {
        const val TILE_INDEX_BRICK = 10
        private const val TAG = "DesertMapBg"
    }

    fun canInstallAt(x: Int, y: Int): Boolean {
        var tile = layer!!.tileAt(x, y)
        if (tile != TILE_INDEX_BRICK) return false
        tile = layer!!.tileAt(x + 1, y)
        if (tile != TILE_INDEX_BRICK) return false
        tile = layer!!.tileAt(x, y + 1)
        if (tile != TILE_INDEX_BRICK) return false
        tile = layer!!.tileAt(x + 1, y + 1)
        if (tile != TILE_INDEX_BRICK) return false
        return true
    }
}

