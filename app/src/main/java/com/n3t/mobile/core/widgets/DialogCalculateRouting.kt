package com.n3t.mobile.core.widgets

import android.app.Dialog
import android.content.Context
import android.widget.Button
import com.n3t.mobile.R

class DialogCalculateRouting(
    private val context: Context,
    private val listener: DialogCalculateRouting.DialogCalculateRoutingListener,
) {
    private var dialog: Dialog? = null

    fun show() {
        dialog = Dialog(context)
        dialog?.setContentView(R.layout.dialog_calculator_routing)
        val btnClose = dialog?.findViewById<Button>(R.id.btnCloseDialog)
        dialog?.setCancelable(false)
        dialog?.setCanceledOnTouchOutside(false)
        dialog?.window?.setBackgroundDrawableResource(R.color.transparent)
        btnClose?.setOnClickListener {
            dialog?.dismiss()
            listener.onCanceled()
        }
        dialog?.show()
    }

    fun hide() {
        try {
            dialog?.dismiss()
            dialog = null
        }
        catch (_: Exception) {
        }
    }

    interface DialogCalculateRoutingListener {
        fun onCanceled()
    }
}

