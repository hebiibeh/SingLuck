package com.example.hebiibeh.singluck.main.recsound

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.hebiibeh.singluck.R
import com.example.hebiibeh.singluck.main.topmenu.TopMenuActivity
import com.example.hebiibeh.singluck.main.searchsound.SearchSoundActivity
import com.example.hebiibeh.singluck.model.RecSoundData
import io.realm.Realm
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.activity_record_sound.*

class RecSoundActivity : AppCompatActivity() {

    // 録音中→true
    // 録音開始・終了を同一ボタンで行うため、切り替えのために使用。
    private var recStartFlag = false
    private lateinit var realm: Realm
    private var nextSoundId: Long = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_record_sound)

        realm = Realm.getDefaultInstance()
        initBtn()
    }

    private fun initBtn() {
        goTopMenuBtn.setOnClickListener {
            val intent = Intent(this, TopMenuActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
        }
        goSearchSoundBtn.setOnClickListener {
            val intent = Intent(this, SearchSoundActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
            startActivity(intent)
        }
        recSoundBtn.setOnClickListener {

            val intent = Intent(this, RecSoundService::class.java)

            when (recStartFlag) {
                true -> {
                    stopService(intent)
                    recSoundBtn.text = resources.getString(R.string.rec_sound_btn)
                    val recSaveConfirmDialog =
                        SaveSoundConfirmDialogFragment()
                    recSaveConfirmDialog.setTargetSoundId(nextSoundId)
                    recSaveConfirmDialog.show(supportFragmentManager, "sample")
                }
                false -> {
                    createNextSoundId()
                    intent.putExtra("nextSoundId", nextSoundId)
                    startService(intent)
                    recSoundBtn.text = resources.getString(R.string.rec_stop_btn)
                }
            }
            // 録音⇔停止の切り替え判定用
            recStartFlag = !recStartFlag
        }
    }

    private fun createNextSoundId() {
        nextSoundId =
            (realm.where<RecSoundData>().max("soundId")?.toLong() ?: 0L) + 1L
    }
}