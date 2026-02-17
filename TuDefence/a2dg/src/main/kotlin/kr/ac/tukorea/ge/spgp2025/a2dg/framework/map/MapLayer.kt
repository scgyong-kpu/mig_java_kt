package kr.ac.tukorea.ge.spgp2025.a2dg.framework.map

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class MapLayer(
    val data: IntArray? = null,
    val height: Int = 0,
    val id: Int = 0,
    val name: String? = null,
    val opacity: Int = 0,
    val type: String? = null,
    val visible: Boolean = false,
    val width: Int = 0,
    val x: Int = 0,
    val y: Int = 0
) {
    fun tileAt(x: Int, y: Int): Int {
        if (x >= width) return -1
        if (y >= height) return -1
        val index = y * width + x
        return data?.get(index)?.toInt() ?: -1
    }
}

