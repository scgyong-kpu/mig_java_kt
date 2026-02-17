package kr.ac.tukorea.ge.spgp2025.a2dg.framework.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.util.AttributeSet
import android.util.Log
import android.view.Choreographer
import android.view.MotionEvent
import android.view.View
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.scene.Scene

class GameView : View, Choreographer.FrameCallback {
    private var running = true
    private var emptyStackListener: OnEmptyStackListener? = null
    private val sceneStack = mutableListOf<Scene>()

    fun interface OnEmptyStackListener {
        fun onEmptyStack()
    }

    fun setEmptyStackListener(listener: OnEmptyStackListener?) {
        emptyStackListener = listener
    }

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, @Nullable attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    private fun init() {
        view = this
        scheduleUpdate()
    }

    fun pushScene(scene: Scene) {
        val last = sceneStack.size - 1
        if (last >= 0) {
            sceneStack[last].onPause()
        }
        sceneStack.add(scene)
        scene.onEnter()
    }

    fun popScene(): Scene? {
        val last = sceneStack.size - 1
        if (last < 0) {
            notifyEmptyStack()
            return null
        }
        val top = sceneStack.removeAt(last)
        top.onExit()
        if (last >= 1) {
            sceneStack[last - 1].onResume()
        } else {
            notifyEmptyStack()
        }
        return top
    }

    fun popAllScenes() {
        val count = sceneStack.size
        Log.d(TAG, "in popAllScenes(), scenes count = $count")
        for (i in count - 1 downTo 0) {
            sceneStack[i].onExit()
        }
        sceneStack.clear()
        if (count > 0) {
            notifyEmptyStack()
        }
    }

    private fun notifyEmptyStack() {
        emptyStackListener?.onEmptyStack()
        Log.d(TAG, "notifyEmptyStack() is called")
    }

    fun changeScene(scene: Scene) {
        val last = sceneStack.size - 1
        if (last < 0) return
        sceneStack[last].onExit()
        sceneStack[last] = scene
        scene.onEnter()
    }

    fun getTopScene(): Scene? {
        val last = sceneStack.size - 1
        return if (last < 0) null else sceneStack[last]
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        Metrics.onSize(w, h)
    }

    override fun onDraw(@NonNull canvas: Canvas) {
        super.onDraw(canvas)
        canvas.save()
        Metrics.concat(canvas)
        if (drawsDebugStuffs) {
            drawDebugBackground(canvas)
        }
        val topSceneIndex = sceneStack.size - 1
        if (topSceneIndex >= 0) {
            draw(canvas, topSceneIndex)
        }
        canvas.restore()
        if (drawsDebugStuffs) {
            drawDebugInfo(canvas)
        }
    }

    private fun draw(canvas: Canvas, sceneIndex: Int) {
        val scene = sceneStack[sceneIndex]
        if (scene.isTransparent()) {
            draw(canvas, sceneIndex - 1)
        }
        scene.draw(canvas)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val scene = getTopScene()
        return if (scene != null) {
            scene.onTouchEvent(event)
        } else {
            super.onTouchEvent(event)
        }
    }

    fun onBackPressed() {
        val last = sceneStack.size - 1
        if (last < 0) {
            notifyEmptyStack()
            return
        }

        val scene = sceneStack[last]
        val handled = scene.onBackPressed()
        if (handled) return

        popScene()
    }

    private fun scheduleUpdate() {
        Choreographer.getInstance().postFrameCallback(this)
    }

    override fun doFrame(nanos: Long) {
        if (previousNanos != 0L) {
            frameTime = (nanos - previousNanos) / 1_000_000_000f
            update()
            invalidate()
        }
        previousNanos = nanos
        if (running) {
            scheduleUpdate()
        }
    }

    private fun update() {
        val scene = getTopScene()
        scene?.update()
    }

    fun pauseGame() {
        running = false
        val scene = getTopScene()
        scene?.onPause()
    }

    fun resumeGame() {
        if (running) return
        running = true
        previousNanos = 0
        scheduleUpdate()
        val scene = getTopScene()
        scene?.onResume()
    }

    fun destroyGame() {
        popAllScenes()
        view = null
    }

    private var borderPaint: Paint? = null
    private var gridPaint: Paint? = null
    private var fpsPaint: Paint? = null

    private fun drawDebugBackground(@NonNull canvas: Canvas) {
        if (borderPaint == null) {
            borderPaint = Paint().apply {
                style = Paint.Style.STROKE
                strokeWidth = 10f
                color = Color.RED
            }

            gridPaint = Paint().apply {
                style = Paint.Style.STROKE
                strokeWidth = 1f
                color = Color.GRAY
            }
        }

        canvas.drawRect(Metrics.borderRect, borderPaint!!)
        var x = Metrics.GRID_UNIT
        while (x < Metrics.width) {
            canvas.drawLine(x, 0f, x, Metrics.height, gridPaint!!)
            x += Metrics.GRID_UNIT
        }
        var y = Metrics.GRID_UNIT
        while (y < Metrics.height) {
            canvas.drawLine(0f, y, Metrics.width, y, gridPaint!!)
            y += Metrics.GRID_UNIT
        }
    }

    private fun drawDebugInfo(canvas: Canvas) {
        if (fpsPaint == null) {
            fpsPaint = Paint().apply {
                color = Color.BLUE
                typeface = Typeface.MONOSPACE
                textSize = 80f
            }
        }

        val topSceneIndex = sceneStack.size - 1
        val scene = if (topSceneIndex >= 0) sceneStack[topSceneIndex] else null
        val fps = (1.0f / frameTime).toInt()
        val count = scene?.count() ?: 0
        val countsForLayers = scene?.getDebugCounts() ?: ""
        canvas.drawText("FPS: $fps", 80f, 80f, fpsPaint!!)
        canvas.drawText("$count $countsForLayers", 80f, 160f, fpsPaint!!)
    }

    companion object {
        private const val TAG = "GameView"
        private var previousNanos = 0L
        var frameTime = 0f
        var view: GameView? = null
        var drawsDebugStuffs = false
    }
}

