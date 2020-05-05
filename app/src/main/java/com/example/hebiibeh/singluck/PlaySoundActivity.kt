package com.example.hebiibeh.singluck

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import io.realm.Realm
import io.realm.Sort
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.activity_song_main.*

class PlaySoundActivity : AppCompatActivity() {

    private lateinit var realm: Realm

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_song_main)

        realm = Realm.getDefaultInstance()

        initBtn()
        setSoundFileList()
    }

    private fun setSoundFileList() {

        // 登録済みの曲リストを全件取得
        val realmResults = realm.where<RecSoundData>().findAll().sort("soundId", Sort.DESCENDING)
        var soundFileNameArray: Array<String?> = arrayOfNulls(realmResults.size)

        // 画面のコンボボックスに表示するため、曲リストの取得結果からsoundIdを取得
        for (i in 0 until realmResults.size) {
            soundFileNameArray[i] = realmResults[i]?.fileName.toString()
        }

        val adapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, soundFileNameArray)
        songList.adapter = adapter
    }

    private fun initBtn() {
        goTopMenuBtn.setOnClickListener {
            val intent = Intent(this, TopMenuActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
        }
        singMainBtn.setOnClickListener {
            val intent = Intent(this, RecSoundActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
            startActivity(intent)
        }
        playSoundBtn.setOnClickListener {
            val intent = Intent(this, PlaySoundService::class.java)
            intent.putExtra("selectedSoundFileName", songList.selectedItem.toString())
            startService(intent)
        }
    }
}
