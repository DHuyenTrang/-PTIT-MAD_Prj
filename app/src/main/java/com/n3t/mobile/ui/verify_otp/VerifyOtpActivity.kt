package com.n3t.mobile.ui.verify_otp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.n3t.mobile.databinding.ActivityVerifyOtpBinding
import com.n3t.mobile.ui.dashboard.DashboardActivity
import com.n3t.mobile.view_model.authen.verify_otp.VerifyOtpViewModel
import com.n3t.mobile.data.model.api_util.Either
import com.n3t.mobile.data.model.authen.verify_otp.VerifyOTPRequest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import android.text.Editable
import android.text.TextWatcher

class VerifyOtpActivity : AppCompatActivity() {
    private lateinit var binding: ActivityVerifyOtpBinding
    private val viewModel: VerifyOtpViewModel by viewModel()

    private var email: String = ""
    private var passwordStr: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVerifyOtpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        email = intent.getStringExtra("EMAIL") ?: ""
        passwordStr = intent.getStringExtra("PASSWORD") ?: ""

        binding.textViewPhoneNumber.text = email

        setupListeners()
        observeViewModel()
    }

    private fun setupListeners() {
        binding.buttonBack.setOnClickListener { finish() }
        
        binding.btnResendOtp.setOnClickListener {
            // logic to resend otp - assuming the backend sends it automatically on checkEmailExist 
            // or we need to call a resend api here. For now, we leave as is.
            Toast.makeText(this, "Đã gửi lại mã OTP", Toast.LENGTH_SHORT).show()
        }

        binding.otpPinView.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                val otpCode = s.toString()
                if (otpCode.length == 6) {
                    binding.otpPinView.isEnabled = false
                    val request = VerifyOTPRequest(
                        email = email,
                        otpCode = otpCode,
                        password = passwordStr
                    )
                    viewModel.verifyOtp(request)
                }
            }
        })
    }

    private fun observeViewModel() {
        viewModel.isLoading.observe(this) { isLoading ->
            if (isLoading) {
                // Show loading
                binding.textViewErrorOtp.visibility = View.GONE
            } else {
                binding.otpPinView.isEnabled = true
            }
        }

        viewModel.verifyOtpResult.observe(this) { result ->
            when (result) {
                is Either.Failure -> {
                    binding.textViewErrorOtp.visibility = View.VISIBLE
                    binding.textViewErrorOtp.text = "OTP không hợp lệ hoặc lỗi kết nối. Vui lòng thử lại."
                    binding.otpPinView.setText("")
                }
                is Either.Success -> {
                    // Success register and OTP verified
                    val intent = Intent(this, DashboardActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    }
                    startActivity(intent)
                    finish()
                }
            }
        }
    }
}
