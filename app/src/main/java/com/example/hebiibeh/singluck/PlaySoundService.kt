package com.example.hebiibeh.singluck

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.IBinder
import java.io.File

class PlaySoundService : Service() {

    private var mp: MediaPlayer? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        playSound(intent)
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent): IBinder? {
        /*TODO:サービス起動時に自動的に開始する処理はonBindに記載で適切か？他のメソッドに記載するほうが良いか、サービスのライフサイクルの各メソッドの役割を調べる*/

        return null
    }

    private fun playSound(intent: Intent?) {

        val selectedSoundId = intent?.getStringExtra("selectedSoundId")
//TODO:project内部のファイルを指定しているが、端末内部のURIを指定するよう修正する
        val myUri: Uri =
            Uri.parse(this.filesDir.toString() + Constants.recFileName + selectedSoundId + Constants.soundFileExtension)

        // TODO:テスト用
        val file =
            File(this.filesDir.toString() + Constants.recFileName + selectedSoundId + Constants.soundFileExtension)
        val a = file.exists()


        mp = MediaPlayer.create(this, myUri)
        mp?.start()
//        mp = MediaPlayer().apply {
//            setAudioStreamType(AudioManager.STREAM_MUSIC)
//            setDataSource(applicationContext, myUri)
//            prepare()
//            start()
//        }
    }
}
