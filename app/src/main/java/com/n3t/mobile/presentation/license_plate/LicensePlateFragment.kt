package com.n3t.mobile.presentation.license_plate

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.n3t.mobile.R
import com.n3t.mobile.databinding.FragmentLicensePlateBinding
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class LicensePlateFragment : Fragment() {

    private var _binding: FragmentLicensePlateBinding? = null
    private val binding get() = _binding!!
    private val viewModel: LicensePlateViewModel by viewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentLicensePlateBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnCheck.setOnClickListener {
            val licensePlate = binding.etLicensePlate.text.toString().trim()
            if (licensePlate.isEmpty()) {
                binding.etLicensePlate.error = getString(R.string.enter_license_plate)
                return@setOnClickListener
            }
            viewModel.licensePlatesQuery(licensePlate)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.isLoading.collect { isLoading ->
                        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
                    }
                }

                launch {
                    viewModel.haveFine.collect { hasFine ->
                        hasFine ?: return@collect
                        if (hasFine) {
                            binding.tvResult.text = getString(R.string.has_fine)
                            binding.tvResult.setTextColor(resources.getColor(android.R.color.holo_red_dark, null))
                        } else {
                            binding.tvResult.text = getString(R.string.no_fine)
                            binding.tvResult.setTextColor(resources.getColor(android.R.color.holo_green_dark, null))
                        }
                        binding.tvResult.visibility = View.VISIBLE
                    }
                }

                launch {
                    viewModel.licensePlatesQuery.collect { info ->
                        info ?: return@collect
                        binding.tvViolationDetail.text = buildString {
                            info.violationType?.let { append("Lỗi: $it\n") }
                            info.violationTime?.let { append("Thời gian: $it\n") }
                            info.violationLocation?.let { append("Địa điểm: $it\n") }
                            info.status?.let { append("Trạng thái: $it\n") }
                            info.resolutionAgency?.let { append("Cơ quan: $it") }
                        }
                        binding.tvViolationDetail.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
