package com.n3t.mobile.ui.save_place

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.n3t.mobile.R
import com.n3t.mobile.data.model.place_flow.CoordinateModel
import com.n3t.mobile.data.model.place_flow.PlaceDetailUiModel
import com.n3t.mobile.data.model.place_flow.PlaceSuggestionUiModel
import com.n3t.mobile.databinding.FragmentSearchToSavedLocationBinding
import com.n3t.mobile.ui.search.adapter.SearchResultAdapter
import com.n3t.mobile.utils.location.LocationPermissionHelper
import com.n3t.mobile.utils.location.MyLocationManager
import com.n3t.mobile.view_model.save_place.PlaceCategory
import com.n3t.mobile.view_model.save_place.SavePlaceViewModel
import com.n3t.mobile.view_model.search.DetailPlaceViewModel
import com.n3t.mobile.view_model.search.SearchViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchToSavedLocationFragment : Fragment() {

    private var _binding: FragmentSearchToSavedLocationBinding? = null
    private val binding get() = _binding!!

    private val searchViewModel: SearchViewModel by viewModel()
    private val detailViewModel: DetailPlaceViewModel by viewModel()
    private val saveViewModel: SavePlaceViewModel by activityViewModel()

    private lateinit var searchResultAdapter: SearchResultAdapter
    private var currentLocation: CoordinateModel? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchToSavedLocationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadCurrentLocation()
        setupViews()
        observeViewModels()
    }

    private fun setupViews() {
        binding.searchBar.setStartIconOnClickListener {
            parentFragmentManager.popBackStack()
        }

        searchResultAdapter = SearchResultAdapter { suggestion ->
            onSuggestionClicked(suggestion)
        }

        binding.rvSearchResult.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = searchResultAdapter
        }

        binding.etSearch.doAfterTextChanged { text ->
            searchViewModel.onQueryChanged(text?.toString().orEmpty(), currentLocation)
        }
    }

    private fun observeViewModels() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                // Search State
                launch {
                    searchViewModel.uiState.collect { state ->
                        searchResultAdapter.submitList(state.suggestions)
                        binding.pgLoading.isVisible = state.isLoading
                    }
                }

                // Detail State for Saving
                launch {
                    detailViewModel.uiState.collect { state ->
                        state.placeDetail?.let { detail ->
                            // When detail is loaded, it means the user picked a suggestion
                            // But we need to know WHICH suggestion and avoid multiple triggers
                            // Actually, I'll trigger category selection AFTER detail is loaded
                            // To keep it simple, I'll handle the selection flow manually.
                        }
                    }
                }
            }
        }
    }

    private fun onSuggestionClicked(suggestion: PlaceSuggestionUiModel) {
        // First, fetch detail
        binding.pgLoading.isVisible = true
        detailViewModel.loadPlaceDetail(
            placeId = suggestion.placeId,
            sessionToken = "save_session",
            fallbackTitle = suggestion.title,
            fallbackSubtitle = suggestion.subtitle,
            currentLocation = currentLocation
        )

        // Observe detail then show dialog
        viewLifecycleOwner.lifecycleScope.launch {
            detailViewModel.uiState.collectLatest { state ->
                if (!state.isLoading && state.placeDetail != null) {
                    binding.pgLoading.isVisible = false
                    showCategorySelection(state.placeDetail)
                    // Reset detail to avoid re-triggering
                    // viewModel.resetDetail() // If implemented
                }
            }
        }
    }

    private fun showCategorySelection(detail: PlaceDetailUiModel) {
        val categories = arrayOf(
            getString(R.string.house),
            getString(R.string.co_quan),
            getString(R.string.favorites)
        )

        AlertDialog.Builder(requireContext())
            .setTitle(R.string.choose_category)
            .setItems(categories) { _, which ->
                val category = when (which) {
                    0 -> PlaceCategory.HOME
                    1 -> PlaceCategory.OFFICE
                    else -> PlaceCategory.FAVORITE
                }
                showConfirmationDialog(detail, category)
            }
            .show()
    }

    private fun showConfirmationDialog(detail: PlaceDetailUiModel, category: PlaceCategory) {
        val categoryName = when (category) {
            PlaceCategory.HOME -> getString(R.string.house)
            PlaceCategory.OFFICE -> getString(R.string.co_quan)
            PlaceCategory.FAVORITE -> getString(R.string.favorites)
        }

        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_confirmation_save, null)
        val dialog = AlertDialog.Builder(requireContext(), R.style.Gofa_Dialog_Transparent)
            .setView(dialogView)
            .create()

        val tvMessage = dialogView.findViewById<android.widget.TextView>(R.id.tvMessage)
        val btnCancel = dialogView.findViewById<android.widget.Button>(R.id.btnCancel)
        val btnConfirm = dialogView.findViewById<android.widget.Button>(R.id.btnConfirm)

        tvMessage.text = getString(R.string.msg_confirm_save_place, categoryName)

        btnCancel.setOnClickListener { dialog.dismiss() }
        btnConfirm.setOnClickListener {
            saveViewModel.savePlace(detail, category)
            Toast.makeText(requireContext(), R.string.save_success, Toast.LENGTH_SHORT).show()
            dialog.dismiss()
            parentFragmentManager.popBackStack()
        }

        dialog.show()
    }

    private fun loadCurrentLocation() {
        val permissionHelper = LocationPermissionHelper(requireContext())
        if (permissionHelper.isLocationPermissionGranted()) {
            MyLocationManager(requireContext()).getLastKnownLocation { location ->
                currentLocation = location?.let { CoordinateModel(it.latitude, it.longitude) }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
