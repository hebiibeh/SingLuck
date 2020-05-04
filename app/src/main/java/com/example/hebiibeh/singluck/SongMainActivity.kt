package com.example.hebiibeh.singluck

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import io.realm.Realm
import io.realm.Sort
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.activity_sing_main.*
import kotlinx.android.synthetic.main.activity_sing_main.topMenuBtn
import kotlinx.android.synthetic.main.activity_song_main.*

class SongMainActivity : AppCompatActivity() {

    private lateinit var realm: Realm

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_song_main)

        realm = Realm.getDefaultInstance()
        initBtn()
        displaySoundFileName()
    }

    private fun displaySoundFileName() {
        val realmResults = realm.where<RecordSoundData>().findAll().sort("soundId", Sort.DESCENDING)
        var soundIdArray: Array<String?> = arrayOfNulls(realmResults.size)

        for (i in 0 until realmResults.size) {
            soundIdArray[i] = realmResults[i]?.soundId.toString()
        }

        val adapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, soundIdArray)
        songList.adapter = adapter
    }

    private fun initBtn() {
        topMenuBtn.setOnClickListener {
            val intent = Intent(this, TopMenuActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
        }
        singMainBtn.setOnClickListener {
            val intent = Intent(this, SingMainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
            startActivity(intent)
        }

        playSoundBtn.setOnClickListener {
            val intent = Intent(this, PlaySoundService::class.java)
            intent.putExtra("selectedSoundId", songList.selectedItem.toString())
            startService(intent)
        }
    }
}
