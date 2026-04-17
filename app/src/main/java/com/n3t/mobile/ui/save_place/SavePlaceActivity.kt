package com.n3t.mobile.ui.save_place

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.n3t.mobile.R
import com.n3t.mobile.databinding.ActivitySavePlaceBinding

class SavePlaceActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySavePlaceBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySavePlaceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, SavedLocationFragment())
                .commit()
        }
    }

    fun openSearchToSave() {
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(
                R.anim.slide_in_right,
                R.anim.slide_out_left,
                R.anim.slide_in_left,
                R.anim.slide_out_right
            )
            .replace(R.id.fragment_container, SearchToSavedLocationFragment())
            .addToBackStack(null)
            .commit()
    }
}
