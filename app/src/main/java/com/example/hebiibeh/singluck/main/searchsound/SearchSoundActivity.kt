package com.example.hebiibeh.singluck.main.searchsound

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.hebiibeh.singluck.*
import com.example.hebiibeh.singluck.main.playsoundlist.PlaySoundListActivity
import com.example.hebiibeh.singluck.main.recsound.RecSoundActivity
import com.example.hebiibeh.singluck.main.recsound.RecordSoundAdapter
import com.example.hebiibeh.singluck.main.topmenu.TopMenuActivity
import com.example.hebiibeh.singluck.model.RecSoundData
import io.realm.Realm
import io.realm.Sort
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.activity_record_sound.*
import kotlinx.android.synthetic.main.activity_search_sound.*
import kotlinx.android.synthetic.main.activity_search_sound.goTopMenuBtn

class SearchSoundActivity : AppCompatActivity() {

    private lateinit var realm: Realm

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_sound)

        realm = Realm.getDefaultInstance()
        initBtn()
        setRecSoundDataToSpinner()
    }

    override fun onDestroy() {
        super.onDestroy()
        realm.close()
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

        goPlaySoundListBtn.setOnClickListener {
            val intent = Intent(this, PlaySoundListActivity::class.java)
            setSearchCondition(intent)
            intent.putExtra("createDate", inputCreateDateTxt.text.toString())
            intent.putExtra("requestPlayFlag", true)
            startActivity(intent)
        }

        playSoundListBtn.setOnClickListener {
            val intent = Intent(this, PlaySoundListActivity::class.java)
            setSearchCondition(intent)
            intent.putExtra("createDate", inputCreateDateTxt.text.toString())
            intent.putExtra("requestPlayFlag", false)
            startActivity(intent)
        }
    }

    private fun setSearchCondition(intent: Intent) {
        intent.putExtra("createDate", inputCreateDateTxt.text.toString())
    }

    private fun setRecSoundDataToSpinner() {
        val registeredRecSoundAll =
            realm.where<RecSoundData>().findAll().sort("soundId", Sort.DESCENDING)

        val recSoundAdapter =
            RecordSoundAdapter(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                registeredRecSoundAll.toTypedArray()
            )
        recFileNameSpinner.adapter = recSoundAdapter
    }
}
