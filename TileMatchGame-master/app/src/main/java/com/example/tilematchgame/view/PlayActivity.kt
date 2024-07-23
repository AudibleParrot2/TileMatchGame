package com.example.tilematchgame.view

import android.content.Intent
import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.example.tilematchgame.MainActivity
import com.example.tilematchgame.R

class PlayActivity : AppCompatActivity() {
    private lateinit var playBtn :ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play)

        val action = supportActionBar
        action?.hide()
        playBtn = findViewById(R.id.playBtn)
        playBtn.setOnClickListener{
            startActivity(Intent(this@PlayActivity,MainActivity::class.java))
        }
    }
}