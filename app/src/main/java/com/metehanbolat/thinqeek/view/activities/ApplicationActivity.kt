package com.metehanbolat.thinqeek.view.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.metehanbolat.thinqeek.databinding.ActivityApplicationBinding

class ApplicationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityApplicationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityApplicationBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }
}