package com.n3t.mobile.presentation.authen.forgot_password

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
import com.n3t.mobile.databinding.FragmentForgotPasswordBinding
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class ForgotPasswordFragment : Fragment() {

    private var _binding: FragmentForgotPasswordBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ForgotPasswordViewModel by viewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentForgotPasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSendOtp.setOnClickListener {
            val phone = binding.etPhone.text.toString().trim()
            if (phone.isEmpty()) {
                binding.etPhone.error = getString(R.string.error_empty_phone)
                return@setOnClickListener
            }
            viewModel.forgotPassword(phone)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.isLoading.collect { isLoading ->
                        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
                        binding.btnSendOtp.isEnabled = !isLoading
                    }
                }

                launch {
                    viewModel.forgotPasswordResult.collect { result ->
                        result?.whenLeftRight(
                            { failure ->
                                Toast.makeText(requireContext(), failure.message ?: getString(R.string.error_something_wrong), Toast.LENGTH_SHORT).show()
                            },
                            {
                                val phone = binding.etPhone.text.toString().trim()
                                val action = ForgotPasswordFragmentDirections.actionForgotPasswordToVerifyOtp(phone, "forgot_password")
                                findNavController().navigate(action)
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
