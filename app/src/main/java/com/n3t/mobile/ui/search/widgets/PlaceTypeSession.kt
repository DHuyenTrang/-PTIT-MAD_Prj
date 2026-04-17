package com.n3t.mobile.ui.search.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.appcompat.widget.LinearLayoutCompat
import com.n3t.mobile.databinding.WidgetSessionPlaceTypeBinding

class PlaceTypeSession(
    context: Context,
    attrs: AttributeSet
) : LinearLayoutCompat(context, attrs) {
    var binding: WidgetSessionPlaceTypeBinding

    init {
        binding = WidgetSessionPlaceTypeBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun setupData() {
        listenAppEvents()
    }

    private fun listenAppEvents() {
        // Stub for n3t migration
    }

    fun refreshTheme() {
        this.removeAllViews()
        binding = WidgetSessionPlaceTypeBinding.inflate(LayoutInflater.from(context), this, true)
        listenAppEvents()
    }
}
