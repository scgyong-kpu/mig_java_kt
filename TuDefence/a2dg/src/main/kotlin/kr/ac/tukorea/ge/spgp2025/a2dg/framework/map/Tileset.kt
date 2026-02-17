package kr.ac.tukorea.ge.spgp2025.a2dg.framework.map

import android.graphics.Rect
import com.fasterxml.jackson.annotation.JsonProperty

class Tileset {
    @JsonProperty("columns")
    var columns: Long = 0

    @JsonProperty("firstgid")
    var firstgid: Long = 0

    @JsonProperty("image")
    var image: String? = null

    @JsonProperty("imageheight")
    var imageheight: Long = 0

    @JsonProperty("imagewidth")
    var imagewidth: Long = 0

    @JsonProperty("margin")
    var margin: Long = 0

    @JsonProperty("name")
    var name: String? = null

    @JsonProperty("spacing")
    var spacing: Long = 0

    @JsonProperty("tilecount")
    var tilecount: Long = 0

    @JsonProperty("tileheight")
    var tileheight: Long = 0

    @JsonProperty("tilewidth")
    var tilewidth: Long = 0

    fun getRect(rect: Rect, tileNo: Int) {
        val x = ((tileNo - 1) % columns).toInt()
        val y = ((tileNo - 1) / columns).toInt()
        rect.left = (x * (tilewidth + spacing) + margin).toInt()
        rect.top = (y * (tileheight + spacing) + margin).toInt()
        rect.right = (rect.left + tilewidth.toInt())
        rect.bottom = (rect.top + tileheight.toInt())
    }
}

