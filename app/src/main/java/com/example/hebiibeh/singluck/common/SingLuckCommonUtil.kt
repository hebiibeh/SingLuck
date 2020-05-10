package com.example.hebiibeh.singluck.common

import android.content.Context


class SingLuckCommonUtil(_context: Context) {

    private val context = _context

    /*
     ファイル名、パス取得メソッド
    */
    fun generateDefaultFileName(nextSoundId: Long?): String {
        return Constants.recFileNameDefault + nextSoundId.toString() + Constants.recFileExtension
    }

    fun generateDefaultFileNameNoExtension(nextSoundId: Long?): String {
        return Constants.recFileNameDefault + nextSoundId.toString()
    }

    fun generateDefaultRecFilePath(nextSoundId: Long?): String {
        return context.filesDir.toString() + "/" + Constants.recFileNameDefault + nextSoundId.toString() + Constants.recFileExtension
    }

    fun generateRecFilePath(fileName: String?): String {
        return context.filesDir.toString() + "/" + fileName + Constants.recFileExtension
    }

    fun generateRecFileName(fileName: String?): String {
        return fileName + Constants.recFileExtension
    }
}