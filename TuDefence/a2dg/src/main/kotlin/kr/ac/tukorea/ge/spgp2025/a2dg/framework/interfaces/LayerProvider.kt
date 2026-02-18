package kr.ac.tukorea.ge.spgp2025.a2dg.framework.interfaces

class LayerProvider<E : Enum<E>>(private val layer: E) : ILayerProvider<E> {
    override fun getLayer(): E = layer
}
