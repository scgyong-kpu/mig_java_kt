package kr.ac.tukorea.ge.spgp2025.a2dg.framework.res

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.view.GameView

object BitmapPool {
    private const val TAG = "BitmapPool"
    private val bitmaps = mutableMapOf<Int, Bitmap>()
    private var opts: BitmapFactory.Options? = null

    fun get(mipmapResId: Int): Bitmap {
        var bitmap = bitmaps[mipmapResId]
        if (bitmap == null) {
            if (opts == null) {
                opts = BitmapFactory.Options()
                opts!!.inScaled = false
            }
            val res: Resources = GameView.view!!.resources
            bitmap = BitmapFactory.decodeResource(res, mipmapResId, opts)
            Log.d(TAG, "Bitmap " + res.getResourceEntryName(mipmapResId) + "(" + mipmapResId + ") : " + bitmap.width + "x" + bitmap.height)
            bitmaps[mipmapResId] = bitmap
        }
        return bitmap
    }

    fun clear() {
        opts = null
        bitmaps.clear()
    }
}

