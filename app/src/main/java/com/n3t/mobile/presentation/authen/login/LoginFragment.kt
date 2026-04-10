package com.n3t.mobile.presentation.authen.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.n3t.mobile.R
import com.n3t.mobile.data.model.api_util.whenLeftRight
import com.n3t.mobile.databinding.FragmentLoginBinding
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val viewModel: LoginViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        collectFlows()
    }

    private fun setupUI() {
        binding.btnLogin.setOnClickListener {
            val phone = binding.etPhone.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            if (phone.isEmpty()) {
                binding.etPhone.error = getString(R.string.error_empty_phone)
                return@setOnClickListener
            }
            if (password.isEmpty()) {
                binding.etPassword.error = getString(R.string.error_empty_password)
                return@setOnClickListener
            }

            viewModel.loginWithPassword(phone, password)
        }

        binding.tvRegister.setOnClickListener {
            findNavController().navigate(R.id.action_login_to_register)
        }

        binding.tvForgotPassword.setOnClickListener {
            findNavController().navigate(R.id.action_login_to_forgot_password)
        }
    }

    private fun collectFlows() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.isLoading.collect { isLoading ->
                        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
                        binding.btnLogin.isEnabled = !isLoading
                    }
                }

                launch {
                    viewModel.loginResult.collect { result ->
                        result?.whenLeftRight(
                            { failure ->
                                Toast.makeText(
                                    requireContext(),
                                    failure.message ?: getString(R.string.error_something_wrong),
                                    Toast.LENGTH_SHORT
                                ).show()
                            },
                            { response ->
                                if (response?.data != null) {
                                    findNavController().navigate(R.id.action_login_to_map)
                                }
                            }
                        )
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
