package com.example.hebiibeh.singluck

import android.app.Service
import android.content.Intent
import android.media.MediaRecorder
import android.os.IBinder
import android.provider.MediaStore
import android.util.Log
import io.realm.Realm
import io.realm.kotlin.createObject
import io.realm.kotlin.where
import java.io.IOException
import java.util.*

class RecSoundService : Service() {

    private var mediaRecorder: MediaRecorder? = null
    private var recFileDir = ""
    private var nextSoundId = 0L
    private lateinit var realm: Realm

    override fun onCreate() {
        super.onCreate()

        recFileDir = this.filesDir.toString()
        realm = Realm.getDefaultInstance()

        startRecording()
        registerRecordSoundData()
    }

    private fun registerRecordSoundData() {

        realm.executeTransaction {
            val recordSoundData = realm.createObject<RecordSoundData>(nextSoundId)
            recordSoundData.fileName = createFileName()
            recordSoundData.displayName = createFileName()
            recordSoundData.createDate = Date()
            recordSoundData.updateDate = Date()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        stopRecording()
        realm.close()
    }

    private fun startRecording() {
        mediaRecorder = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            setOutputFile(createFileName())
            setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)

            try {
                prepare()
            } catch (e: IOException) {
                Log.e("RecSoundService", "prepare() failed")
            }

            start()
        }
    }

    private fun stopRecording() {
        mediaRecorder?.apply {
            stop()
            release()
        }
    }

    private fun createFileName(): String {

        // 登録済みのsoundIdの最大値＋1の値を取得。取得できない場合、1を設定
        nextSoundId =
            (realm.where<RecordSoundData>().max("soundId")?.toLong() ?: 0L) + 1L
        val fileName =
            recFileDir + Constants.recFileName + nextSoundId + Constants.soundFileExtension

        return fileName
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}
