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
        singBtn.setOnClickListener {
            val intent = Intent(this, SingMainActivity::class.java)
            startActivity(intent)
        }
        songBtn.setOnClickListener {
            val intent = Intent(this, SongMainActivity::class.java)
            startActivity(intent)
        }
    }
}
