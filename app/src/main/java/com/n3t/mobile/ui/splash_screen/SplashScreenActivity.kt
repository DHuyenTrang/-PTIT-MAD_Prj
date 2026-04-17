package com.n3t.mobile.ui.splash_screen

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.n3t.mobile.ui.dashboard.DashboardActivity
import com.n3t.mobile.ui.login.AuthEntryActivity
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class SplashScreenActivity : AppCompatActivity() {

    private val viewModel: SplashViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        
        // Keep the splash screen visible until we've decided where to navigate
        splashScreen.setKeepOnScreenCondition { true }

        navigateTo(DashboardActivity::class.java)

//        lifecycleScope.launch {
//            repeatOnLifecycle(Lifecycle.State.STARTED) {
//                viewModel.splashState.collect { state ->
//                    when (state) {
//                        is SplashState.GoToDashboard -> {
//                            navigateTo(DashboardActivity::class.java)
//                        }
//                        is SplashState.GoToLogin -> {
//                            navigateTo(AuthEntryActivity::class.java)
//                        }
//                        SplashState.Initial -> {
//                            // Do nothing, waiting...
//                        }
//                    }
//                }
//            }
//        }

        viewModel.checkUserProfile()
    }

    private fun navigateTo(activityClass: Class<*>) {
        val intent = Intent(this, activityClass)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}
