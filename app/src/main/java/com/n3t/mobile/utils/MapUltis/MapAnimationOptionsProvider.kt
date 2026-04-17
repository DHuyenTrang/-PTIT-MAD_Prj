package com.n3t.mobile.utils.MapUltis

import android.animation.TimeInterpolator
import com.mapbox.maps.plugin.animation.MapAnimationOptions

data class MapAnimationOptionsProvider(
    /**
     * Owner or creator this animation.
     */
    var owner: String? = null,

    /**
     * The duration of the animation in milliseconds.
     */
    var duration: Long? = null,

    /**
     * The amount of time, in milliseconds, to delay starting the animation after animation start.
     */
    var startDelay: Long? = null,

    /**
     * The animation interpolator.
     */
    var interpolator: TimeInterpolator? = null
) {


    fun toMapbox(): MapAnimationOptions {
        val mapAnimationOptions = MapAnimationOptions.Builder()
        owner?.let { mapAnimationOptions.owner(it) }
        duration?.let { mapAnimationOptions.duration(it) }
        startDelay?.let { mapAnimationOptions.startDelay(it) }
        interpolator?.let { mapAnimationOptions.interpolator(it) }
        return mapAnimationOptions.build()
    }


    class Builder {
        private var owner: String? = null
        private var duration: Long? = null
        private var startDelay: Long? = null
        private var interpolator: TimeInterpolator? = null

        /**
         * Owner or creator this animation.
         */
        fun owner(owner: String) = apply {
            this.owner = owner
        }

        /**
         * The duration of the animation in milliseconds.
         */
        fun duration(duration: Long) = apply { this.duration = duration }

        /**
         * Set the start delay of the animation in milliseconds.
         */
        fun startDelay(startDelay: Long) = apply { this.startDelay = startDelay }

        /**
         * Set the animation interpolator.
         */
        fun interpolator(interpolator: TimeInterpolator) = apply { this.interpolator = interpolator }


        /**
         * Xây dựng và trả về một đối tượng CameraProvider hoàn chỉnh.
         */
        fun build() = MapAnimationOptionsProvider(
            owner = owner,
            duration = duration,
            startDelay = startDelay,
            interpolator = interpolator
        )
    }

    /**
     * Static methods.
     */

    companion object {
        /**
         * Builder DSL function to create [MapAnimationOptions] object.
         */

        inline fun mapAnimationOptionsProvider(block: Builder.() -> Unit): MapAnimationOptionsProvider =
            Builder().apply(block).build()
    }
}


