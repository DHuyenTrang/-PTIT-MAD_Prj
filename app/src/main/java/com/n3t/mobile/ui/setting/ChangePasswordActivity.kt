package com.n3t.mobile.ui.setting

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.n3t.mobile.databinding.ActivityChangePasswordBinding
import com.n3t.mobile.data.model.authen.login.PasswordUpdateRequest
import com.n3t.mobile.data.repositories.AuthenRepository
import com.n3t.mobile.data.model.api_util.Either
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class ChangePasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChangePasswordBinding
    private val authenRepository: AuthenRepository by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangePasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupListeners()
    }

    private fun setupListeners() {
        binding.btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.btnSavePassword.setOnClickListener {
            val currentPassword = binding.etCurrentPassword.text.toString().trim()
            val newPassword = binding.etNewPassword.text.toString().trim()
            val confirmPassword = binding.etConfirmPassword.text.toString().trim()

            var isValid = true

            if (currentPassword.isEmpty()) {
                binding.txtCurrentPasswordError.text = "Vui lòng nhập mật khẩu hiện tại"
                binding.txtCurrentPasswordError.visibility = View.VISIBLE
                isValid = false
            } else {
                binding.txtCurrentPasswordError.visibility = View.GONE
            }

            if (newPassword.isEmpty() || newPassword.length < 6) {
                binding.txtNewPasswordError.text = "Mật khẩu mới tối thiểu 6 ký tự"
                binding.txtNewPasswordError.visibility = View.VISIBLE
                isValid = false
            } else {
                binding.txtNewPasswordError.visibility = View.GONE
            }

            if (confirmPassword != newPassword) {
                binding.txtConfirmPasswordError.text = "Mật khẩu không trùng khớp"
                binding.txtConfirmPasswordError.visibility = View.VISIBLE
                isValid = false
            } else {
                binding.txtConfirmPasswordError.visibility = View.GONE
            }

            if (isValid) {
                updatePassword(currentPassword, newPassword)
            }
        }
    }

    private fun updatePassword(currentPassword: String, newPassword: String) {
        binding.btnSavePassword.isEnabled = false
        binding.progressBar.visibility = View.VISIBLE

        lifecycleScope.launch {
            val request = PasswordUpdateRequest(
                oldPassword = currentPassword,
                newPassword = newPassword
            )
            when (val result = authenRepository.updatePassword(request)) {
                is Either.Failure -> {
                    binding.btnSavePassword.isEnabled = true
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(
                        this@ChangePasswordActivity,
                        "Đổi mật khẩu thất bại. Vui lòng thử lại.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                is Either.Success -> {
                    binding.btnSavePassword.isEnabled = true
                    binding.progressBar.visibility = View.GONE
                    if (result.value?.success == true) {
                        Toast.makeText(
                            this@ChangePasswordActivity,
                            "Đổi mật khẩu thành công",
                            Toast.LENGTH_SHORT
                        ).show()
                        finish()
                    } else {
                        Toast.makeText(
                            this@ChangePasswordActivity,
                            result.value?.message ?: "Đổi mật khẩu thất bại",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }
}
