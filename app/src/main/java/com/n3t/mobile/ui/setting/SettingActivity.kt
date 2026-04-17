package com.n3t.mobile.ui.setting

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.n3t.mobile.R

class SettingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.setting_fragment_container, SettingFragment())
                .commit()
        }
    }
}
