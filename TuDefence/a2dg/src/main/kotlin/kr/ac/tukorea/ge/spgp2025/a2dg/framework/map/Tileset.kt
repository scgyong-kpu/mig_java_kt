package kr.ac.tukorea.ge.spgp2025.a2dg.framework.map

import android.graphics.Rect
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class Tileset(
    val columns: Int = 0,
    val firstgid: Int = 0,
    val image: String? = null,
    val imageheight: Int = 0,
    val imagewidth: Int = 0,
    val margin: Int = 0,
    val name: String? = null,
    val spacing: Int = 0,
    val tilecount: Int = 0,
    val tileheight: Int = 0,
    val tilewidth: Int = 0
) {
    fun getRect(rect: Rect, tileNo: Int) {
        val x = (tileNo - 1) % columns
        val y = (tileNo - 1) / columns
        rect.left = x * (tilewidth + spacing) + margin
        rect.top = y * (tileheight + spacing) + margin
        rect.right = rect.left + tilewidth
        rect.bottom = rect.top + tileheight
    }
}

