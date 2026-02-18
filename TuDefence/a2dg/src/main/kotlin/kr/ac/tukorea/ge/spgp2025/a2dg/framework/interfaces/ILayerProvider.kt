package kr.ac.tukorea.ge.spgp2025.a2dg.framework.interfaces

interface ILayerProvider<E : Enum<E>> {
    fun getLayer(): E
}

