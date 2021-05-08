package com.rushik.cowinslotapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.rushik.cowinslotapp.R
import com.rushik.cowinslotapp.databinding.ActivityTestBinding

class TestActivity : AppCompatActivity() {
    private lateinit var activityTestBinding: ActivityTestBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityTestBinding = DataBindingUtil.setContentView(this, R.layout.activity_test)
    }
}