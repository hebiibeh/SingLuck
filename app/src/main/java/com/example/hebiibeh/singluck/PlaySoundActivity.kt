package com.example.hebiibeh.singluck

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import io.realm.Realm
import io.realm.Sort
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.activity_play_sound.*

class PlaySoundActivity : AppCompatActivity() {

    private lateinit var realm: Realm

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play_sound)

        realm = Realm.getDefaultInstance()
        initBtn()
        setRecSoundDataToSpinner()
    }

    private fun initBtn() {
        goTopMenuBtn.setOnClickListener {
            val intent = Intent(this, TopMenuActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
        }

        goRecordSoundBtn.setOnClickListener {
            val intent = Intent(this, RecSoundActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
            startActivity(intent)
        }

        playSoundBtn.setOnClickListener {
            val intent = Intent(this, PlaySoundService::class.java)
            val selectedSoundName =
                (recFileNameSpinner.selectedItem as RecSoundData).fileNameNoExtension
            intent.putExtra("selectedSoundName", selectedSoundName)
            startService(intent)
        }
    }

    private fun setRecSoundDataToSpinner() {
        val registeredRecSoundAll =
            realm.where<RecSoundData>().findAll().sort("soundId", Sort.DESCENDING)

        val recSoundAdapter = RecordSoundAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            registeredRecSoundAll.toTypedArray()
        )
        recFileNameSpinner.adapter = recSoundAdapter
    }
}
