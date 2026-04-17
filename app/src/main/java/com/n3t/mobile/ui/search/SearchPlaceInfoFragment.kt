package com.n3t.mobile.ui.search

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.plugin.annotation.annotations
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationManager
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.createPointAnnotationManager
import com.n3t.mobile.data.model.common.MapStyleURL
import com.n3t.mobile.data.model.common.getStyleUrl
import com.n3t.mobile.data.model.place_flow.CoordinateModel
import com.n3t.mobile.databinding.FragmentSearchPlaceInfoBinding
import com.n3t.mobile.ui.routing.RoutingActivity
import com.n3t.mobile.utils.RouteFormatUtils
import com.n3t.mobile.utils.getBitmapFromDrawable
import com.n3t.mobile.utils.location.LocationPermissionHelper
import com.n3t.mobile.utils.location.MyLocationManager
import com.n3t.mobile.view_model.search.DetailPlaceViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchPlaceInfoFragment : Fragment() {
    private var _binding: FragmentSearchPlaceInfoBinding? = null
    private val binding get() = _binding!!
    private val args: SearchPlaceInfoFragmentArgs by navArgs()
    private val viewModel: DetailPlaceViewModel by viewModel()
    private var pointAnnotationManager: PointAnnotationManager? = null
    private var currentLocation: CoordinateModel? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchPlaceInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupMap()
        setupActions()
        bindViewModel()
        loadCurrentLocationAndDetail()
    }

    private fun setupMap() {
        binding.mapView.mapboxMap.loadStyle(MapStyleURL.DEFAULT.getStyleUrl()) {
            pointAnnotationManager = binding.mapView.annotations.createPointAnnotationManager()
        }
    }

    private fun setupActions() {
        binding.btnBack.setOnClickListener { findNavController().navigateUp() }
        binding.btnClose.setOnClickListener { requireActivity().finish() }
        binding.btnViewRoute.setOnClickListener {
            val detail = viewModel.uiState.value.placeDetail ?: return@setOnClickListener
            val origin = currentLocation ?: return@setOnClickListener
            startActivity(
                RoutingActivity.newIntent(
                    context = requireContext(),
                    origin = origin,
                    destination = detail.coordinate,
                    destinationName = detail.name,
                    destinationAddress = detail.formattedAddress,
                )
            )
        }
    }

    private fun bindViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    binding.btnViewRoute.isEnabled = !state.isLoading && state.placeDetail != null && currentLocation != null
                    state.placeDetail?.let { detail ->
                        binding.txtName.text = detail.name
                        binding.txtAddress.text = detail.formattedAddress
                        binding.txtDistance.isVisible = detail.distanceMeters != null
                        binding.txtDistance.text = detail.distanceMeters?.let(RouteFormatUtils::formatDistance)
                        renderMarker(detail.latitude, detail.longitude)
                    }
                }
            }
        }
    }

    private fun loadCurrentLocationAndDetail() {
        val permissionHelper = LocationPermissionHelper(requireContext())
        if (permissionHelper.isLocationPermissionGranted()) {
            MyLocationManager(requireContext()).getLastKnownLocation { location ->
                currentLocation = location?.let { CoordinateModel(it.latitude, it.longitude) }
                requestDetail()
            }
        } else {
            requestDetail()
        }
    }

    private fun requestDetail() {
        viewModel.loadPlaceDetail(
            placeId = args.placeId,
            sessionToken = args.sessionToken,
            fallbackTitle = args.fallbackTitle,
            fallbackSubtitle = args.fallbackSubtitle,
            currentLocation = currentLocation,
        )
    }

    private fun renderMarker(latitude: Double, longitude: Double) {
        val manager = pointAnnotationManager ?: return
        manager.deleteAll()
        val drawable = AppCompatResources.getDrawable(requireContext(), com.n3t.mobile.R.drawable.ic_map_pin_new) ?: return
        val bitmap = getBitmapFromDrawable(drawable) ?: return
        manager.create(
            PointAnnotationOptions()
                .withPoint(Point.fromLngLat(longitude, latitude))
                .withIconImage(bitmap)
        )
        binding.mapView.mapboxMap.setCamera(
            CameraOptions.Builder()
                .center(Point.fromLngLat(longitude, latitude))
                .zoom(15.5)
                .build()
        )
    }

    private fun openGoogleMaps(latitude: Double, longitude: Double, label: String) {
        val intent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse("geo:$latitude,$longitude?q=$latitude,$longitude(${Uri.encode(label)})")
        )
        startActivity(intent)
    }

    override fun onDestroyView() {
        pointAnnotationManager = null
        _binding = null
        super.onDestroyView()
    }
}
