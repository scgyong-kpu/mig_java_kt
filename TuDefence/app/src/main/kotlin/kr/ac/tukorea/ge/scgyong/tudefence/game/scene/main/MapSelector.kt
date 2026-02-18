package kr.ac.tukorea.ge.scgyong.tudefence.game.scene.main

import android.animation.ValueAnimator
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.Log
import android.view.MotionEvent
import kr.ac.tukorea.ge.scgyong.tudefence.BuildConfig
import kr.ac.tukorea.ge.scgyong.tudefence.R
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.interfaces.ILayerProvider
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.interfaces.LayerProvider
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.objects.Sprite
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.res.BitmapPool
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.view.GameView
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.view.Metrics

class MapSelector(val scene: MainScene) : Sprite(R.mipmap.selection),
    ILayerProvider<MainScene.Layer> by LayerProvider(MainScene.Layer.selection) {
    companion object {
        private val TAG = MapSelector::class.simpleName
        private const val TILE_SIZE = 100f
        private const val SELECTOR_SIZE = 2 * TILE_SIZE
        private val MENU_ITEMS_BLANK = intArrayOf()
        private val MENU_ITEMS_INSTALL = intArrayOf(
            R.mipmap.f_01_01, R.mipmap.f_02_01, R.mipmap.f_03_01
        )
        private val MENU_ITEMS_CANNON = intArrayOf(
            R.mipmap.upgrade, R.mipmap.uninstall
        )
        private const val ALPHA_ANIM_DURATION_MSEC = 300L
    }

    private var cannon: Cannon? = null
    private val menuBgBitmap: Bitmap = BitmapPool.get(R.mipmap.menu_bg)
    private val notAvailableBitmap: Bitmap = BitmapPool.get(R.mipmap.not_available)
    private var menuItems: IntArray = MENU_ITEMS_BLANK
    private val alphaPaint = Paint()
    private var alphaAnimator: ValueAnimator? = null
    private val menuRect = RectF()

    init {
        setPosition(-SELECTOR_SIZE, -SELECTOR_SIZE, SELECTOR_SIZE, SELECTOR_SIZE)
    }

    private fun hideSelector() {
        setPosition(-SELECTOR_SIZE, -SELECTOR_SIZE)
    }

    override fun draw(canvas: Canvas) {
        super.draw(canvas)
        cannon?.drawRange(canvas)
        prepareMenuRect()
        for (item in menuItems) {
            menuRect.offset(SELECTOR_SIZE, 0f)
            canvas.drawBitmap(menuBgBitmap, null, menuRect, alphaPaint)
            val itemBitmap = BitmapPool.get(item)
            canvas.drawBitmap(itemBitmap, null, menuRect, alphaPaint)
            if (prohibits(item)) {
                canvas.drawBitmap(notAvailableBitmap, null, menuRect, alphaPaint)
            }
        }
    }

    private fun prohibits(item: Int): Boolean {
        return when (item) {
            R.mipmap.f_01_01 -> !canInstall(1)
            R.mipmap.f_02_01 -> !canInstall(2)
            R.mipmap.f_03_01 -> !canInstall(3)
            R.mipmap.upgrade -> {
                if (cannon == null) return true
                val score = scene.score.score
                val cost = cannon!!.getUpgradeCost()
                cost > score
            }
            else -> false
        }
    }

    private fun canInstall(level: Int): Boolean {
        val cost = Cannon.getInstallationCost(level)
        val score = scene.score.score
        return cost <= score
    }

    private fun prepareMenuRect() {
        menuRect.set(dstRect)
        val right = dstRect.right + SELECTOR_SIZE * menuItems.size
        if (right > Metrics.width) {
            menuRect.offset(-SELECTOR_SIZE * (menuItems.size + 1), 0f)
        }
    }

    fun onTouch(action: Int, x: Float, y: Float): Boolean {
        val processed = handleMenuItem(action, x, y)
        if (processed) {
            return false
        }
        cannon = findCannonAt(x, y)
        if (cannon != null) {
            Log.d(TAG, "Found: $cannon")
            if (action == MotionEvent.ACTION_UP) {
                setMenuItems(*MENU_ITEMS_CANNON)
            } else {
                bitmap = BitmapPool.get(R.mipmap.selection)
                setPosition(cannon!!.x, cannon!!.y)
                setMenuItems(*MENU_ITEMS_BLANK)
            }
            return true
        }
        val mapX = (x / TILE_SIZE).toInt()
        val mapY = (y / TILE_SIZE).toInt()
        val cx = (mapX + 1) * TILE_SIZE
        val cy = (mapY + 1) * TILE_SIZE

        setPosition(cx, cy)

        val possible = !intersectsIfInstalledAt(cx, cy) && scene.tiledBg.canInstallAt(mapX, mapY)
        val resId = if (possible) R.mipmap.selection else R.mipmap.sel_non_installable
        bitmap = BitmapPool.get(resId)

        if (action != MotionEvent.ACTION_UP) {
            setMenuItems(*MENU_ITEMS_BLANK)
            return true
        }
        if (!possible) {
            hideSelector()
            return true
        }
        setMenuItems(*MENU_ITEMS_INSTALL)
        return true
    }

    private fun handleMenuItem(action: Int, x: Float, y: Float): Boolean {
        if (action != MotionEvent.ACTION_DOWN || menuItems.isEmpty()) return false
        prepareMenuRect()
        for (item in menuItems) {
            menuRect.offset(SELECTOR_SIZE, 0f)
            if (menuRect.contains(x, y)) {
                if (BuildConfig.DEBUG) {
                    val res = GameView.view!!.resources
                    val name = res.getResourceEntryName(item)
                    Log.d(TAG, "메뉴 선택: $name($item)")
                }
                val done = doItemAction(item)
                if (done) {
                    hideSelector()
                }
                return true
            }
        }
        return false
    }

    private fun doItemAction(menuItem: Int): Boolean {
        return when (menuItem) {
            R.mipmap.f_01_01 -> installCannon(1)
            R.mipmap.f_02_01 -> installCannon(2)
            R.mipmap.f_03_01 -> installCannon(3)
            R.mipmap.upgrade -> upgradeCannon()
            R.mipmap.uninstall -> uninstallCannon()
            else -> false
        }
    }

    private fun installCannon(level: Int): Boolean {
        val cost = Cannon.getInstallationCost(level)
        val score = scene.score.score
        if (cost > score) return false
        scene.score.score = score - cost
        val newCannon = Cannon(level, x, y)
        scene.add(newCannon)
        return true
    }

    private fun upgradeCannon(): Boolean {
        if (cannon == null) return false
        val cost = cannon!!.getUpgradeCost()
        val score = scene.score.score
        if (cost > score) return false
        scene.score.score = score - cost
        cannon!!.upgrade()
        return true
    }

    private fun uninstallCannon(): Boolean {
        if (cannon == null) return false
        val price = cannon!!.getSellPrice()
        scene.score.score += price
        cannon!!.uninstall()
        cannon = null
        return true
    }

    private fun setMenuItems(vararg items: Int) {
        menuItems = items
        if (BuildConfig.DEBUG) {
            val res = GameView.view!!.resources
            val names = items.map { resId ->
                val name = res.getResourceEntryName(resId)
                "$name($resId)"
            }
            Log.d(TAG, "메뉴 아이템 = $names")
        }

        if (alphaAnimator == null) {
            alphaAnimator = ValueAnimator.ofInt(0, 192).apply {
                duration = ALPHA_ANIM_DURATION_MSEC
                addUpdateListener { animator ->
                    alphaPaint.alpha = animator.animatedValue as Int
                }
            }
        }
        alphaAnimator!!.start()
    }

    private fun findCannonAt(x: Float, y: Float): Cannon? {
        for (obj in scene.objectsAt(MainScene.Layer.cannon)) {
            val c = obj as? Cannon ?: continue
            if (c.containsPoint(x, y)) {
                return c
            }
        }
        return null
    }

    private fun intersectsIfInstalledAt(x: Float, y: Float): Boolean {
        for (obj in scene.objectsAt(MainScene.Layer.cannon)) {
            val c = obj as? Cannon ?: continue
            if (c.intersectsIfInstalledAt(x, y)) {
                Log.d(TAG, "교차함: $c")
                return true
            }
        }
        return false
    }

    override fun update() {
    }
}

