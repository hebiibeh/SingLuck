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
    private var recFileName = ""
    private var nextSoundId = 0L

    fun getNextSoundId(): Long {
        return nextSoundId
    }

    override fun onCreate() {
        super.onCreate()

        realm = Realm.getDefaultInstance()

        createFileName()

        startRecording()
    }

    private fun registerRecordSoundData() {
        realm.executeTransaction {
            val recordSoundData = realm.createObject<RecSoundData>(nextSoundId)
            recordSoundData.fileName = recFileName
            recordSoundData.displayName = recFileName
            recordSoundData.createDate = Date()
            recordSoundData.updateDate = Date()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        stopRecording()
        realm.close()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private fun startRecording() {
        mediaRecorder = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            setOutputFile(recFileName)
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
        registerRecordSoundData()
    }

    private fun createNextSoundId() {
        nextSoundId =
            (realm.where<RecSoundData>().max("soundId")?.toLong() ?: 0L) + 1L
    }

    private fun createFileName() {
        createNextSoundId()
        recFileName =
            this.filesDir.toString() +"/" + Constants.recFileName + nextSoundId + Constants.soundFileExtension
    }
}
