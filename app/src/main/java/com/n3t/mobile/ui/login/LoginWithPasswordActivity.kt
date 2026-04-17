package com.n3t.mobile.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.n3t.mobile.databinding.ActivityLoginWithPasswordBinding
import com.n3t.mobile.ui.dashboard.DashboardActivity
import com.n3t.mobile.view_model.authen.login.LoginUIState
import com.n3t.mobile.view_model.authen.login.LoginViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import android.util.Patterns

class LoginWithPasswordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginWithPasswordBinding
    private val viewModel: LoginViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginWithPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupListeners()
        observeViewModel()
    }

    private fun setupListeners() {
        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etCurrentPassword.text.toString().trim()

            var isValid = true

            if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                // Here we reuse txtError for both, or just show text
                binding.txtError.text = "Email không hợp lệ"
                binding.txtError.visibility = View.VISIBLE
                isValid = false
            } else {
                binding.txtError.visibility = View.GONE
            }

            if (isValid && password.isEmpty()) {
                binding.txtError.text = "Vui lòng nhập mật khẩu"
                binding.txtError.visibility = View.VISIBLE
                isValid = false
            }

            if (isValid) {
                viewModel.loginWithPassword(email, password)
            }
        }
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    when (state) {
                        is LoginUIState.Loading -> {
                            binding.btnLogin.isEnabled = false
                        }
                        is LoginUIState.Error -> {
                            binding.btnLogin.isEnabled = true
                            binding.txtError.text = state.message
                            binding.txtError.visibility = View.VISIBLE
                        }
                        is LoginUIState.Success -> {
                            binding.btnLogin.isEnabled = true
                            
                            // Let's assume the session is handled inside AuthenRepository
                            // Navigate to Dashboard
                            val intent = Intent(this@LoginWithPasswordActivity, DashboardActivity::class.java).apply {
                                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            }
                            startActivity(intent)
                            finish()
                        }
                        LoginUIState.Idle -> {
                            binding.btnLogin.isEnabled = true
                        }
                    }
                }
            }
        }
    }
}
