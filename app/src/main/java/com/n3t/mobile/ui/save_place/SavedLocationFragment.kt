package com.n3t.mobile.ui.save_place

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.n3t.mobile.R
import com.n3t.mobile.data.model.place_flow.CoordinateModel
import com.n3t.mobile.data.model.place_flow.PlaceDetailUiModel
import com.n3t.mobile.databinding.FragmentSavedLocationBinding
import com.n3t.mobile.ui.routing.RoutingActivity
import com.n3t.mobile.ui.save_place.adapter.SavedLocationAdapter
import com.n3t.mobile.utils.location.LocationPermissionHelper
import com.n3t.mobile.utils.location.MyLocationManager
import com.n3t.mobile.view_model.save_place.SavePlaceViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class SavedLocationFragment : Fragment() {

    private var _binding: FragmentSavedLocationBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SavePlaceViewModel by activityViewModel()
    private lateinit var adapter: SavedLocationAdapter

    private var isEditMode = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSavedLocationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        observeViewModel()
    }

    private fun setupViews() {
        adapter = SavedLocationAdapter(
            onItemClick = { handleItemClick(it) }
        )

        binding.rycSaveLocation.layoutManager = LinearLayoutManager(requireContext())
        binding.rycSaveLocation.adapter = adapter

        binding.ivBack.setOnClickListener {
            requireActivity().onBackPressed()
        }

        binding.ivEdit.setOnClickListener {
            toggleEditMode(true)
        }

        binding.btnDone.setOnClickListener {
            toggleEditMode(false)
        }

        // Add Location button mapping
        binding.tbAddLocation.setOnClickListener {
            (requireActivity() as? SavePlaceActivity)?.openSearchToSave()
        }
        binding.btnAddLocation.setOnClickListener {
            (requireActivity() as? SavePlaceActivity)?.openSearchToSave()
        }

        // Initialize Home/Office static UI actions
        initStaticItems()
    }

    private fun initStaticItems() {
        binding.tbEditHome.setOnClickListener {
            handleItemClick(viewModel.uiState.value.homePlace)
        }
        binding.btnEditHome.setOnClickListener {
            handleItemClick(viewModel.uiState.value.homePlace)
        }

        binding.tbEditWorkPlace.setOnClickListener {
            handleItemClick(viewModel.uiState.value.officePlace)
        }
        binding.btnEditWorkPlace.setOnClickListener {
            handleItemClick(viewModel.uiState.value.officePlace)
        }
    }

    private fun toggleEditMode(enable: Boolean) {
        isEditMode = enable
        binding.ivEdit.isVisible = !enable
        binding.btnDone.isVisible = enable
        adapter.isEditMode = enable
    }

    private fun handleItemClick(place: PlaceDetailUiModel?, category: String? = null) {
        if (isEditMode) {
            if (place != null) {
                showEditBottomSheet(place)
            } else {
                (requireActivity() as? SavePlaceActivity)?.openSearchToSave()
            }
        } else {
            if (place != null) {
                navigateToRouting(place)
            } else {
                (requireActivity() as? SavePlaceActivity)?.openSearchToSave()
            }
        }
    }

    private fun navigateToRouting(destination: PlaceDetailUiModel) {
        val permissionHelper = LocationPermissionHelper(requireContext())
        if (permissionHelper.isLocationPermissionGranted()) {
            MyLocationManager(requireContext()).getLastKnownLocation { location ->
                if (location != null) {
                    val origin = CoordinateModel(location.latitude, location.longitude)
                    requireContext().startActivity(
                        RoutingActivity.newIntent(
                            context = requireContext(),
                            origin = origin,
                            destination = destination.coordinate,
                            destinationName = destination.name,
                            destinationAddress = destination.formattedAddress
                        )
                    )
                } else {
                    Toast.makeText(requireContext(), R.string.msg_wait_gps, Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            Toast.makeText(requireContext(), "Vui lòng cấp quyền vị trí", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showEditBottomSheet(place: PlaceDetailUiModel) {
        val bottomSheet = SavedLocationEditBottomSheet(
            place = place,
            onEdit = { 
                (requireActivity() as? SavePlaceActivity)?.openSearchToSave()
            },
            onDelete = { 
                viewModel.deletePlace(it)
                Toast.makeText(requireContext(), R.string.delete_success, Toast.LENGTH_SHORT).show()
            }
        )
        bottomSheet.show(childFragmentManager, SavedLocationEditBottomSheet.TAG)
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    renderHome(state.homePlace)
                    renderOffice(state.officePlace)
                    renderFavorites(state.savedPlaces)
                }
            }
        }
    }

    private fun renderHome(place: PlaceDetailUiModel?) {
        binding.tvHomeName.text = place?.formattedAddress ?: getString(R.string.set_up_place)
        val colorObj = if (place != null) R.color.onSurfaceVariant else R.color.primary
        binding.tvHomeName.setTextColor(resources.getColor(colorObj, requireContext().theme))
    }

    private fun renderOffice(place: PlaceDetailUiModel?) {
        binding.tvWorkPlaceName.text = place?.formattedAddress ?: getString(R.string.set_up_place)
        val colorObj = if (place != null) R.color.onSurfaceVariant else R.color.primary
        binding.tvWorkPlaceName.setTextColor(resources.getColor(colorObj, requireContext().theme))
    }

    private fun renderFavorites(places: List<PlaceDetailUiModel>) {
        adapter.submitList(places)
        binding.rycSaveLocation.isVisible = places.isNotEmpty()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
