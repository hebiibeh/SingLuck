package com.example.hebiibeh.singluck.main.recsound

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.hebiibeh.singluck.R
import com.example.hebiibeh.singluck.main.topmenu.TopMenuActivity
import com.example.hebiibeh.singluck.main.searchsound.SearchSoundActivity
import com.example.hebiibeh.singluck.model.RecSoundInfoData
import io.realm.Realm
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.activity_record_sound.*

class RecSoundActivity : AppCompatActivity() {

    private val realm = Realm.getDefaultInstance()

    // 録音中→true。録音開始・終了を同一ボタンで行うため、切り替えのために使用。
    private var nowRecordingFlag = false

    // 次に録音する音声に割り当てるsoundId。DBに登録されているMax(soundId)+1。
    private var nextSoundId: Long = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_record_sound)

        initBtn()
    }

    private fun initBtn() {
        goTopMenuBtn.setOnClickListener {
            val intent = Intent(this, TopMenuActivity::class.java)

            // TOP画面で戻るボタン押下した際にアプリを終了させるため、
            // TOP画面に遷移時は他アクティビティのインスタンスを全削除。
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
        }
        goSearchSoundBtn.setOnClickListener {
            val intent = Intent(this, SearchSoundActivity::class.java)
            // 戻るボタン押下時の処理が以下の例の通りとなるように、
            // アクティビティの表示履歴の順序を入れ替える。
            // 誤：TOP→A→B→A→Bと遷移した場合、B→A→B→A→TOPの順序で戻る。
            //　正：TOP→A→B→A→Bと遷移した場合、B→A→TOPの順序で戻る。
            intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
            startActivity(intent)
        }
        recSoundBtn.setOnClickListener {
            // recSoundBtn１つで録音、録音終了機能を交互に切り替えて使う。
            when (nowRecordingFlag) {
                true -> this.recStop()
                false -> this.recStart()
            }
            // 録音中⇔停止中の切り替え判定用
            nowRecordingFlag = !nowRecordingFlag
        }
    }

    private fun recStop() {
        val intent = Intent(this, RecSoundService::class.java)
        stopService(intent)
        // recSoundBtnの表示を切り替え（録音中→録音終了）
        recSoundBtn.text = resources.getString(R.string.rec_sound_btn)
        // 録音終了時に保存確認ダイアログを表示。 ダイアログ内でファイル名の入力が可能。
        val recSaveConfirmDialog =
            SaveConfirmDialogFragment()
        val arg = Bundle()
        arg.putLong("nextSoundId", nextSoundId)
        recSaveConfirmDialog.arguments = arg
        recSaveConfirmDialog.show(supportFragmentManager, "sample")
    }

    private fun recStart() {
        val intent = Intent(this, RecSoundService::class.java)
        createNextSoundId()
        intent.putExtra("nextSoundId", nextSoundId)
        startService(intent)
        // recSoundBtnの表示を切り替え（録音終了→録音中）
        recSoundBtn.text = resources.getString(R.string.rec_stop_btn)
    }

    private fun createNextSoundId() {
        nextSoundId =
            (realm.where<RecSoundInfoData>().max("soundId")?.toLong() ?: 0L) + 1L
    }
}