package com.n3t.mobile.presentation.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.n3t.mobile.R
import com.n3t.mobile.databinding.FragmentMapBinding

class MapFragment : Fragment() {

    private var _binding: FragmentMapBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentMapBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fabSearch.setOnClickListener {
            findNavController().navigate(R.id.action_map_to_search)
        }

        binding.fabSettings.setOnClickListener {
            findNavController().navigate(R.id.action_map_to_settings)
        }

        binding.fabHistory.setOnClickListener {
            findNavController().navigate(R.id.action_map_to_move_history)
        }

        binding.fabSavedPlaces.setOnClickListener {
            findNavController().navigate(R.id.action_map_to_saved_places)
        }

        binding.fabLicensePlate.setOnClickListener {
            findNavController().navigate(R.id.action_map_to_license_plate)
        }

        binding.fabBot.setOnClickListener {
            findNavController().navigate(R.id.action_map_to_bot)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
