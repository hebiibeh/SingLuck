package com.example.hebiibeh.singluck

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_sing_main.*

class SingMainActivity : AppCompatActivity() {

    private var recStartFlag = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sing_main)

        initBtn()

    }

    private fun initBtn() {
        topMenuBtn.setOnClickListener {
            val intent = Intent(this, TopMenuActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
        }
        songMainBtn.setOnClickListener {
            val intent = Intent(this, SongMainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
            startActivity(intent)
        }
        recSoundBtn.setOnClickListener {

            val intent = Intent(this, RecSoundService::class.java)

            when (recStartFlag) {
                true -> stopService(intent)
                false -> startService(intent)
            }
            recStartFlag = !recStartFlag

        }
    }
}
