package com.n3t.mobile.core.widgets

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getColor
import androidx.core.content.res.ResourcesCompat
import com.n3t.mobile.R
import com.n3t.mobile.utils.extensions.dpToPx

class CircleIconButton(
    context: Context,
    attrs: AttributeSet
): LinearLayoutCompat(context, attrs) {
    private var icon: Drawable? = null
    private var iconSize: Int = 24.dpToPx()
    private var buttonSize: Int = 56.dpToPx()
    private var isActive: Boolean = false
    private var isColorIcon: Boolean = false

    init {
        val attributes = context.obtainStyledAttributes(attrs, R.styleable.CircleIconButton)

        try {
            icon = attributes.getDrawable(R.styleable.CircleIconButton_icon)
            iconSize = attributes.getDimensionPixelSize(R.styleable.CircleIconButton_iconSize, 24.dpToPx())
            buttonSize = attributes.getDimensionPixelSize(R.styleable.CircleIconButton_size, 56.dpToPx())
            isActive = attributes.getBoolean(R.styleable.CircleIconButton_isActive, false)
            isColorIcon = attributes.getBoolean(R.styleable.CircleIconButton_isColorIcon, false)
            updateView()
        } finally {
            attributes.recycle()
        }
    }

    private fun updateView() {
        val linearLayout = LinearLayout(context)
        linearLayout.orientation = LinearLayout.VERTICAL
        linearLayout.layoutParams = LayoutParams(buttonSize, buttonSize)
        linearLayout.gravity = android.view.Gravity.CENTER

        val iconView = ImageView(context)
        iconView.setImageDrawable(icon)
        iconView.layoutParams = LayoutParams(iconSize, iconSize)
        if (isActive) {
            if (!isColorIcon) {
                iconView.setColorFilter(getColor(context, R.color.white))
            }
            linearLayout.background = ResourcesCompat.getDrawable(resources, R.drawable.bg_circle_button_icon_active, null)
        } else {
            if (!isColorIcon) {
                iconView.setColorFilter(getColor(context, R.color.primary))
            }
            else iconView.setColorFilter(getColor(context, R.color.onSurfaceVariant))
            linearLayout.background = ResourcesCompat.getDrawable(resources, R.drawable.bg_circle_button_icon, null)
        }

        linearLayout.addView(iconView)
        this.addView(linearLayout)
    }

    fun setActive(isActive: Boolean) {
        this.isActive = isActive
        this.removeAllViews()
        updateView()
    }

    fun setIcon(icon: Int) {
        this.icon = ContextCompat.getDrawable(context, icon)
        this.removeAllViews()
        updateView()
    }

    fun refreshView() {
        this.removeAllViews()
        updateView()
        invalidate()
    }
}

