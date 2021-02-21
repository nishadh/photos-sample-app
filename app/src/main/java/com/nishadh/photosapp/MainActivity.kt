package com.nishadh.photosapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.nishadh.photosapp.ui.main.MainFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
    }
}