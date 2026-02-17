package kr.ac.tukorea.ge.spgp2025.a2dg.framework.map

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class TiledMap(
    val compressionLevel: Int = 0,
    val height: Int = 0,
    val infinite: Boolean = false,
    val layers: List<MapLayer> = emptyList(),
    val nextLayerId: Int = 0,
    val nextObjectId: Int = 0,
    val orientation: String? = null,
    val renderOrder: String? = null,
    val tiledVersion: String? = null,
    val tileHeight: Int = 0,
    val tilesets: List<Tileset> = emptyList(),
    val tileWidth: Int = 0,
    val type: String? = null,
    val version: String? = null,
    val width: Int = 0
)

