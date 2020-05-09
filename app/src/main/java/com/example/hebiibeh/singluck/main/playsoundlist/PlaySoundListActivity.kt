package com.example.hebiibeh.singluck.main.playsoundlist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hebiibeh.singluck.R
import com.example.hebiibeh.singluck.model.RecSoundData
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
        var playSoundFlag = false
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

        switchPlayAndStopBtn(false)

        var results: RealmResults<RecSoundData>
        val targetDateStr = intent.getStringExtra("createDate")
        if (targetDateStr != null && targetDateStr != "") {
            val df = SimpleDateFormat("yyyyMMdd")
            val targetCreateDate = df.parse(targetDateStr)
            val cal = Calendar.getInstance()
            cal.time = targetCreateDate
            cal.add(Calendar.DATE, 1)
            results = realm.where<RecSoundData>()
                .between("createDate", targetCreateDate, cal.time).findAll()
        } else {
            results = realm.where<RecSoundData>().findAll()
        }
        layoutManager = LinearLayoutManager(this)
        soundListRecyclerView.layoutManager = layoutManager

        adapter = SoundListAdapter(results)
        soundListRecyclerView.adapter = adapter

    }

    private fun initBtn() {
        playAndStopBtn.setOnClickListener {
            val soundListViewHolder =
                soundListRecyclerView.findViewHolderForAdapterPosition(0) as SounListViewHolder
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

    fun switchPlayAndStopBtn(flag: Boolean) {

        playSoundFlag = flag

        when (playSoundFlag) {
            true -> playAndStopBtn.text = resources.getString(R.string.stop_btn)
            false -> playAndStopBtn.text = resources.getString(R.string.play_btn)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        realm.close()
    }

    fun getSoundNameByPosition(position: Int): String {
        if (soundListRecyclerView.findViewHolderForAdapterPosition(position) == null) {
            return ""
        }
        val soundListViewHolder =
            soundListRecyclerView.findViewHolderForAdapterPosition(position) as SounListViewHolder

        return soundListViewHolder.soundName?.text.toString()
    }
}
