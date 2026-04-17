package com.n3t.mobile.ui.map.widget

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.n3t.mobile.R
import com.n3t.mobile.data.model.common.MapStyleURL
import com.n3t.mobile.data.model.common.getTitleResource
import com.n3t.mobile.databinding.BottomSheetChooseMapTypeBinding
import com.n3t.mobile.ui.map.adapter.MapOption
import com.n3t.mobile.ui.map.adapter.MapTypeAdapter

/**
 * Bottom sheet fragment for choosing map type (Default / Satellite).
 */
class ChooseMapTypeFragment : BottomSheetDialogFragment() {

    private var _binding: BottomSheetChooseMapTypeBinding? = null
    private val binding get() = _binding!!

    /** Callback when a map style is selected. */
    var onMapStyleSelected: ((MapStyleURL) -> Unit)? = null

    private var currentStyle: MapStyleURL = MapStyleURL.DEFAULT

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetChooseMapTypeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnClose.setOnClickListener { dismiss() }

        setupMapTypeList()
    }

    private fun setupMapTypeList() {
        val mapTypeAdapter = MapTypeAdapter { selectedOption ->
            val style = when (selectedOption.title) {
                getString(R.string.map_satellite) -> MapStyleURL.SATELLITE
                else -> MapStyleURL.DEFAULT
            }
            currentStyle = style
            onMapStyleSelected?.invoke(style)
            updateMapTypeSelection()
            dismiss()
        }

        binding.listMapType.adapter = mapTypeAdapter

        val options = listOf(
            MapOption(
                icon = R.drawable.type_map4d,
                title = getString(MapStyleURL.DEFAULT.getTitleResource()),
                isSelected = currentStyle == MapStyleURL.DEFAULT
            ),
            MapOption(
                icon = R.drawable.type_map4d,
                title = getString(MapStyleURL.SATELLITE.getTitleResource()),
                isSelected = currentStyle == MapStyleURL.SATELLITE
            ),
        )
        mapTypeAdapter.submitList(options)
    }

    private fun updateMapTypeSelection() {
        // Refresh adapter with updated selection state
        setupMapTypeList()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
