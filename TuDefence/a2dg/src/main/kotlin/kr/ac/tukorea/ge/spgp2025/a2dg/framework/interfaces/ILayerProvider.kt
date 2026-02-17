package kr.ac.tukorea.ge.spgp2025.a2dg.framework.interfaces

interface ILayerProvider<E : Enum<E>> : IGameObject {
    fun getLayer(): E
}

