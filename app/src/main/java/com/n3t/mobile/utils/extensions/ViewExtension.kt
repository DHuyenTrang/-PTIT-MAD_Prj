package com.n3t.mobile.utils.extensions

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.Canvas
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.Px
import androidx.core.view.updateLayoutParams
import com.n3t.mobile.R
import com.google.android.material.button.MaterialButton
import androidx.core.view.isVisible
import androidx.core.view.isGone

fun View.toggleVisibleWithScaleAnimation() {
    val scaleIn = AnimationUtils.loadAnimation(context, R.anim.scale_in)
    val scaleOut = AnimationUtils.loadAnimation(context, R.anim.scale_out)

    val visible = this.visibility == View.VISIBLE
    this.startAnimation(if (visible) scaleOut else scaleIn)
    this.visibility = if (visible) {
        View.GONE
    } else {
        View.VISIBLE
    }
}

private object SymbolGenerator {
    @SuppressLint("ResourceAsColor")
    fun generate(view: View): Bitmap {
        val measureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        view.measure(measureSpec, measureSpec)

        val measuredWidth = view.measuredWidth
        val measuredHeight = view.measuredHeight

        view.layout(0, 0, measuredWidth, measuredHeight)
        val bitmap = Bitmap.createBitmap(measuredWidth, measuredHeight, Bitmap.Config.ARGB_8888)
        bitmap.eraseColor(android.R.color.transparent)
        val canvas = Canvas(bitmap)
        view.draw(canvas)
        return bitmap
    }
}

fun View.getBitmap(): Bitmap = SymbolGenerator.generate(this)

fun View.visibleWithScaleAnimation(visible: Boolean = true) {
    val scaleIn = AnimationUtils.loadAnimation(context, R.anim.scale_in)
    val scaleOut = AnimationUtils.loadAnimation(context, R.anim.scale_out)
    if ((this.isVisible && visible) || (this.isGone && !visible)) return
    this.startAnimation(if (visible) scaleIn else scaleOut)
    this.visibility = if (visible) {
        View.VISIBLE
    } else {
        View.GONE
    }
}

fun View.toggleVisibleWithFadeAnimation() {
    val faceIn = AnimationUtils.loadAnimation(context, R.anim.fade_in_view)
    val faceOut = AnimationUtils.loadAnimation(context, R.anim.fade_out_view)

    val visible = this.visibility == View.VISIBLE
    this.startAnimation(if (visible) faceOut else faceIn)
    this.visibility = if (visible) {
        View.GONE
    } else {
        View.VISIBLE
    }
}

fun View.visibleWithFadeAnimation(visible: Boolean = true) {
    if (visible && this.visibility == View.VISIBLE) return
    if (!visible && this.visibility != View.VISIBLE) return

    val faceIn = AnimationUtils.loadAnimation(context, R.anim.fade_in_view)
    val faceOut = AnimationUtils.loadAnimation(context, R.anim.fade_out_view)

    this.startAnimation(if (visible) faceIn else faceOut)
    this.visibility = if (visible) {
        View.VISIBLE
    } else {
        View.GONE
    }
}

fun View.updateMargins(
    @Px left: Int? = null,
    @Px top: Int? = null,
    @Px right: Int? = null,
    @Px bottom: Int? = null
) {
    updateLayoutParams<ViewGroup.MarginLayoutParams> {
        setMargins(
            left ?: leftMargin,
            top ?: topMargin,
            right ?: rightMargin,
            bottom ?: bottomMargin
        )
    }
}

fun Context.getThemeColor(attrRes: Int): Int {
    val typedValue = TypedValue()
    theme.resolveAttribute(attrRes, typedValue, true)
    return typedValue.data
}

fun View.updateBackground(resId: Int) {
    this.setBackgroundResource(0) // Xóa cache cu
    this.setBackgroundResource(resId) // Gán l?i resource (s? an theo theme m?i)
}

fun ImageView.reloadImage(resId: Int) {
    this.setImageResource(0)
    this.setImageResource(resId)
}

fun TextView.updateTextColorRes(colorResId: Int) {
    val color = androidx.core.content.ContextCompat.getColor(context, colorResId)
    this.setTextColor(color)
}

fun View.updateBackgroundColor(attrRes: Int) {
    setBackgroundColor(context.getThemeColor(attrRes))
}

fun MaterialButton.updateIconTint(attrRes: Int) {
    val color = context.getThemeColor(attrRes)
    this.iconTint = ColorStateList.valueOf(color)
}


