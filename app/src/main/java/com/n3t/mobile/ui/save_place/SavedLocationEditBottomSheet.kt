package com.n3t.mobile.ui.save_place

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.n3t.mobile.data.model.place_flow.PlaceDetailUiModel
import com.n3t.mobile.databinding.BottomSheetEditSavedLocationBinding

class SavedLocationEditBottomSheet(
    private val place: PlaceDetailUiModel,
    private val onEdit: (PlaceDetailUiModel) -> Unit,
    private val onDelete: (PlaceDetailUiModel) -> Unit
) : BottomSheetDialogFragment() {

    private var _binding: BottomSheetEditSavedLocationBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetEditSavedLocationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvTitle.text = place.name ?: "Địa điểm"

        binding.btnClose.setOnClickListener { dismiss() }
        binding.btnDone.setOnClickListener { dismiss() }

        binding.btnEditLocation.setOnClickListener {
            onEdit(place)
            dismiss()
        }

        binding.btnDeleteLocation.setOnClickListener {
            onDelete(place)
            dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val TAG = "SavedLocationEditBottomSheet"
    }
}
