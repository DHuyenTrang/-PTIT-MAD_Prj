package com.n3t.mobile.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.n3t.mobile.databinding.FragmentSearchBinding
import com.n3t.mobile.ui.search.adapter.SearchResultAdapter
import com.n3t.mobile.utils.extensions.setSafeOnClickListener
import com.n3t.mobile.utils.location.LocationPermissionHelper
import com.n3t.mobile.utils.location.MyLocationManager
import com.n3t.mobile.view_model.search.SearchViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.n3t.mobile.data.model.place_flow.CoordinateModel

class SearchFragment : Fragment() {
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SearchViewModel by viewModel()
    private lateinit var searchResultAdapter: SearchResultAdapter
    private var currentLocation: CoordinateModel? = null
    private var locationManager: MyLocationManager? = null
    private var locationPermissionHelper: LocationPermissionHelper? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        locationManager = MyLocationManager(requireContext())
        locationPermissionHelper = LocationPermissionHelper(requireContext())
        setupViews()
        bindViewModel()
        loadCurrentLocation()
    }

    private fun setupViews() {
        searchResultAdapter = SearchResultAdapter { viewModel.onSuggestionSelected(it) }
        binding.rvSearchResult.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = searchResultAdapter
        }
        binding.searchBar.setStartIconOnClickListener {
            requireActivity().finish()
        }
        binding.etSearch.doAfterTextChanged { editable ->
            viewModel.onQueryChanged(editable?.toString().orEmpty(), currentLocation)
        }
        binding.btnTryAgain.setSafeOnClickListener {
            viewModel.onQueryChanged(binding.etSearch.text?.toString().orEmpty(), currentLocation)
        }
        binding.groupSearchRecent.isVisible = true
    }

    private fun bindViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.uiState.collect(::renderState)
                }
                launch {
                    viewModel.openPlaceDetail.collect { event ->
                        findNavController().navigate(
                            SearchFragmentDirections.actionSearchFragmentToSearchPlaceInfoFragment(
                                placeId = event.placeId,
                                sessionToken = event.sessionToken,
                                fallbackTitle = event.fallbackTitle,
                                fallbackSubtitle = event.fallbackSubtitle,
                                distanceMeters = event.distanceMeters,
                            )
                        )
                    }
                }
            }
        }
    }

    private fun renderState(state: com.n3t.mobile.view_model.search.SearchUiState) {
        searchResultAdapter.submitList(state.suggestions)
        binding.pgLoading.isVisible = state.isLoading
        binding.llNoInternet.isVisible = state.isOffline
        val hasQuery = state.query.trim().length >= 2
        val showResults = hasQuery && !state.isLoading && !state.isOffline
        binding.rvSearchResult.isVisible = showResults && state.suggestions.isNotEmpty()
        binding.groupSearchRecent.isVisible = !hasQuery
    }

    private fun loadCurrentLocation() {
        if (locationPermissionHelper?.isLocationPermissionGranted() != true) return
        locationManager?.getLastKnownLocation { location ->
            if (location != null) {
                currentLocation = CoordinateModel(location.latitude, location.longitude)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        locationPermissionHelper = null
        locationManager = null
        _binding = null
    }
}
