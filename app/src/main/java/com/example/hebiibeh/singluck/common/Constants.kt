package com.example.hebiibeh.singluck.common

import android.Manifest

class Constants {
    companion object {
        const val recFileExtension = ".3gp"

        // 録音データ保存時に初期表示されるファイル名
        const val recFileNameDefault = "recSound"

        val permissionList: List<String> =
            listOf(Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    }
}