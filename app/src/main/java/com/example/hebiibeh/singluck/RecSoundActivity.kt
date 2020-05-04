package com.example.hebiibeh.singluck

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_rec_sound.*

class RecSoundActivity : AppCompatActivity() {

    // 録音中→true
    // 録音開始・終了を同一ボタンで行うため、切り替えのために使用。
    private var recStartFlag = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rec_sound)

        initBtn()
    }

    private fun initBtn() {
        goTopMenuBtn.setOnClickListener {
            val intent = Intent(this, TopMenuActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
        }
        goPlaySoundBtn.setOnClickListener {
            val intent = Intent(this, PlaySoundActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
            startActivity(intent)
        }
        recSoundBtn.setOnClickListener {
            val intent = Intent(this, RecSoundService::class.java)

            when (recStartFlag) {
                true -> stopService(intent)
                false -> startService(intent)
            }
            // 録音⇔停止の切り替え判定用
            recStartFlag = !recStartFlag
        }
    }
}