package com.example.codingchallange.ui.home

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.codingchallange.MainActivity
import com.example.codingchallange.databinding.ActivityHomeBinding
import com.example.codingchallange.ui.main.QuizQuestionsActivity

class HomeActivity : AppCompatActivity() {
    private lateinit var homeBinding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeBinding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(homeBinding.root)

        onClickListeners()

    }

    private fun onClickListeners() {
        homeBinding.btnPlay.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }
}