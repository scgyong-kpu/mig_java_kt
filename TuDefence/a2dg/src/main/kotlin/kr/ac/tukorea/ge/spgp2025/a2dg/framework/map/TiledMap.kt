package kr.ac.tukorea.ge.spgp2025.a2dg.framework.map

import com.fasterxml.jackson.annotation.JsonProperty

class TiledMap {
    @JsonProperty("compressionlevel")
    var compressionlevel: Long = 0

    @JsonProperty("height")
    var height: Long = 0

    @JsonProperty("infinite")
    var infinite: Boolean = false

    @JsonProperty("layers")
    var layers: Array<MapLayer>? = null

    @JsonProperty("nextlayerid")
    var nextlayerid: Long = 0

    @JsonProperty("nextobjectid")
    var nextobjectid: Long = 0

    @JsonProperty("orientation")
    var orientation: String? = null

    @JsonProperty("renderorder")
    var renderorder: String? = null

    @JsonProperty("tiledversion")
    var tiledversion: String? = null

    @JsonProperty("tileheight")
    var tileheight: Long = 0

    @JsonProperty("tilesets")
    var tilesets: Array<Tileset>? = null

    @JsonProperty("tilewidth")
    var tilewidth: Long = 0

    @JsonProperty("type")
    var type: String? = null

    @JsonProperty("version")
    var version: String? = null

    @JsonProperty("width")
    var width: Long = 0
}

