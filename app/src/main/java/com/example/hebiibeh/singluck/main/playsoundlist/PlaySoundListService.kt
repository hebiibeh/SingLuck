package com.example.hebiibeh.singluck.main.playsoundlist

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.IBinder
import com.example.hebiibeh.singluck.common.SingLuckCommonUtil

class PlaySoundListService : Service(), MediaPlayer.OnCompletionListener {

    private var mp: MediaPlayer? = null
    private val util =
        SingLuckCommonUtil(this)

    private var selectedPosition: Int = 0

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val selectedSoundName = intent?.getStringExtra("selectedFileNameNoExtension")
        selectedPosition = intent?.getIntExtra("selectedPosition", 0) ?: 0
        playSound(selectedSoundName.toString())
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    private fun playSound(selectedSoundName: String) {
        val selectedSoundUri: Uri =
            Uri.parse(util.generateRecFilePath(selectedSoundName))

        mp = MediaPlayer.create(this, selectedSoundUri)
        mp?.start()
        mp?.setOnCompletionListener(this)
        PlaySoundListActivity.getInstance().switchPlayAndStopBtn(true)
    }

    override fun onDestroy() {
        super.onDestroy()
        mp?.stop()
        PlaySoundListActivity.getInstance().switchPlayAndStopBtn(false)
    }

    override fun onCompletion(mp: MediaPlayer?) {
        val activity = PlaySoundListActivity.getInstance()
        selectedPosition = selectedPosition + 1
        val nextSoundName = activity.getSoundNameByPosition(selectedPosition)
        if (nextSoundName != null && nextSoundName != "") {
            playSound(nextSoundName)
        } else {
            stopSelf()
        }
    }
}
