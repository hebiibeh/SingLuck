package com.example.hebiibeh.singluck

import android.app.Service
import android.content.Intent
import android.media.MediaRecorder
import android.os.IBinder
import android.util.Log
import io.realm.Realm
import io.realm.kotlin.createObject
import io.realm.kotlin.where
import java.io.IOException
import java.util.*

class RecSoundService : Service() {

    private var mediaRecorder: MediaRecorder? = null
    private lateinit var realm: Realm
    private val utils = SingLuckCommonUtils(this)

    override fun onCreate() {
        super.onCreate()

        realm = Realm.getDefaultInstance()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        // nextSoundIdがnullになることはないため、第２引数の"0"に意味はない
        val nextSoundId = intent?.getLongExtra("nextSoundId", 0)
        startRecording(nextSoundId)
        registerRecordSoundData(nextSoundId)

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

    private fun startRecording(nextSoundId: Long?) {
        mediaRecorder = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            setOutputFile(utils.generateDefaultRecFilePath(nextSoundId))
            setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)

            try {
                prepare()
            } catch (e: IOException) {
                Log.e("RecSoundService", "prepare() failed")
            }
            start()
        }
    }

    private fun registerRecordSoundData(nextSoundId: Long?) {
        realm.executeTransaction {
            val recordSoundData = realm.createObject<RecSoundData>(nextSoundId)
            recordSoundData.fileName = utils.generateDefaultRecFileName(nextSoundId)
            recordSoundData.fileNameNoExtension =
                utils.generateDefaultRecFileNameNoExtension(nextSoundId)
            recordSoundData.createDate = Date()
            recordSoundData.updateDate = Date()
        }
    }

    private fun stopRecording() {
        mediaRecorder?.apply {
            stop()
            release()
        }
    }
}