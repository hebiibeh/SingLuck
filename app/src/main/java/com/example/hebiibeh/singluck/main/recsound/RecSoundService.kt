package com.example.hebiibeh.singluck.main.recsound

import android.app.Service
import android.content.Intent
import android.media.MediaRecorder
import android.os.IBinder
import com.example.hebiibeh.singluck.common.SingLuckCommonUtil
import com.example.hebiibeh.singluck.model.RecSoundInfoData
import io.realm.Realm
import io.realm.kotlin.createObject
import java.util.*

class RecSoundService : Service() {

    private var mediaRecorder: MediaRecorder? = null
    private var realm = Realm.getDefaultInstance()
    private val util = SingLuckCommonUtil(this)

    override fun onCreate() {
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        val nextSoundId = intent?.getLongExtra("nextSoundId", 0) ?: 1L

        //　録音開始
        startRecording(nextSoundId)
        // 再生開始時に録音情報をDBに登録
        registRecSoundInfo(nextSoundId)

        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
        stopRecording()
        realm.close()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private fun startRecording(nextSoundId: Long) {
        mediaRecorder = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            setOutputFile(util.generateDefaultRecFilePath(nextSoundId))
            setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)

            prepare()
            start()
        }
    }

    private fun registRecSoundInfo(nextSoundId: Long?) {
        realm.executeTransaction {
            val recSoundInfoData = realm.createObject<RecSoundInfoData>(nextSoundId)

            recSoundInfoData.fileName = util.generateDefaultFileName(nextSoundId)
            recSoundInfoData.fileNameNoExtension =
                util.generateDefaultFileNameNoExtension(nextSoundId)
            recSoundInfoData.createDate = Date()
            recSoundInfoData.updateDate = Date()
        }
    }

    private fun stopRecording() {
        mediaRecorder?.apply {
            stop()
            release()
        }
    }
}