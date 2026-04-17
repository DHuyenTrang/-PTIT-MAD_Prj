package com.n3t.mobile.ui.map.widget

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomSheetGoGptOnboard : BottomSheetDialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return View(inflater.context)
    }

    companion object {
        fun newInstance() = BottomSheetGoGptOnboard()
    }
}
