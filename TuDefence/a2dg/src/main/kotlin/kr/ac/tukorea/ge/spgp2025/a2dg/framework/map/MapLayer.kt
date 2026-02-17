package kr.ac.tukorea.ge.spgp2025.a2dg.framework.map

import com.fasterxml.jackson.annotation.JsonProperty

class MapLayer {
    @JsonProperty("data")
    var data: LongArray? = null

    @JsonProperty("height")
    var height: Long = 0

    @JsonProperty("id")
    var id: Long = 0

    @JsonProperty("name")
    var name: String? = null

    @JsonProperty("opacity")
    var opacity: Long = 0

    @JsonProperty("type")
    var type: String? = null

    @JsonProperty("visible")
    var visible: Boolean = false

    @JsonProperty("width")
    var width: Long = 0

    @JsonProperty("x")
    var x: Long = 0

    @JsonProperty("y")
    var y: Long = 0

    fun tileAt(x: Int, y: Int): Int {
        if (x >= width.toInt()) return -1
        if (y >= height.toInt()) return -1
        val index = (y.toLong() * width + x.toLong()).toInt()
        return data?.get(index)?.toInt() ?: -1
    }
}

