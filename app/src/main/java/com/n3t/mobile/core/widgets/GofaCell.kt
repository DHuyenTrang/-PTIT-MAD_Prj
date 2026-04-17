package com.n3t.mobile.core.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.widget.ImageViewCompat
import com.n3t.mobile.R
import com.n3t.mobile.databinding.GofaCellBinding
import com.n3t.mobile.utils.extensions.dpToPx
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.ShapeAppearanceModel

enum class GofaCellBorderType {
    All,
    TOP,
    BOTTOM,
    NONE;

    companion object {
        fun from(value: Int): GofaCellBorderType {
            return when (value) {
                0 -> All
                1 -> TOP
                2 -> BOTTOM
                3 -> NONE
                else -> All
            }
        }
    }
}

fun getBorderType(position: Int, itemsSize: Int): GofaCellBorderType {
    if (itemsSize < 2) return GofaCellBorderType.All

    return when (position) {
        0 -> GofaCellBorderType.TOP
        itemsSize - 1 -> GofaCellBorderType.BOTTOM
        else -> GofaCellBorderType.NONE
    }
}

class GofaCell(
    context: Context,
    attrs: AttributeSet
) : LinearLayoutCompat(context, attrs) {
    private var binding: GofaCellBinding? = null

    init {
        binding = GofaCellBinding.inflate(LayoutInflater.from(context), this, true)
        val attributes = context.obtainStyledAttributes(attrs, R.styleable.GofaCell)

        try {
            val borderType = attributes.getInt(R.styleable.GofaCell_borderType, 0)
            val icon = attributes.getDrawable(R.styleable.GofaCell_icon)
            val enableTint = attributes.getBoolean(R.styleable.GofaCell_enableTint, false)
            val title = attributes.getString(R.styleable.GofaCell_title)
            val subtitle = attributes.getString(R.styleable.GofaCell_subtitle)
            val isActive = attributes.getBoolean(R.styleable.GofaCell_isActiveInCell, false)
            val value = attributes.getString(R.styleable.GofaCell_value)
            val arrowIcon = attributes.getBoolean(R.styleable.GofaCell_arrowIcon, true)
            val arrowIconDrawable = attributes.getDrawable(R.styleable.GofaCell_arrowIconDrawable)

            setBorderType(GofaCellBorderType.from(borderType))

            binding?.let {
                it.ivIcon.visibility = if (icon != null) VISIBLE else GONE
                if (icon != null) it.ivIcon.setImageDrawable(icon)

                it.tvTitle.text = title
                if (icon == null) {
                    val llContentLayoutParams = it.llContent.layoutParams as MarginLayoutParams
                    llContentLayoutParams.setMargins(0, 16.dpToPx(), 0, 16.dpToPx())
                    it.llContent.layoutParams = llContentLayoutParams
                }
                if (!enableTint) {
                    ImageViewCompat.setImageTintList(it.ivIcon, ContextCompat.getColorStateList(context, R.color.onSurfaceVariant))
                }
                if (isActive) {
                    it.tvTitle.setTextColor(ResourcesCompat.getColor(resources, R.color.primary, null))
                    it.tvSubtitle.setTextColor(ResourcesCompat.getColor(resources, R.color.primary, null))
                    it.tvValue.setTextColor(ResourcesCompat.getColor(resources, R.color.primary, null))

                    it.tvTitle.typeface = ResourcesCompat.getFont(context, R.font.sf_pro_text_bold)
                    it.tvSubtitle.typeface = ResourcesCompat.getFont(context, R.font.sf_pro_text_regular)
                    it.tvValue.typeface = ResourcesCompat.getFont(context, R.font.sf_pro_text_regular)
                }

                it.tvSubtitle.text = subtitle
                it.tvSubtitle.visibility = if (subtitle != null) VISIBLE else GONE

                it.tvValue.text = value
                it.tvValue.visibility = if (value != null) VISIBLE else GONE
                it.ivArrow.visibility = if (arrowIcon) VISIBLE else GONE

                if(arrowIconDrawable != null) {
                    it.ivArrow.setImageDrawable(arrowIconDrawable)
                }
            }
        } finally {
            attributes.recycle()
        }
    }

    override fun setOnClickListener(listener: OnClickListener?) {
        binding?.root?.setOnClickListener(listener)
    }

    fun setValue(value: String) {
        binding?.tvValue?.text = value
    }

    fun setTitle(title: String) {
        binding?.tvTitle?.text = title
    }

    fun setSubtitle(subtitle: String) {
        binding?.tvSubtitle?.text = subtitle
    }

    fun setIcon(icon: Int) {
        binding?.ivIcon?.setImageResource(icon)
    }

    fun setArrowIcon(icon: Int) {
        binding?.ivArrow?.setImageResource(icon)
    }

    fun setArrowIconEnable(enable: Boolean) {
        binding?.ivArrow?.visibility = if (enable) VISIBLE else GONE
    }

    fun setBorderType(borderType: GofaCellBorderType) {
        val borderRadius = resources.getDimension(R.dimen.card_border_radius)

        // process border radius
        val shapeAppearance = ShapeAppearanceModel()
            .toBuilder()
        shapeAppearance.setBottomRightCorner(
            CornerFamily.ROUNDED
        ) { if (borderType == GofaCellBorderType.All || borderType == GofaCellBorderType.BOTTOM) borderRadius else 0f }
        shapeAppearance.setBottomLeftCorner(
            CornerFamily.ROUNDED
        ) { if (borderType == GofaCellBorderType.All || borderType == GofaCellBorderType.BOTTOM) borderRadius else 0f }
        shapeAppearance.setTopLeftCorner(
            CornerFamily.ROUNDED
        ) { if (borderType == GofaCellBorderType.All || borderType == GofaCellBorderType.TOP) borderRadius else 0f }
        shapeAppearance.setTopRightCorner(
            CornerFamily.ROUNDED
        ) { if (borderType == GofaCellBorderType.All || borderType == GofaCellBorderType.TOP) borderRadius else 0f }

        binding?.root?.shapeAppearanceModel = shapeAppearance.build()

        binding?.divider?.visibility = if (borderType == GofaCellBorderType.TOP || borderType == GofaCellBorderType.NONE) VISIBLE else GONE
    }

    fun setBackGroundColor(color: Int) {
        binding?.root?.setCardBackgroundColor(resources.getColor(color))
    }
}

