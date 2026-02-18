package kr.ac.tukorea.ge.spgp2025.a2dg.framework.scene

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.view.MotionEvent
import android.util.Log
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.interfaces.IBoxCollidable
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.interfaces.IGameObject
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.interfaces.ILayerProvider
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.interfaces.IRecyclable
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.interfaces.ITouchable
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.view.GameView
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.view.Metrics

open class Scene {
    protected val layers = mutableListOf<MutableList<IGameObject>>()
    private val recycleBin = mutableMapOf<Class<*>, MutableList<IRecyclable>>()
    private var capturingTouchable: ITouchable? = null

    protected fun initLayers(layerCount: Int) {
        layers.clear()
        repeat(layerCount) {
            layers.add(mutableListOf())
        }
    }

    fun <E : Enum<E>> add(layer: E, gameObject: IGameObject) {
        val layerIndex = layer.ordinal
        layers[layerIndex].add(gameObject)
    }

    fun <T> add(gameObject: T) where T : IGameObject, T : ILayerProvider<*> {
        val e = gameObject.getLayer()
        val layerIndex = e.ordinal
        layers[layerIndex].add(gameObject)
    }

    fun <E : Enum<E>> remove(layer: E, gobj: IGameObject) {
        val layerIndex = layer.ordinal
        remove(layerIndex, gobj)
    }

    fun <T> remove(gameObject: T) where T : IGameObject, T : ILayerProvider<*> {
        val e = gameObject.getLayer()
        val layerIndex = e.ordinal
        remove(layerIndex, gameObject)
    }

    private fun remove(layerIndex: Int, gobj: IGameObject) {
        layers[layerIndex].remove(gobj)
        if (gobj is IRecyclable) {
            collectRecyclable(gobj)
            gobj.onRecycle()
        }
    }

    fun <E : Enum<E>> objectsAt(layer: E): MutableList<IGameObject> {
        val layerIndex = layer.ordinal
        return layers[layerIndex]
    }

    fun count(): Int {
        var total = 0
        for (layer in layers) {
            total += layer.size
        }
        return total
    }

    fun <E : Enum<E>> countAt(layer: E): Int {
        val layerIndex = layer.ordinal
        return layers[layerIndex].size
    }

    fun getDebugCounts(): String {
        val sb = StringBuilder()
        for (gameObjects in layers) {
            if (sb.isEmpty()) {
                sb.append('[')
            } else {
                sb.append(',')
            }
            sb.append(gameObjects.size)
        }
        sb.append(']')
        return sb.toString()
    }

    fun collectRecyclable(obj: IRecyclable) {
        val clazz = obj.javaClass
        val bin = recycleBin.getOrPut(clazz) { mutableListOf() }
        bin.add(obj)
    }

    fun <T : IRecyclable> getRecyclable(clazz: Class<T>): T? {
        val bin = recycleBin[clazz]
        if (bin == null || bin.isEmpty()) {
            return try {
                clazz.newInstance()
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
        @Suppress("UNCHECKED_CAST")
        return bin.removeAt(0) as T
    }

    open fun update() {
        for (gameObjects in layers) {
            val count = gameObjects.size
            for (i in count - 1 downTo 0) {
                gameObjects[i].update()
            }
        }
    }

    open fun draw(canvas: Canvas) {
        if (clipsRect()) {
            canvas.clipRect(0f, 0f, Metrics.width, Metrics.height)
        }
        for (gameObjects in layers) {
            for (gobj in gameObjects) {
                gobj.draw(canvas)
            }
        }
        if (GameView.drawsDebugStuffs) {
            if (bboxPaint == null) {
                bboxPaint = Paint().apply {
                    style = Paint.Style.STROKE
                    color = Color.RED
                }
            }
            for (gameObjects in layers) {
                for (gobj in gameObjects) {
                    if (gobj is IBoxCollidable) {
                        val rect = gobj.getCollisionRect()
                        canvas.drawRect(rect, bboxPaint!!)
                    }
                }
            }
        }
    }

    fun change() {
        GameView.view?.changeScene(this)
    }

    fun push() {
        GameView.view?.pushScene(this)
    }

    open fun isTransparent(): Boolean = false

    protected open fun getTouchLayerIndex(): Int = -1

    open fun onTouchEvent(event: MotionEvent): Boolean {
        val touchLayer = getTouchLayerIndex()
        if (touchLayer < 0) return false
        if (capturingTouchable != null) {
            val processed = capturingTouchable!!.onTouchEvent(event)
            if (!processed || event.action == MotionEvent.ACTION_UP) {
                Log.d(TAG, "Capture End: $capturingTouchable")
                capturingTouchable = null
            }
            return processed
        }
        val gameObjects = layers[touchLayer]
        for (gobj in gameObjects) {
            if (gobj !is ITouchable) {
                continue
            }
            val processed = gobj.onTouchEvent(event)
            if (processed) {
                capturingTouchable = gobj
                Log.d(TAG, "Capture Start: $capturingTouchable")
                return true
            }
        }
        return false
    }

    open fun onEnter() {
        Log.v(TAG, "onEnter: ${this.javaClass.simpleName}")
    }

    open fun onPause() {
        Log.v(TAG, "onPause: ${this.javaClass.simpleName}")
    }

    open fun onResume() {
        Log.v(TAG, "onResume: ${this.javaClass.simpleName}")
    }

    open fun onExit() {
        Log.v(TAG, "onExit: ${this.javaClass.simpleName}")
    }

    open fun onBackPressed(): Boolean = false

    open fun clipsRect(): Boolean = true

    companion object {
        private const val TAG = "Scene"
        var bboxPaint: Paint? = null

        fun pop(): Scene? = GameView.view?.popScene()

        fun top(): Scene? = GameView.view?.getTopScene()

        fun popAll() = GameView.view?.popAllScenes()
    }
}

