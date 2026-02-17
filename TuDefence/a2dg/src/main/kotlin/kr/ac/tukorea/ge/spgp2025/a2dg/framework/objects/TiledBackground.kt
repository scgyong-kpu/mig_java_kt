package kr.ac.tukorea.ge.spgp2025.a2dg.framework.objects

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.RectF
import android.util.Log
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.map.Converter
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.map.MapLayer
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.map.TiledMap
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.map.Tileset
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.interfaces.IGameObject
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.view.GameView
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.view.Metrics

open class TiledBackground(mapAssetFile: String, var tileWidth: Float, var tileHeight: Float) : IGameObject {
    val map: TiledMap
    val assetPath: String
    var tileset: Tileset? = null
    var layer: MapLayer? = null
    var bitmap: Bitmap? = null
    val srcRect = Rect()
    val dstRect = RectF()
    var scrollX = 0f
    var scrollY = 0f
    var wraps = false

    init {
        map = loadMap(mapAssetFile)!!
        assetPath = getDirectory(mapAssetFile)
        Log.d(TAG, "Map file $mapAssetFile has ${map.layers!!.size} layer(s) and ${map.tilesets!!.size} tileset(s).")
        setActiveTileset(0)
        setActiveLayer(0)
    }

    fun scrollTo(x: Float, y: Float) {
        scrollX = x
        scrollY = y
    }


    private fun loadMap(fileName: String): TiledMap? {
        return try {
            val json = loadAssetAsString(fileName)
            Converter.fromJsonString(json)
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    @Throws(IOException::class)
    private fun loadAssetAsString(fileName: String): String {
        val context = GameView.view!!.context
        val assets = context.assets
        val inputStream = assets.open(fileName)
        val reader = BufferedReader(InputStreamReader(inputStream))
        val builder = StringBuilder()
        var line: String?
        while (reader.readLine().also { line = it } != null) {
            builder.append(line)
        }
        return builder.toString()
    }

    private fun getDirectory(filePath: String): String {
        val lastSlash = filePath.lastIndexOf('/')
        return if (lastSlash >= 0) filePath.substring(0, lastSlash + 1) else ""
    }

    fun setActiveTileset(index: Int) {
        tileset = map.tilesets!![index]
        val imageFile = assetPath + tileset!!.image
        bitmap = loadImage(imageFile)
    }

    fun setActiveLayer(index: Int) {
        layer = map.layers!![index]
    }

    private fun loadImage(fileName: String): Bitmap {
        val context = GameView.view!!.context
        val assets = context.assets
        val inputStream = assets.open(fileName)
        return BitmapFactory.decodeStream(inputStream)
    }

    override fun update() {
    }

    override fun draw(canvas: Canvas) {
        val layer = layer ?: return
        val tileset = tileset ?: return

        val startCol = (scrollX / tileWidth).toInt()
        val startRow = (scrollY / tileHeight).toInt()
        val endCol = startCol + (Metrics.width / tileWidth).toInt() + 1
        val endRow = startRow + (Metrics.height / tileHeight).toInt() + 1

        for (row in startRow..endRow) {
            for (col in startCol..endCol) {
                val drawCol = if (wraps) col % layer.width.toInt() else col
                val drawRow = if (wraps) row % layer.height.toInt() else row

                if (drawCol < 0 || drawCol >= layer.width.toInt() || drawRow < 0 || drawRow >= layer.height.toInt()) continue

                val tileId = layer.data!![(drawRow.toLong() * layer.width + drawCol.toLong()).toInt()].toInt()
                if (tileId == 0) continue

                val screenX = col * tileWidth - scrollX
                val screenY = row * tileHeight - scrollY

                tileset.getRect(srcRect, tileId)
                dstRect.set(screenX, screenY, screenX + tileWidth, screenY + tileHeight)
                canvas.drawBitmap(bitmap!!, srcRect, dstRect, null)
            }
        }
    }

    companion object {
        private const val TAG = "TiledBackground"
    }
}

