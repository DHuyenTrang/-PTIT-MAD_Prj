package com.n3t.mobile.core.widgets

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.graphics.toColorInt
import com.n3t.mobile.R
import com.n3t.mobile.databinding.GofaAppBarBinding

class GofaAppBar(
    context: Context,
    attrs: AttributeSet? = null,
) : ConstraintLayout(context, attrs) {
    private var _binding: GofaAppBarBinding? = null

    init {
        _binding = GofaAppBarBinding.inflate(LayoutInflater.from(context), this, true)

        val attributes = context.obtainStyledAttributes(attrs, R.styleable.GofaAppBar)
        val title = attributes.getString(R.styleable.GofaAppBar_title)
        val backButton = attributes.getBoolean(R.styleable.GofaAppBar_backButton, true)
        try {
            _binding?.let {
                it.tvTitle.text = title
                it.btnBack.visibility = if (backButton) VISIBLE else GONE

                it.btnBack.setOnClickListener {
                    (context as? Activity)?.onBackPressed()
                }
            }
        } finally {
            attributes.recycle()
        }
    }

    fun setColorTheme() {
        val color = resources.getColor(R.color.onSurface)
        _binding?.tvTitle?.setTextColor(color)
        _binding?.btnBack?.iconTint = ColorStateList.valueOf(color)
    }

    fun setTextColorDefault() {
        val color = resources.getColor(R.color.neutral10)
        _binding?.tvTitle?.setTextColor(color)
        _binding?.btnBack?.iconTint = ColorStateList.valueOf(color)
    }

    fun setTitle(title: String) {
        _binding?.tvTitle?.text = title
    }

    fun setOnClickBackButton(onClick: () -> Unit) {
        _binding?.btnBack?.setOnClickListener {
            onClick()
        }
    }
}

