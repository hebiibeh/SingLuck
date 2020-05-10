package com.example.hebiibeh.singluck.main.playsoundlist

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.IBinder
import com.example.hebiibeh.singluck.common.SingLuckCommonUtil

class PlaySoundListService : Service(), MediaPlayer.OnCompletionListener {

    private var mp: MediaPlayer? = null
    private val activity = PlaySoundListActivity.getInstance()
    private val util = SingLuckCommonUtil(this)
    private var selectedPosition: Int = 0

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        selectedPosition = intent?.getIntExtra("selectedPosition", 0) ?: 0
        val selectedSoundName = intent?.getStringExtra("selectedFileNameNoExtension").toString()

        // 呼び出し元で指定された音声を再生する
        playSound(selectedSoundName)
        // 再生中の曲名、再生&停止ボタンの画面表示を変更する。
        changeViewSoundInfo(selectedSoundName)

        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        mp?.stop()
        activity.setPlayingSoundName("")
        activity.switchPlayAndStopBtnText(false)
    }

    // 再生終了時、再生リストの次の音声を連続再生。次の音声がない場合、再生終了。
    override fun onCompletion(mp: MediaPlayer?) {
        selectedPosition += 1
        val nextSoundName = activity.getSoundNameByPosition(selectedPosition)
        // 次の再生曲が再生リストにある場合
        if (nextSoundName != null && nextSoundName != "") {
            playSound(nextSoundName)
            changeViewSoundInfo(nextSoundName)
        }
        // 次の再生曲が再生リスト無い場合
        else {
            activity.setPlayingSoundName("")
            stopSelf()
        }
    }

    private fun playSound(selectedSoundName: String) {
        val selectedSoundUri: Uri =
            Uri.parse(util.generateRecFilePath(selectedSoundName))
        mp = MediaPlayer.create(this, selectedSoundUri)
        mp?.start()
        mp?.setOnCompletionListener(this)
    }

    private fun changeViewSoundInfo(selectedSoundName: String) {
        activity.setPlayingSoundName(selectedSoundName)
        activity.switchPlayAndStopBtnText(true)
    }
}
