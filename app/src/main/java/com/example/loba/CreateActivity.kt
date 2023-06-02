package com.example.loba

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.loba.databinding.ActivityCreateBinding
import com.example.loba.databinding.ActivityPostsBinding


private const val TAG = "CreateActivity"
class CreateActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreateBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create)

        binding.btnPickImage.setOnClickListener {
            Log.i(TAG, "Open up image picker on device")
        }
    }
}
