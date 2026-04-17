package com.n3t.mobile.ui.map.widget

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.n3t.mobile.data.model.place.StationModel
import com.n3t.mobile.data.model.place.StationProvider
import com.n3t.mobile.data.model.place_flow.CoordinateModel
import com.n3t.mobile.databinding.BottomSheetStationInfoBinding
import com.n3t.mobile.ui.routing.RoutingActivity
import com.n3t.mobile.utils.location.MyLocationManager
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Bottom sheet that displays station info (name, address, working hours, capacity)
 * and a "Xem đường đi" (Navigate) button.
 */
class StationInfoBottomSheet : BottomSheetDialogFragment() {

    private var _binding: BottomSheetStationInfoBinding? = null
    private val binding get() = _binding!!

    private var station: StationModel? = null
    private var stationProvider: StationProvider = StationProvider.NONE

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetStationInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        station?.let { bindStationData(it) }
    }

    private fun bindStationData(station: StationModel) {
        // Name
        binding.txtNameStation.text = station.displayName ?: "Trạm không tên"

        // Address
        binding.txtAddressStation.text = station.address ?: "Không có địa chỉ"

        // Sunday status
        binding.txtSunday.text = if (station.offSunday == true) "Không" else "Có"

        // Working hours & status
        if (station.startWorkingTime == null || station.endWorkingTime == null) {
            binding.layoutOpenStatus.visibility = View.GONE
        } else {
            binding.layoutOpenStatus.visibility = View.VISIBLE
            val isOpen = isStationOpen(station.startWorkingTime, station.endWorkingTime)
            binding.txtStatus.text = if (isOpen) "Đang mở cửa" else "Đã đóng cửa"
            binding.txtCloseTime.text = "Đóng cửa lúc ${station.endWorkingTime}"
        }

        // Capacity (for charging stations)
        if (stationProvider == StationProvider.CHARGE && station.extendData?.capacityType != null) {
            binding.layoutCapacity.visibility = View.VISIBLE
            binding.txtCapacity.text = extractCapacityType(station.extendData.capacityType)
        } else {
            binding.layoutCapacity.visibility = View.GONE
        }

        // Other info container
        setupOtherInfo(station)

        // Navigate button (in-scroll)
        binding.btnViewRouteStationInLayout.setOnClickListener {
            navigateToStation(station)
        }

        // Navigate button (sticky bottom — hidden by default in XML, kept as fallback)
        binding.btnViewRouteStation.setOnClickListener {
            navigateToStation(station)
        }
    }

    private fun navigateToStation(station: StationModel) {
        val lon = station.position?.firstOrNull() ?: return
        val lat = station.position.lastOrNull() ?: return

        val locationManager = MyLocationManager(requireContext())
        locationManager.getLastKnownLocation { location ->
            val origin = if (location != null) {
                CoordinateModel(location.latitude, location.longitude)
            } else {
                // Fallback: use station itself if location unavailable
                return@getLastKnownLocation
            }

            startActivity(
                RoutingActivity.newIntent(
                    context = requireContext(),
                    origin = origin,
                    destination = CoordinateModel(lat, lon),
                    destinationName = station.displayName ?: "",
                    destinationAddress = station.address ?: "",
                )
            )
            dismiss()
        }
    }

    private fun setupOtherInfo(station: StationModel) {
        val container = binding.containerOtherInfo
        container.removeAllViews()

        station.extendData?.others?.forEach { pair ->
            if (pair.size >= 2) {
                val textView = android.widget.TextView(requireContext()).apply {
                    text = "${pair[0]}: ${pair[1]}"
                    setTextColor(
                        resources.getColor(com.n3t.mobile.R.color.onSurfaceVariant, null)
                    )
                    setPadding(0, 4, 0, 4)
                }
                container.addView(textView)
            }
        }

        // Show container only if there are items
        container.alpha = if (container.childCount > 0) 1f else 0f
    }

    private fun isStationOpen(startTime: String, endTime: String): Boolean {
        return try {
            val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
            val start = sdf.parse(startTime) ?: return true
            val end = sdf.parse(endTime) ?: return true
            val now = sdf.parse(sdf.format(Date())) ?: return true
            now.after(start) && now.before(end)
        } catch (e: Exception) {
            true // Default to open if parsing fails
        }
    }

    private fun extractCapacityType(capacityType: Int?): String {
        return when (capacityType) {
            1 -> "Chậm 3.5KW"
            2 -> "Bình thường 7-22KW"
            3 -> "Nhanh 50KW"
            4 -> "Siêu nhanh 80KW"
            else -> "Không rõ"
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    companion object {
        fun newInstance(
            station: StationModel,
            provider: StationProvider
        ): StationInfoBottomSheet {
            return StationInfoBottomSheet().apply {
                this.station = station
                this.stationProvider = provider
            }
        }
    }
}
