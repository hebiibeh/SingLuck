package com.example.hebiibeh.singluck.main.searchsound

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.hebiibeh.singluck.*
import com.example.hebiibeh.singluck.main.playsoundlist.PlaySoundListActivity
import com.example.hebiibeh.singluck.main.recsound.RecSoundActivity
import com.example.hebiibeh.singluck.main.topmenu.TopMenuActivity
import com.example.hebiibeh.singluck.model.RecSoundInfoData
import io.realm.Realm
import io.realm.Sort
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.activity_search_sound.*
import kotlinx.android.synthetic.main.activity_search_sound.goTopMenuBtn

class SearchSoundActivity : AppCompatActivity() {

    private var realm = Realm.getDefaultInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_sound)

        initBtn()
    }

    override fun onResume() {
        super.onResume()
        // 初回表示以降に録音画面から遷移した際、
        // コンボボックスの中身が更新されるようにonResumeで実施
        setRecInfoToSpinner()
    }

    private fun initBtn() {
        goTopMenuBtn.setOnClickListener {
            val intent = Intent(this, TopMenuActivity::class.java)
            // TOP画面で戻るボタン押下した際にアプリを終了させるため、
            // TOP画面に遷移時は他アクティビティのインスタンスを全削除。
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
        }

        goRecordSoundBtn.setOnClickListener {
            val intent = Intent(this, RecSoundActivity::class.java)
            // 戻るボタン押下時の処理が以下の例の通りとなるように、
            // アクティビティの表示履歴の順序を入れ替える。
            // 誤：TOP→A→B→A→Bと遷移した場合、B→A→B→A→TOPの順序で戻る。
            //　正：TOP→A→B→A→Bと遷移した場合、B→A→TOPの順序で戻る。
            intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
            startActivity(intent)
        }

        playSoundListBtn.setOnClickListener {
            // 再生リスト画面への遷移+自動再生開始
            goPlaySoundList(true)
        }

        goPlaySoundListBtn.setOnClickListener {
            // 再生リスト画面への遷移のみ
            goPlaySoundList(false)
        }
    }

    private fun goPlaySoundList(requestAutoPlayFlag: Boolean) {
        val intent = Intent(this, PlaySoundListActivity::class.java)

        intent.putExtra(
            "fileNameNoExtension",
            (recFileNameSpinner.selectedItem as RecSoundInfoData).fileNameNoExtension
        )
        intent.putExtra("createDate", inputCreateDateTxt.text.toString())
        intent.putExtra("requestPlayFlag", requestAutoPlayFlag)

        startActivity(intent)
    }

    private fun setRecInfoToSpinner() {
        val recInfoAll = mutableListOf<RecSoundInfoData>()
        // コンボボックスの一行目は空欄とするため、空のモデルをセット
        recInfoAll.add(RecSoundInfoData())

        val results =
            realm.where<RecSoundInfoData>().findAll().sort("soundId", Sort.DESCENDING)
        recInfoAll.addAll(results)

        val recSoundAdapter =
            SearchFileNameSpinnerAdapter(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                recInfoAll.toTypedArray()
            )
        recFileNameSpinner.adapter = recSoundAdapter
    }

    override fun onDestroy() {
        super.onDestroy()
        realm.close()
    }
}