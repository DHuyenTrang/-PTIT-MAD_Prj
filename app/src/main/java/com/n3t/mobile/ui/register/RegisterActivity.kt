package com.n3t.mobile.ui.register

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.n3t.mobile.databinding.ActivityRegisterBinding
import com.n3t.mobile.ui.verify_otp.VerifyOtpActivity
import com.n3t.mobile.view_model.authen.register.RegisterUIState
import com.n3t.mobile.view_model.authen.register.RegisterViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import android.util.Patterns

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private val viewModel: RegisterViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupListeners()
        observeViewModel()
    }

    private fun setupListeners() {
        binding.btnRegister.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etCurrentPassword.text.toString().trim()

            var isValid = true

            if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.txtEmailError.visibility = View.VISIBLE
                isValid = false
            } else {
                binding.txtEmailError.visibility = View.GONE
            }

            if (password.isEmpty() || password.length < 6) {
                binding.txtPassError.visibility = View.VISIBLE
                binding.txtPassError.text = "Mật khẩu tối thiểu 6 ký tự"
                isValid = false
            } else {
                binding.txtPassError.visibility = View.GONE
            }

            if (isValid) {
                viewModel.checkEmail(email)
            }
        }
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    when (state) {
                        is RegisterUIState.Loading -> {
                            binding.btnRegister.isEnabled = false
                        }
                        is RegisterUIState.EmailExistsError -> {
                            binding.btnRegister.isEnabled = true
                            binding.txtEmailError.text = state.message
                            binding.txtEmailError.visibility = View.VISIBLE
                        }
                        is RegisterUIState.Error -> {
                            binding.btnRegister.isEnabled = true
                            binding.txtEmailError.text = "Có lỗi xảy ra, vui lòng thử lại."
                            binding.txtEmailError.visibility = View.VISIBLE
                        }
                        is RegisterUIState.Success -> {
                            binding.btnRegister.isEnabled = true
                            // Navigate to OTP Screen
                            val intent = Intent(this@RegisterActivity, VerifyOtpActivity::class.java).apply {
                                putExtra("EMAIL", state.email)
                                putExtra("PASSWORD", binding.etCurrentPassword.text.toString().trim())
                            }
                            startActivity(intent)
                        }
                        RegisterUIState.Idle -> {
                            binding.btnRegister.isEnabled = true
                        }
                    }
                }
            }
        }
    }
}
