package com.example.hebiibeh.singluck

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_rec_sound.*

class RecSoundActivity : AppCompatActivity() {

    // 録音中→true
    // 録音開始・終了を同一ボタンで行うため、切り替えのために使用。
    private var recStartFlag = false
    private lateinit var realm: Realm

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rec_sound)

        realm = Realm.getDefaultInstance()
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
                true -> {
                    stopService(intent)
                    recSoundBtn.text = resources.getString(R.string.rec_sound_btn_txt)
                    val recSaveConfirmDialog = RecSaveConfirmDialogFragment()
                    recSaveConfirmDialog.show(supportFragmentManager, "sample")
                }
                false -> {
                    startService(intent)
                    recSoundBtn.text = resources.getString(R.string.rec_stop_btn_txt)
                }
            }
            // 録音⇔停止の切り替え判定用
            recStartFlag = !recStartFlag
        }
    }
}