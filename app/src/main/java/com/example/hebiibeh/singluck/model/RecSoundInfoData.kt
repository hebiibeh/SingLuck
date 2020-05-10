package com.example.hebiibeh.singluck.model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.util.Date

open class RecSoundInfoData : RealmObject() {

    @PrimaryKey
    var soundId: Long = 0

    var fileName: String = ""
    var fileNameNoExtension: String = ""
    var createDate: Date = Date()
    var updateDate: Date = Date()
}