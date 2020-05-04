package com.example.hebiibeh.singluck

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_top_menu.*

class TopMenuActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_top_menu)

        initBtn()
    }

    private fun initBtn() {
        goRecSoundBtn.setOnClickListener {
            val intent = Intent(this, RecSoundActivity::class.java)
            startActivity(intent)
        }
        goPlaySoundBtn.setOnClickListener {
            val intent = Intent(this, PlaySoundActivity::class.java)
            startActivity(intent)
        }
    }
}