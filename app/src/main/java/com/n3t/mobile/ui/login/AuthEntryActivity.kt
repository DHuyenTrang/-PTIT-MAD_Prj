package com.n3t.mobile.ui.login

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.n3t.mobile.databinding.ActivityAuthEntryBinding
import com.n3t.mobile.ui.register.RegisterActivity

class AuthEntryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAuthEntryBinding
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthEntryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnNavigateLogin.setOnClickListener {
            startActivity(Intent(this, LoginWithPasswordActivity::class.java))
        }

        binding.btnNavigateRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }
}
