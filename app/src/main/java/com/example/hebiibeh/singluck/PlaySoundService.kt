package com.example.hebiibeh.singluck

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.IBinder

class PlaySoundService : Service() {

    private var mp: MediaPlayer? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        playSound(intent)
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    private fun playSound(intent: Intent?) {
        val selectedSoundFileName = intent?.getStringExtra("selectedSoundFileName")
        val selectedSoundUri: Uri =
            Uri.parse(this.filesDir.toString() + "/" + selectedSoundFileName)

        mp = MediaPlayer.create(this, selectedSoundUri)
        mp?.start()
    }
}
