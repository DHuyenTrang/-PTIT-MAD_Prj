package com.n3t.mobile.core.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.appcompat.widget.LinearLayoutCompat
import com.n3t.mobile.R
import com.n3t.mobile.databinding.GofaCellSwitchBinding
import com.n3t.mobile.utils.extensions.dpToPx
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.ShapeAppearanceModel

class GofaCellSwitch(
    context: Context,
    attrs: AttributeSet
) : LinearLayoutCompat(context, attrs) {
    private var binding: GofaCellSwitchBinding? = null

    init {
        binding = GofaCellSwitchBinding.inflate(LayoutInflater.from(context), this, true)
        val attributes = context.obtainStyledAttributes(attrs, R.styleable.GofaCellSwitch)

        try {
            val borderType = attributes.getInt(R.styleable.GofaCellSwitch_borderType, 0)
            val icon = attributes.getDrawable(R.styleable.GofaCellSwitch_icon)
            val title = attributes.getString(R.styleable.GofaCellSwitch_title)
            val switchEnable = attributes.getBoolean(R.styleable.GofaCellSwitch_switchEnable, false)
            val isColorIcon = attributes.getBoolean(R.styleable.GofaCellSwitch_isSwitchColorIcon, true)
            val bgTint  = attributes.getColorStateList(R.styleable.GofaCellSwitch_cellBackground)
            val borderRadius = resources.getDimension(R.dimen.card_border_radius)

            // process border radius
            val shapeAppearance = ShapeAppearanceModel()
                .toBuilder()
            shapeAppearance.setBottomRightCorner(
                CornerFamily.ROUNDED
            ) { if (borderType == 0 || borderType == 2) borderRadius else 0f }
            shapeAppearance.setBottomLeftCorner(
                CornerFamily.ROUNDED
            ) { if (borderType == 0 || borderType == 2) borderRadius else 0f }
            shapeAppearance.setTopLeftCorner(
                CornerFamily.ROUNDED
            ) { if (borderType == 0 || borderType == 1) borderRadius else 0f }
            shapeAppearance.setTopRightCorner(
                CornerFamily.ROUNDED
            ) { if (borderType == 0 || borderType == 1) borderRadius else 0f }

            binding?.root?.shapeAppearanceModel = shapeAppearance.build()

            binding?.let {
                it.divider.visibility = if (borderType == 1 || borderType == 3) VISIBLE else GONE

                it.ivIcon.visibility = if (icon != null) VISIBLE else GONE
                if (icon != null) it.ivIcon.setImageDrawable(icon)
                if (isColorIcon) {
                    it.ivIcon.setColorFilter(context.getColor(R.color.onSurfaceVariant))
                }
                it.tvTitle.text = title
                if (icon == null) {
                    val titleLayoutParams = it.tvTitle.layoutParams as MarginLayoutParams
                    titleLayoutParams.setMargins(0, 16.dpToPx(), 0, 16.dpToPx())
                    it.tvTitle.layoutParams = titleLayoutParams
                }
                bgTint?.let { cellBackground ->
                    binding?.root?.setCardBackgroundColor(cellBackground)
                }
            }
        } finally {
            attributes.recycle()
        }
    }

    fun setOnSwitchChangeListener(listener: (Boolean) -> Unit) {
        binding?.switchButton?.setOnClickListener {
            listener.invoke(binding?.switchButton?.isChecked ?: false)
        }
    }

    fun setTitle(title: String) {
        binding?.tvTitle?.text = title
    }

    fun setSwitchState(isChecked: Boolean) {
        binding?.switchButton?.isChecked = isChecked
    }
}

