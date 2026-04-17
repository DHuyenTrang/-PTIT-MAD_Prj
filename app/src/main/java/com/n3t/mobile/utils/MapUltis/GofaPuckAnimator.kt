package com.n3t.mobile.utils.MapUltis

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.TypeEvaluator
import android.animation.ValueAnimator
import android.graphics.Color
import android.view.animation.LinearInterpolator
import androidx.annotation.ColorInt
import androidx.annotation.RestrictTo
import androidx.annotation.VisibleForTesting
import androidx.annotation.VisibleForTesting.Companion.PRIVATE
import androidx.core.view.animation.PathInterpolatorCompat
import com.mapbox.geojson.Point
import com.mapbox.maps.EdgeInsets
import com.mapbox.maps.ScreenCoordinate
import com.mapbox.maps.plugin.locationcomponent.LocationComponentConstants
import com.mapbox.maps.plugin.locationcomponent.OnIndicatorAccuracyRadiusChangedListener
import com.mapbox.maps.plugin.locationcomponent.OnIndicatorBearingChangedListener
import com.mapbox.maps.plugin.locationcomponent.OnIndicatorPositionChangedListener
import com.mapbox.maps.plugin.locationcomponent.generated.LocationComponentSettings
import com.mapbox.maps.threading.AnimationThreadController
import com.mapbox.maps.util.MathUtils

internal class GofaPuckAnimator(
    indicatorPositionChangedListener: OnIndicatorPositionChangedListener,
    indicatorBearingChangedListener: OnIndicatorBearingChangedListener,
    indicatorAccuracyRadiusChangedListener: OnIndicatorAccuracyRadiusChangedListener,
    pixelRatio: Float
) {
    private var bearingAnimator = GofaBearingAnimator(indicatorBearingChangedListener)
    private var positionAnimator = GofaPositionAnimator(indicatorPositionChangedListener)
    private var accuracyRadiusAnimator =
        GofaAccuracyRadiusAnimator(indicatorAccuracyRadiusChangedListener)
    private var pulsingAnimator = GofaPulsingAnimator(pixelRatio)

    internal var puckAnimationEnabled: Boolean
        get() = bearingAnimator.enabled
        set(value) {
            bearingAnimator.enabled = value
        }

    @VisibleForTesting(otherwise = PRIVATE)
    constructor(
        indicatorPositionChangedListener: OnIndicatorPositionChangedListener,
        indicatorBearingChangedListener: OnIndicatorBearingChangedListener,
        indicatorAccuracyRadiusChangedListener: OnIndicatorAccuracyRadiusChangedListener,
        bearingAnimator: GofaBearingAnimator,
        positionAnimator: GofaPositionAnimator,
        pulsingAnimator: GofaPulsingAnimator,
        radiusAnimator: GofaAccuracyRadiusAnimator,
        pixelRatio: Float
    ) : this(
        indicatorPositionChangedListener,
        indicatorBearingChangedListener,
        indicatorAccuracyRadiusChangedListener,
        pixelRatio
    ) {
        this.bearingAnimator = bearingAnimator
        this.positionAnimator = positionAnimator
        this.pulsingAnimator = pulsingAnimator
        this.accuracyRadiusAnimator = radiusAnimator
    }

    fun setUpdateListeners(
        onLocationUpdated: ((Point) -> Unit),
        onBearingUpdated: ((Double) -> Unit),
        onAccuracyRadiusUpdated: ((Double) -> Unit)
    ) {
        positionAnimator.setUpdateListener(onLocationUpdated)
        bearingAnimator.setUpdateListener(onBearingUpdated)
        accuracyRadiusAnimator.setUpdateListener(onAccuracyRadiusUpdated)
    }

    fun onStart() {
        if (pulsingAnimator.enabled) {
            pulsingAnimator.animateInfinite()
        }
    }

    fun onStop() {
        bearingAnimator.cancelRunning()
        positionAnimator.cancelRunning()
        pulsingAnimator.cancelRunning()
        accuracyRadiusAnimator.cancelRunning()
    }

    fun animateBearing(
        vararg targets: Double,
        options: (ValueAnimator.() -> Unit)?
    ) {
        bearingAnimator.animate(
            *MathUtils.prepareOptimalBearingPath(targets).toTypedArray(),
            options = options
        )
    }

    fun animatePosition(
        vararg targets: Point,
        options: (ValueAnimator.() -> Unit)?
    ) {
        positionAnimator.animate(*targets, options = options)
    }

    fun animateAccuracyRadius(
        vararg targets: Double,
        options: (ValueAnimator.() -> Unit)?
    ) {
        accuracyRadiusAnimator.animate(*targets.toTypedArray(), options = options)
    }

    fun updatePulsingRadius(target: Double, settings: LocationComponentSettings) {
        pulsingAnimator.apply {
            enabled = settings.pulsingEnabled
            if (settings.pulsingEnabled) {
                maxRadius = target
                animateInfinite()
            } else {
                cancelRunning()
            }
        }
    }

    fun applySettings(enabled: Boolean) {
        pulsingAnimator.apply {
            if (enabled) {
                animateInfinite()
            } else {
                cancelRunning()
            }
        }
    }

    fun updateBearingAnimator(block: ValueAnimator.() -> Unit) {
        bearingAnimator.updateOptions(block)
    }

    fun updatePositionAnimator(block: ValueAnimator.() -> Unit) {
        positionAnimator.updateOptions(block)
    }

    fun updateAccuracyRadiusAnimator(block: ValueAnimator.() -> Unit) {
        accuracyRadiusAnimator.updateOptions(block)
    }
}

internal class GofaBearingAnimator(private val indicatorBearingChangedListener: OnIndicatorBearingChangedListener) : GofaAnimator<Double>(
    Evaluators.DOUBLE) {

    override var enabled = true

    override fun updateLayer(fraction: Float, value: Double) {
        if (enabled) {
            indicatorBearingChangedListener.onIndicatorBearingChanged(value)
        }
    }
}

internal class GofaPositionAnimator(private val indicatorPositionChangedListener: OnIndicatorPositionChangedListener) : GofaAnimator<Point>(
    Evaluators.POINT) {

    override fun updateLayer(fraction: Float, value: Point) {
        indicatorPositionChangedListener.onIndicatorPositionChanged(value)
    }
}

internal class GofaAccuracyRadiusAnimator(private val accuracyRadiusChangedListener: OnIndicatorAccuracyRadiusChangedListener) :
    GofaAnimator<Double>(Evaluators.DOUBLE) {
    override fun updateLayer(fraction: Float, value: Double) {
        if (enabled) {
            accuracyRadiusChangedListener.onIndicatorAccuracyRadiusChanged(value)
        } else {
        }
    }
}

internal class GofaPulsingAnimator(private val pixelRatio: Float = 1.0f) :
    GofaAnimator<Double>(Evaluators.DOUBLE) {
    var maxRadius: Double = DEFAULT_RADIUS_DP * pixelRatio

    @ColorInt
    var pulsingColor: Int = Color.BLUE
    private var pulseFadeEnabled = true

    init {
        duration = PULSING_DEFAULT_DURATION
        repeatMode = RESTART
        repeatCount = INFINITE
        interpolator = PULSING_DEFAULT_INTERPOLATOR
    }

    fun animateInfinite() {
        if (maxRadius <= 0.0) {
            maxRadius = DEFAULT_RADIUS_DP * pixelRatio
        }
        if (!isRunning) {
            animate(0.0, maxRadius)
        }
        addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationRepeat(animation: Animator) {
                super.onAnimationRepeat(animation)
                setObjectValues(0.0, maxRadius)
            }
        })
    }

    override fun updateLayer(fraction: Float, value: Double) {
        var opacity = 1.0f
        if (pulseFadeEnabled) {
            opacity = (1.0f - (value / maxRadius).toFloat()).coerceIn(0.0f, 1.0f)
        }
    }

    internal companion object {
        const val PULSING_DEFAULT_DURATION = 3_000L
        const val DEFAULT_RADIUS_DP = 10.0
        private val PULSING_DEFAULT_INTERPOLATOR = PathInterpolatorCompat.create(
            0.0f,
            0.0f,
            0.25f,
            1.0f
        )
    }
}

internal abstract class GofaAnimator<T>(
    evaluator: TypeEvaluator<T>
) : ValueAnimator() {

    @VisibleForTesting(otherwise = PRIVATE)
    internal var updateListener: ((T) -> Unit)? = null
    @VisibleForTesting(otherwise = PRIVATE)
    internal var userConfiguredAnimator: ValueAnimator
    internal open var enabled = false

    abstract fun updateLayer(fraction: Float, value: T)

    init {
        setObjectValues(emptyArray<Any>())
        setEvaluator(evaluator)
        addUpdateListener {
            AnimationThreadController.postOnMainThread {
                @Suppress("UNCHECKED_CAST")
                val updatedValue = it.animatedValue as T
                updateLayer(it.animatedFraction, updatedValue)
                updateListener?.invoke(updatedValue)
            }
        }
        duration = LocationComponentConstants.DEFAULT_INTERVAL_MILLIS
        interpolator = DEFAULT_INTERPOLATOR
        userConfiguredAnimator = clone()
    }

    /**
     * Set the animator object values
     *
     * @param values varags of object values
     */
    final override fun setObjectValues(vararg values: Any?) {
        super.setObjectValues(*values)
    }

    /**
     * Set the animator evaluator
     *
     * @param value the animator type evaluator
     */
    final override fun setEvaluator(value: TypeEvaluator<*>?) {
        super.setEvaluator(value)
    }

    /**
     * Adds a listener to the set of listeners that are sent update events through the life of
     * an animation. This method is called on all listeners for every frame of the animation,
     * after the values for the animation have been calculated.
     *
     * @param listener the listener to be added to the current set of listeners for this animation.
     */
    final override fun addUpdateListener(listener: AnimatorUpdateListener?) {
        super.addUpdateListener(listener)
    }

    final override fun clone(): ValueAnimator {
        return super.clone()
    }

    fun setUpdateListener(updateListener: ((T) -> Unit)) {
        if (this.updateListener != updateListener) {
            this.updateListener = updateListener
        }
    }

    fun animate(
        vararg targets: T,
        options: (ValueAnimator.() -> Unit)? = null
    ) {
        cancelRunning()
        if (options == null) {
            setObjectValues(*targets)
            AnimationThreadController.postOnAnimatorThread {
                start()
            }
        } else {
            options.invoke(userConfiguredAnimator)
            userConfiguredAnimator.setObjectValues(*targets)
            AnimationThreadController.postOnAnimatorThread {
                userConfiguredAnimator.start()
            }
        }
    }

    fun updateOptions(block: ValueAnimator.() -> Unit) {
        if (isRunning) {
            addListener(object : AnimatorListenerAdapter() {

                override fun onAnimationEnd(animation: Animator, isReverse: Boolean) {
                    super.onAnimationEnd(animation, isReverse)
                    block.invoke(this@GofaAnimator)
                    removeListener(this)
                }
            })
        } else {
            block.invoke(this@GofaAnimator)
        }
    }

    fun cancelRunning() {
        AnimationThreadController.postOnAnimatorThread {
            if (isRunning) {
                cancel()
            }
            if (userConfiguredAnimator.isRunning) {
                userConfiguredAnimator.cancel()
            }
        }
    }

    companion object {
        private val DEFAULT_INTERPOLATOR = LinearInterpolator()
    }
}

object Evaluators {

    /**
     * Type evaluator for Point data
     */
    val POINT = TypeEvaluator<Point> { fraction, startValue, endValue ->
        // If fraction is 1 or 0 then return `endValue`/`startValue` directly without creating new Point
        if (fraction == 1F) {
            endValue
        } else if (fraction == 0F) {
            startValue
        } else {
            val longitudeDelta = endValue.longitude() - startValue.longitude()
            val latitudeDelta = endValue.latitude() - startValue.latitude()
            if (longitudeDelta == 0.0 && latitudeDelta == 0.0) {
                startValue
            } else {
                Point.fromLngLat(
                    startValue.longitude() + fraction * longitudeDelta,
                    startValue.latitude() + fraction * latitudeDelta
                )
            }
        }
    }

    /**
     * Type evaluator for Double data
     */
    val DOUBLE = TypeEvaluator<Double> { fraction, startValue, endValue ->
        // If fraction is 1 or 0 then return `endValue`/`startValue` directly without creating new Double
        if (fraction == 1F) {
            endValue
        } else if (fraction == 0F) {
            startValue
        } else {
            val delta = endValue - startValue
            if (delta == 0.0) {
                startValue
            } else {
                startValue + fraction * delta
            }
        }
    }

    private val zeroEdgeInsets = EdgeInsets(0.0, 0.0, 0.0, 0.0)

    /**
     * Type evaluator for EdgeInsets data
     */
    val EDGE_INSET = TypeEvaluator { fraction, startValue: EdgeInsets?, endValue: EdgeInsets? ->
        // We have seen in the wild that under some conditions we get null values. So let's guard
        // against possible null start/end
        val nonNullEnd = if (endValue != null) {
            endValue
        } else {
            zeroEdgeInsets
        }

        // If fraction is 1 then we can return the `endValue` directly without creating new EdgeInsets
        if (fraction == 1F) {
            return@TypeEvaluator nonNullEnd
        }
        val nonNullStart = if (startValue != null) {
            startValue
        } else {
            zeroEdgeInsets
        }

        // If fraction is 0 then we can return the `startValue` directly without creating new EdgeInsets
        if (fraction == 0F) {
            return@TypeEvaluator nonNullStart
        }

        val topDelta = nonNullEnd.top - nonNullStart.top
        val leftDelta = nonNullEnd.left - nonNullStart.left
        val bottomDelta = nonNullEnd.bottom - nonNullStart.bottom
        val rightDelta = nonNullEnd.right - nonNullStart.right

        // As an optimization we can avoid creating EdgeInsets object if there's no delta for any side
        if (topDelta == 0.0 && leftDelta == 0.0 && bottomDelta == 0.0 && rightDelta == 0.0) {
            nonNullStart
        } else {
            EdgeInsets(
                nonNullStart.top + fraction * topDelta,
                nonNullStart.left + fraction * leftDelta,
                nonNullStart.bottom + fraction * bottomDelta,
                nonNullStart.right + fraction * rightDelta
            )
        }
    }

    /**
     * Type evaluator for ScreenCoordinate data
     */
    val SCREEN_COORDINATE = TypeEvaluator<ScreenCoordinate> { fraction, startValue, endValue ->
        // If fraction is 1 or 0 then return `endValue`/`startValue` directly without creating new ScreenCoordinate
        if (fraction == 1F) {
            endValue
        } else if (fraction == 0F) {
            startValue
        } else {
            val xDelta = endValue.x - startValue.x
            val yDelta = endValue.y - startValue.y
            if (xDelta == 0.0 && yDelta == 0.0) {
                startValue
            } else {
                ScreenCoordinate(startValue.x + fraction * xDelta, startValue.y + fraction * yDelta)
            }
        }
    }
}