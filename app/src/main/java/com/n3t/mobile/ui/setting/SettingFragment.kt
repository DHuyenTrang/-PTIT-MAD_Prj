package com.n3t.mobile.ui.setting

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.n3t.mobile.R
import com.n3t.mobile.data.datasources.local.AppStore
import com.n3t.mobile.databinding.FragmentSettingBinding
import com.n3t.mobile.ui.login.AuthEntryActivity
import com.n3t.mobile.view_model.setting.SettingUIState
import com.n3t.mobile.view_model.setting.SettingViewModel
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingFragment : Fragment() {

    private var _binding: FragmentSettingBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SettingViewModel by viewModel()
    private val appStore: AppStore by inject()

    private var selectedImageUri: Uri? = null

    private val imagePickerLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.data?.let { uri ->
                    selectedImageUri = uri
                    binding.ivAvatar.setImageURI(uri)
                }
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadUserData()
        setupListeners()
        observeViewModel()
    }

    private fun loadUserData() {
        val displayName = appStore.getDisplayName()
        val phoneNumber = appStore.getPhoneNumber()

        binding.tvDisplayName.text = displayName ?: "Người dùng"
        binding.etName.setText(displayName ?: "")
        binding.etEmail.setText(phoneNumber ?: "")
    }

    private fun setupListeners() {
        binding.btnBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        binding.btnChangePhoto.setOnClickListener {
            openImagePicker()
        }

        binding.avatarContainer.setOnClickListener {
            openImagePicker()
        }

        binding.rowChangePassword.setOnClickListener {
            val intent = Intent(requireContext(), ChangePasswordActivity::class.java)
            startActivity(intent)
        }

        binding.rowLogout.setOnClickListener {
            viewModel.logout()
        }
    }

    private fun openImagePicker() {
        val intent = Intent(Intent.ACTION_PICK).apply {
            type = "image/*"
        }
        imagePickerLauncher.launch(intent)
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    when (state) {
                        is SettingUIState.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                            binding.rowLogout.isEnabled = false
                        }
                        is SettingUIState.LogoutSuccess -> {
                            binding.progressBar.visibility = View.GONE
                            appStore.clearAll()

                            val intent = Intent(
                                requireContext(),
                                AuthEntryActivity::class.java
                            ).apply {
                                flags = Intent.FLAG_ACTIVITY_NEW_TASK or
                                        Intent.FLAG_ACTIVITY_CLEAR_TASK
                            }
                            startActivity(intent)
                            requireActivity().finish()
                        }
                        is SettingUIState.Error -> {
                            binding.progressBar.visibility = View.GONE
                            binding.rowLogout.isEnabled = true
                            Toast.makeText(
                                requireContext(),
                                state.message,
                                Toast.LENGTH_SHORT
                            ).show()
                            viewModel.resetState()
                        }
                        SettingUIState.Idle -> {
                            binding.progressBar.visibility = View.GONE
                            binding.rowLogout.isEnabled = true
                        }
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
