package com.n3t.mobile.presentation.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.n3t.mobile.R
import com.n3t.mobile.data.datasources.local.AppStore
import com.n3t.mobile.databinding.FragmentSettingsBinding
import org.koin.android.ext.android.inject

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    private val appStore: AppStore by inject()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Display user info
        binding.tvDisplayName.text = appStore.getDisplayName() ?: "User"
        binding.tvPhone.text = appStore.getPhoneNumber() ?: ""

        binding.btnChangePassword.setOnClickListener {
            findNavController().navigate(R.id.action_settings_to_change_password)
        }

        binding.btnEditProfile.setOnClickListener {
            findNavController().navigate(R.id.action_settings_to_edit_profile)
        }

        binding.btnLogout.setOnClickListener {
            appStore.clearAll()
            findNavController().navigate(R.id.action_settings_to_login)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
