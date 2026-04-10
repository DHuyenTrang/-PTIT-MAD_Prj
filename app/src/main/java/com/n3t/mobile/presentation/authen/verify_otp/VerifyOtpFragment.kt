package com.n3t.mobile.presentation.authen.verify_otp

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
import androidx.navigation.fragment.navArgs
import com.n3t.mobile.R
import com.n3t.mobile.data.model.api_util.whenLeftRight
import com.n3t.mobile.databinding.FragmentVerifyOtpBinding
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class VerifyOtpFragment : Fragment() {

    private var _binding: FragmentVerifyOtpBinding? = null
    private val binding get() = _binding!!
    private val viewModel: VerifyOtpViewModel by viewModel()
    private val args: VerifyOtpFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentVerifyOtpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvPhone.text = args.phone

        binding.btnVerify.setOnClickListener {
            val otpCode = binding.etOtp.text.toString().trim()
            if (otpCode.isEmpty()) return@setOnClickListener
            viewModel.verifyOtp(args.phone, otpCode)
        }

        binding.tvResendOtp.setOnClickListener {
            viewModel.resendOtp(args.phone)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.isLoading.collect { isLoading ->
                        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
                        binding.btnVerify.isEnabled = !isLoading
                    }
                }

                launch {
                    viewModel.verifyResult.collect { result ->
                        result?.whenLeftRight(
                            { failure ->
                                Toast.makeText(requireContext(), failure.message ?: getString(R.string.error_something_wrong), Toast.LENGTH_SHORT).show()
                            },
                            {
                                findNavController().navigate(R.id.action_verify_otp_to_map)
                            }
                        )
                    }
                }

                launch {
                    viewModel.resendResult.collect { result ->
                        result?.whenLeftRight(
                            { failure ->
                                Toast.makeText(requireContext(), failure.message ?: getString(R.string.error_something_wrong), Toast.LENGTH_SHORT).show()
                            },
                            {
                                Toast.makeText(requireContext(), getString(R.string.success), Toast.LENGTH_SHORT).show()
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
