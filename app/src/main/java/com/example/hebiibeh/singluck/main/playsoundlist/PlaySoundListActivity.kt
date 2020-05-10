package com.example.hebiibeh.singluck.main.playsoundlist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hebiibeh.singluck.R
import com.example.hebiibeh.singluck.model.RecSoundInfoData
import io.realm.Realm
import io.realm.RealmResults
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.activity_play_sound_list.*
import java.text.SimpleDateFormat
import java.util.*

class PlaySoundListActivity : AppCompatActivity() {

    private val realm = Realm.getDefaultInstance()
    private lateinit var adapter: SoundListAdapter
    private lateinit var layoutManager: RecyclerView.LayoutManager

    companion object {
        // 音声再生中かどうかを判定
        var playSoundFlag = false

        // 他クラスから画面の表示を操作するためにインスタンスを保持
        private lateinit var instance: PlaySoundListActivity
        fun getInstance(): PlaySoundListActivity {
            return instance
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        instance = this
        setContentView(R.layout.activity_play_sound_list)

        initBtn()

        var results: RealmResults<RecSoundInfoData> = findRecSoundInfoByCondition()
        setRecyclerView(results)

        val requestPlayFlag = intent.getBooleanExtra("requestPlayFlag", false)
        if (requestPlayFlag && !results.isNullOrEmpty()) {
            autoPlaySound(requestPlayFlag, results)
        }

        switchPlayAndStopBtnText(playSoundFlag)
    }

    override fun onDestroy() {
        super.onDestroy()
        realm.close()
    }

    fun switchPlayAndStopBtnText(flag: Boolean) {

        playSoundFlag = flag

        when (playSoundFlag) {
            true -> playAndStopBtn.text = resources.getString(R.string.stop_btn)
            false -> playAndStopBtn.text = resources.getString(R.string.play_btn)
        }
    }

    fun setPlayingSoundName(soundName: String) {
        if (soundName != null && soundName != "") {
            playingSoundNameTxt.text = "Now Playing：$soundName"
        } else {
            playingSoundNameTxt.text = ""
        }
    }

    // 再生リストの行数を指定して音声ファイル名を取得
    fun getSoundNameByPosition(position: Int): String {
        if (soundListRecyclerView.findViewHolderForAdapterPosition(position) == null) {
            return ""
        }
        val soundListViewHolder =
            soundListRecyclerView.findViewHolderForAdapterPosition(position) as SoundListViewHolder

        return soundListViewHolder.soundName?.text.toString()
    }

    private fun initBtn() {
        playAndStopBtn.setOnClickListener {
            val soundListViewHolder =
                soundListRecyclerView.findViewHolderForAdapterPosition(0) as SoundListViewHolder?
            if (soundListViewHolder == null) return@setOnClickListener

            val intent = Intent(this, PlaySoundListService::class.java)
            intent.putExtra(
                "selectedFileNameNoExtension",
                soundListViewHolder.soundName?.text.toString()
            )

            when (playSoundFlag) {
                true -> stopService(intent)
                false -> startService(intent)
            }
        }
    }

    private fun autoPlaySound(
        requestPlayFlag: Boolean,
        results: RealmResults<RecSoundInfoData>
    ) {
        val intent = Intent(this, PlaySoundListService::class.java)
        intent.putExtra(
            "selectedFileNameNoExtension",
            results[0]?.fileNameNoExtension.toString()
        )
        startService(intent)

        intent.putExtra("requestPlayFlag", false)
        playSoundFlag = true
    }

    private fun setRecyclerView(results: RealmResults<RecSoundInfoData>) {
        layoutManager = LinearLayoutManager(this)
        soundListRecyclerView.layoutManager = layoutManager
        adapter = SoundListAdapter(results)
        soundListRecyclerView.adapter = adapter
    }

    // TODO:画面で入力された検索条件にあったデータを取得する。冗長なため改善する。
    private fun findRecSoundInfoByCondition(): RealmResults<RecSoundInfoData> {
        var results: RealmResults<RecSoundInfoData>
        val targetDateStr = intent.getStringExtra("createDate")
        val fileNameNoExtension = intent.getStringExtra("fileNameNoExtension")

        if (targetDateStr != null && targetDateStr != "" && fileNameNoExtension != null && fileNameNoExtension != "") {
            val df = SimpleDateFormat("yyyyMMdd")
            val targetCreateDate = df.parse(targetDateStr)
            val cal = Calendar.getInstance()
            cal.time = targetCreateDate
            cal.add(Calendar.DATE, 1)
            results = realm.where<RecSoundInfoData>()
                .between("createDate", targetCreateDate, cal.time)
                .equalTo("fileNameNoExtension", fileNameNoExtension).findAll().sort("soundId")
        } else if (targetDateStr != null && targetDateStr != "") {
            val df = SimpleDateFormat("yyyyMMdd")
            val targetCreateDate = df.parse(targetDateStr)
            val cal = Calendar.getInstance()
            cal.time = targetCreateDate
            cal.add(Calendar.DATE, 1)
            results = realm.where<RecSoundInfoData>()
                .between("createDate", targetCreateDate, cal.time).findAll().sort("soundId")
        } else if (fileNameNoExtension != null && fileNameNoExtension != "") {
            results = realm.where<RecSoundInfoData>()
                .equalTo("fileNameNoExtension", fileNameNoExtension).findAll().sort("soundId")
        } else {
            results = realm.where<RecSoundInfoData>().findAll().sort("soundId")
        }
        return results
    }
}
