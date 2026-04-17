package com.n3t.mobile.core.widgets

import android.app.Activity
import android.app.AlertDialog
import android.content.res.Configuration
import android.view.ViewGroup
import com.n3t.mobile.R
import com.n3t.mobile.databinding.GofaUpdateNotificationDialogBinding
import com.n3t.mobile.utils.extensions.setSafeOnClickListener

class GofaUpdateNofiticationDialog(
    private val activity: Activity
) {
    private lateinit var binding: GofaUpdateNotificationDialogBinding
    private var dialog: AlertDialog? = null
    private var onUpdateNow: (() -> Unit)? = null

    fun show() {
        val builder = AlertDialog.Builder(activity, R.style.Gofa_Dialog)

        val inflater = activity.layoutInflater
        binding = GofaUpdateNotificationDialogBinding.inflate(inflater)

        builder.setView(binding.root)
        builder.setCancelable(false)
        dialog = builder.create()

        binding.btnUpdateNow.setSafeOnClickListener {
            onUpdateNow?.invoke()
        }

        dialog?.show()
        // ? X? lý gi?i h?n chi?u r?ng theo orientation
        val displayMetrics = activity.resources.displayMetrics
        val orientation = activity.resources.configuration.orientation

        val maxWidthPercent = if (orientation == Configuration.ORIENTATION_LANDSCAPE) 0.6 else 0.9
        val maxWidth = (displayMetrics.widthPixels * maxWidthPercent).toInt()

        dialog?.window?.setLayout(
            maxWidth,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    fun hide() {
        dialog?.dismiss()
    }

    fun setButtonOnClick(
        onUpdateNow: () -> Unit
    ) {
        this.onUpdateNow = onUpdateNow
    }

}

