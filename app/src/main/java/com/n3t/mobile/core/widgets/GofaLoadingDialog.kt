package com.n3t.mobile.core.widgets

import android.app.Activity
import android.app.AlertDialog
import com.n3t.mobile.R

class GofaLoadingDialog(
    private val activity: Activity
) {
    private var dialog: AlertDialog? = null

    fun show() {
        val builder = AlertDialog.Builder(activity, R.style.Gofa_Dialog)

        val inflater = activity.layoutInflater
        val view = inflater.inflate(R.layout.gofa_loading, null)

        builder.setView(view)
        builder.setCancelable(false)
        dialog = builder.create()

        dialog?.show()
    }

    fun hide() {
        dialog?.dismiss()
    }
}

