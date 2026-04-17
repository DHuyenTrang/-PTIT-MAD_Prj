package com.n3t.mobile.core.widgets

import android.app.Dialog
import android.content.Context
import android.widget.Button
import com.n3t.mobile.R

class DialogLocationInformation(
    private val context: Context
) {
    private var dialog: Dialog? = null
    private var callbackShow: (() -> Unit)? = null
    private var callbackDismiss: (() -> Unit)? = null

    fun show() {
        dialog = Dialog(context)
        dialog?.setContentView(R.layout.dialog_location_information)
        val btnClose = dialog?.findViewById<Button>(R.id.btnCloseDialog)
        dialog?.setCancelable(false)
        dialog?.setCanceledOnTouchOutside(false)
        dialog?.window?.setBackgroundDrawableResource(R.color.transparent)
        btnClose?.setOnClickListener {

            dialog?.dismiss()
        }

        dialog?.setOnShowListener {
            callbackShow?.invoke()
        }

        dialog?.setOnDismissListener {
            callbackDismiss?.invoke()
        }

        dialog?.show()
    }

    fun setCallBackShow(callback: (() -> Unit)) {
        callbackShow = callback
    }

    fun setCallbackDismiss(callback: () -> Unit) {
        callbackDismiss = callback
    }

    fun hide() {
        try {
            dialog?.dismiss()
            dialog = null
        } catch (_: Exception) {
        }
    }
}

