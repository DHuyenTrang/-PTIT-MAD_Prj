package com.n3t.mobile.core.widgets

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.DrawableUtils
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.content.ContextCompat
import com.n3t.mobile.R
import com.n3t.mobile.databinding.GofaCellBinding
import com.n3t.mobile.databinding.GofaDropdownAlertBinding

enum class GofaDropdownType {
    SUCESS,
    WARNING,
    ERROR,
    INFO;

    companion object {
        fun from(value: Int): GofaDropdownType {
            return when (value) {
                0 -> SUCESS
                1 -> WARNING
                2 -> ERROR
                3 -> INFO
                else -> SUCESS
            }
        }
    }
}

class GofaDropdownAlert(
    context: Context,
    attrs: AttributeSet
) : LinearLayoutCompat(context, attrs) {
    private var binding: GofaDropdownAlertBinding? = null
    init {
        binding = GofaDropdownAlertBinding.inflate(LayoutInflater.from(context), this, true)
//        val attributes = context.obtainStyledAttributes(attrs, R.styleable.GofaCell)
    }

    fun setProperties(text: String, type: GofaDropdownType) {
        binding?.tvDropdown?.text = text
        when (type) {
            GofaDropdownType.ERROR -> {
                binding?.tvDropdown?.setTextColor(context.resources.getColor(R.color.onErrorContainer))
                binding?.imageDropdown?.setColorFilter(context.resources.getColor(R.color.error))
                binding?.imageDropdown?.setImageResource(R.drawable.ic_seal_warning)
                binding?.root?.setBackgroundColor(context.resources.getColor(R.color.errorContainer))
            }
            GofaDropdownType.SUCESS -> {
                binding?.tvDropdown?.setTextColor(context.resources.getColor(R.color.onSuccessContainer))
                binding?.imageDropdown?.setColorFilter(context.resources.getColor(R.color.success))
                binding?.root?.setBackgroundColor(context.resources.getColor(R.color.successContainer))
                binding?.imageDropdown?.setImageResource(R.drawable.ic_check)
            }
            GofaDropdownType.WARNING -> {
                binding?.tvDropdown?.setTextColor(context.resources.getColor(R.color.onWarningContainer))
                binding?.imageDropdown?.setColorFilter(context.resources.getColor(R.color.warning))
                binding?.root?.setBackgroundColor(context.resources.getColor(R.color.warningContainer))
                binding?.imageDropdown?.setImageResource(R.drawable.ic_warning)
            }
            GofaDropdownType.INFO -> {
                binding?.tvDropdown?.setTextColor(context.resources.getColor(R.color.onInfoContainer))
                binding?.imageDropdown?.setColorFilter(context.resources.getColor(R.color.info))
                binding?.root?.setBackgroundColor(context.resources.getColor(R.color.infoContainer))
                binding?.imageDropdown?.setImageResource(R.drawable.ic_info)
            }
        }
    }
}


