package com.hecos.mobile.ui.rationale

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.hecos.mobile.databinding.ActivityRationaleBinding

class RationaleActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRationaleBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRationaleBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}
